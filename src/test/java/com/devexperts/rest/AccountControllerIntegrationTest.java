package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.rest.AccountController;
import com.devexperts.service.AccountService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class AccountControllerIntegrationTest {
    @Value("${application.transfer.executor.thread-pool-max-size}")
    private int concurrencyTaskPoolSize;

    @Autowired
    AccountService accountService;

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void resetCaches() {
        accountService.clear();
    }

    @Test
    @SneakyThrows
    public void sunnyDayConcurrentTest() {
        int threadSize = concurrencyTaskPoolSize;
        final List<UUID> accountIds = new CopyOnWriteArrayList<>();
        AtomicReference<BigDecimal> result = new AtomicReference<>(BigDecimal.ZERO);
        CyclicBarrier startThreads = new CyclicBarrier(threadSize + 1);
        CyclicBarrier endThreads = new CyclicBarrier(threadSize + 1);
        IntStream.range(0, threadSize + 1)
                .forEach(index -> {
                    UUID accountId = UUID.randomUUID();
                    Account account = new Account(AccountKey.valueOf(accountId),
                            "testName " + index,
                            "testLastname " + index,
                            BigDecimal.valueOf(100));
                    accountService.createAccount(account);
                    accountIds.add(accountId);
                    new Thread(() -> {
                        try {
                            String apiUrl = "http://localhost:8080/api/operations/transfer?sourceId={}&targetId={}&amount={}";
                            if (index == 0) {
                                return;
                            }
                            UUID first = accountIds.get(0);
                            UUID next = accountIds.get(index);
                            BigDecimal amount = BigDecimal.valueOf(1);
                            result.getAndUpdate(res -> res.add(amount));

                            String url = apiUrl
                                    .replaceFirst("[{][}]", first.toString())
                                    .replaceFirst("[{][}]", next.toString())
                                    .replaceFirst("[{][}]", amount.toString());
                            startThreads.await();

                            try {
                                restTemplate.exchange(URI.create(url), HttpMethod.POST, RequestEntity.EMPTY, Void.class);
                            } finally {
                                endThreads.await();
                            }
                        } catch (InterruptedException e) {
                            //
                        } catch (BrokenBarrierException e) {
                            //
                        }
                    }).start();
                });
        startThreads.await();
        endThreads.await();

        result.getAndUpdate(res -> BigDecimal.valueOf(100).subtract(res));
        Assertions.assertEquals(result.get(), accountService.getAccount(accountIds.get(0)).getBalance());
    }

    @Test
    public void accountNotFoundTest() {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(10);

        HttpStatusCodeException error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    public void nullArgumentsTest() {
        UUID source = null;
        UUID target = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(10);

        HttpStatusCodeException error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());

        source = UUID.randomUUID();
        target = null;
        error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());

        target = UUID.randomUUID();
        amount = null;
        error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }

    @Test
    public void notEnoughMoneyTest() {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(125);

        Account sourceAcc = new Account(AccountKey.valueOf(source),
                "testName_1",
                "testLastname_1",
                BigDecimal.valueOf(100));
        Account targetAcc = new Account(AccountKey.valueOf(target),
                "testName_2",
                "testLastname_2",
                BigDecimal.valueOf(100));

        accountService.createAccount(sourceAcc);
        accountService.createAccount(targetAcc);
        HttpStatusCodeException error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void invalidAmountTest() {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        BigDecimal amount = BigDecimal.ZERO;

        Account sourceAcc = new Account(AccountKey.valueOf(source),
                "testName_1",
                "testLastname_1",
                BigDecimal.valueOf(100));
        Account targetAcc = new Account(AccountKey.valueOf(target),
                "testName_1",
                "testLastname_2",
                BigDecimal.valueOf(100));

        accountService.createAccount(sourceAcc);
        accountService.createAccount(targetAcc);
        HttpStatusCodeException error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());

        amount = BigDecimal.valueOf(-123);
        error = executeErrorRequest(source, target, amount);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }

    protected HttpStatusCodeException executeErrorRequest(UUID source, UUID target, BigDecimal amount) {
        String apiUrl = "http://localhost:8080/api/operations/transfer?sourceId={}&targetId={}&amount={}";

        String url = apiUrl
                .replaceFirst("[{][}]", source == null ? "" : source.toString())
                .replaceFirst("[{][}]", target == null ? "" : target.toString())
                .replaceFirst("[{][}]", amount == null ? "" : amount.toString());
        try {
           restTemplate.exchange(URI.create(url), HttpMethod.POST, RequestEntity.EMPTY, Void.class);
        } catch (HttpStatusCodeException e) {
            return e;
        }

        return null;
    }
}
