package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ShiftsApiResponse {

    private List<Shift> results;
    private NavigationLinks links;
    private int start;
    private int limit;
    private int size;

    public boolean hasNextPage() {
        return links.getNext() != null;
    }

}
