package com.example.etl.service;

import com.example.etl.client.ShiftsApiClient;
import com.example.etl.client.response.ShiftsApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartEtlJob {

    private final ShiftsApiClient client;

    public void execute() {
        List<ShiftsApiResponse> res = client.fetchAllData();

        System.out.println("Response Body: " + res);
    }

}
