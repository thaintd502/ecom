package com.ecom2.branch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transfer_detail")
@Data
public class TransferDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferDetailId;

    @ManyToOne
    @JoinColumn(name = "transfer_id", referencedColumnName = "transferId")
    private Transfer transfer;

    @Column
    private String detail;
}
