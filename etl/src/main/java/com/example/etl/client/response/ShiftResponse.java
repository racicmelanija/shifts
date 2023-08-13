package com.example.etl.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class ShiftResponse {

    private UUID id;
    private Date date;
    private long start;
    private long finish;
    private List<BreakResponse> breaks;
    private List<AllowanceResponse> allowances;

    @JsonProperty("award_interpretations")
    private List<AwardInterpretationResponse> awardInterpretations;

}
