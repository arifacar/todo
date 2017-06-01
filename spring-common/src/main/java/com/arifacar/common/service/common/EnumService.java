package com.arifacar.common.service.common;

import com.arifacar.domain.model.common.EnumValue;
import com.arifacar.domain.repository.common.EnumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnumService {

    @Autowired
    private EnumRepository enumRepository;


    public EnumValue save(EnumValue enumValue) {
        EnumValue persistedEnumValue = null;

        persistedEnumValue = enumRepository.save(enumValue);

        return persistedEnumValue;
    }
}
