package com.example.etl.predicate;

import com.example.etl.client.response.ShiftsApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.function.Predicate;

public class ConditionalRetryPredicate implements Predicate<ResponseEntity<ShiftsApiResponse>> {

    @Override
    public boolean test(ResponseEntity<ShiftsApiResponse> res) {
        return res.getStatusCode().is5xxServerError()
                || res.getStatusCode() == HttpStatusCode.valueOf(429);
    }

}
