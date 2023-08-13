package com.example.etl.client;

import com.example.etl.client.response.ShiftsApiResponse;
import com.example.etl.model.Shift;
import com.example.etl.service.TransformData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftsApiClient {

    private final RestTemplate restTemplate;
    private final TransformData transformData;
    private static final int LIMIT = 30;

    public List<Shift> fetchAllData() {
        return fetchData(0, new ArrayList<>());
    }

    private List<Shift> fetchData(int start, List<Shift> data) {
        ResponseEntity<ShiftsApiResponse> response = sendGetRequest(start);
        ShiftsApiResponse shiftsApiResponse = response.getBody();
        if (shiftsApiResponse == null) {
            throw new RuntimeException();
        }

        data.addAll(transformData.execute(shiftsApiResponse));

        if (shiftsApiResponse.hasNextPage()) {
            fetchData(start + LIMIT, data);
        }

        return data;
    }

    @Retry(name = "shiftsApiClientRetryPolicy")
    public ResponseEntity<ShiftsApiResponse> sendGetRequest(int start) {
        String url = "/shifts?start={start}&limit={limit}";
        return restTemplate.getForEntity(url, ShiftsApiResponse.class, start, LIMIT);
    }

}
