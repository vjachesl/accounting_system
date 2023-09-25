package com.task.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
        @Id
        @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="company_generator")
        @SequenceGenerator(name = "company_generator", sequenceName = "company_seq", allocationSize = 1)
        private long id;

        @Column(name = "company_name", nullable = false)
        private String companyName;

        @Column(name = "company_code", nullable = false)
        private Integer companyCode;

        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "company_id")
        private List<Address> addressList;
        
        @Column(name = "email_address", nullable = false)
        private String email;

        @Column(name = "phone_number", nullable = false)
        private String phone;

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_at", nullable = false)
        private Date createdAt;

    }
