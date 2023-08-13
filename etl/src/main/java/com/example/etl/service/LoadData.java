package com.example.etl.service;

import com.example.etl.repository.ShiftRepository;
import com.example.etl.model.Shift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadData {

    private final ShiftRepository shiftRepository;
    private final EntityManager entityManager;

    @Transactional
    public void execute(List<Shift> shifts) {
        int batchSize = 50;

        for (int i = 0; i < shifts.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, shifts.size());
            List<Shift> batch = shifts.subList(i, endIndex);

            shiftRepository.saveAll(batch);

            entityManager.flush();
            entityManager.clear();
        }
    }

}
