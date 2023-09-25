package com.task.model;

import com.task.model.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="operation_generator")
    @SequenceGenerator(name = "operation_generator", sequenceName = "operation_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="document_id")
    private PaymentDocument paymentDocument;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="corresp_account", nullable = false)
    private Account correspAccount;

    @Column(name = "creditAmount")
    private BigDecimal creditAmount;

    @Column(name = "debitAmount")
    private BigDecimal debitAmount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="currency_id", nullable=false)
    private Currency currency;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
