package com.example.etl.service;

import com.example.etl.repository.KpiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalculateKpis {

    private final KpiRepository kpiRepository;

    @Transactional
    public void execute() {
        if (kpiRepository.kpisExistForCurrentDate()) {
            return;
        }

        kpiRepository.insertKpis();
    }

}
