package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Getter
@ToString
public class AwardInterpretationResponse {

    private UUID id;
    private Date date;
    private double units;
    private double cost;

}
