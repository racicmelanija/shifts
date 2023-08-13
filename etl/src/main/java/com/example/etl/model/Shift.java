package com.example.etl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class Shift extends BaseEntity {

    private Date date;
    private Date start;
    private Date finish;
    private double cost;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Break> breaks;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Allowance> allowances;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AwardInterpretation> awardInterpretations;

}
