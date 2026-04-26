package com.springbatch.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coffee_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CoffeeEntity {

    @Id
    @Column(name ="COFFEE_ID")
    private String coffeeId;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "CHARACTERISTICS")
    private String characteristics;
}
