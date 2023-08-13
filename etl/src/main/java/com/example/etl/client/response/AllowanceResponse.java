package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class AllowanceResponse {

    private UUID id;
    private double value;
    private double cost;

}
