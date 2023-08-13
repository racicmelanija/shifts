package com.example.etl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "allowances")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class Allowance extends BaseEntity{

    private double value;
    private double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Shift shift;

}
