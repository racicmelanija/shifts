package com.example.etl.controller;

import com.example.etl.service.StartEtlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EtlJobController {

    private final StartEtlJob startEtlJob;

    @GetMapping("/start-etl-job")
    public void startEtlJob() {
        startEtlJob.execute();
    }

}
