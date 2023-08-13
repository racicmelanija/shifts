package com.example.etl;

import com.example.etl.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
}
