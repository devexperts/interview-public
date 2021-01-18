package com.devexperts.facade;

/**
 * @author pashkevich.ea
 */
public interface AccountFacade {

    void transfer(long sourceId, long targetId, double amount);
}
