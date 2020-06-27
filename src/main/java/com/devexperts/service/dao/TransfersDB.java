package com.devexperts.service.dao;


import com.devexperts.account.Transfer;
import org.springframework.data.repository.CrudRepository;

public interface TransfersDB extends CrudRepository<Transfer, Long> {
}
