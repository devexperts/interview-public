package com.devexperts.service.bean;

import com.devexperts.account.Account;

/**
 * @author pashkevich.ea
 */
public class TransferHelper {

    private Account first;
    private Account second;

    private TransferSideEnum firstSide;
    private TransferSideEnum secondSide;

    public TransferHelper(Account first, Account second, TransferSideEnum firstSide, TransferSideEnum secondSide) {
        this.first = first;
        this.second = second;
        this.firstSide = firstSide;
        this.secondSide = secondSide;
    }

    public Account getFirst() {
        return first;
    }

    public Account getSecond() {
        return second;
    }

    public TransferSideEnum getFirstSide() {
        return firstSide;
    }

    public TransferSideEnum getSecondSide() {
        return secondSide;
    }
}
