package com.devexperts.transfer;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "SOURCE_ID")
    private Long sourceId;
    @Column(name = "TARGET_ID")
    private Long targetId;
    @Column(name = "AMOUNT")
    private Double amount;
    @CreationTimestamp
    @Column(name = "TRANSFER_TIME")
    private Date transferTime;

    public Transfer(Long sourceId, Long targetId, Double amount) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
    }
}
