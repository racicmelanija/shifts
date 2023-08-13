package com.example.etl.service;

import com.example.etl.client.ShiftsApiClient;
import com.example.etl.model.Shift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartEtlJob {

    private final ShiftsApiClient client;
    private final LoadData loadData;
    private final CalculateKpis calculateKpis;

    public void execute() {
        List<Shift> data = client.fetchAllData();
        loadData.execute(data);
        calculateKpis.execute();
    }

}
