package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class BreakResponse {

    private UUID id;
    private long start;
    private long finish;
    private boolean paid;

}
