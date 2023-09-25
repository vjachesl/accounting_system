package com.task.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="currency_generator")
    @SequenceGenerator(name = "currency_generator", sequenceName = "currency_seq", allocationSize = 1)
    private long id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Override
    public String toString() {
        return shortName;
    }
}
