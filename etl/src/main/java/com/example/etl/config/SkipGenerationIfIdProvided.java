package com.example.etl.config;

import com.example.etl.model.BaseEntity;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

public class SkipGenerationIfIdProvided extends UUIDGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return ((BaseEntity) obj).getId() != null
                ? ((BaseEntity) obj).getId()
                : super.generate(s, obj);
    }

}
