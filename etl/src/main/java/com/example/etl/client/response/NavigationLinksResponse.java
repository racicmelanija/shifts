package com.example.etl.client.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NavigationLinksResponse {

    private String base;
    private String prev;
    private String next;

}
