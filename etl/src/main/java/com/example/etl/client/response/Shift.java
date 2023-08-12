package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Shift {

    private UUID id;
    private Date date;
    private long start;
    private long finish;
    private List<Break> breaks;
    private List<Allowance> allowances;
    private List<AwardInterpretation> awardInterpretations;

}
