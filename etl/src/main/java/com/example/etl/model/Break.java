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
import java.util.Date;

@Entity
@Table(name = "breaks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class Break extends BaseEntity {

    private Date start;
    private Date finish;
    private boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Shift shift;

}
