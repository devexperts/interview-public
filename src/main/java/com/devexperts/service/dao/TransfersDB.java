package com.devexperts.service.dao;


import com.devexperts.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TransfersDB extends CrudRepository<Transfer, Long> {
}
