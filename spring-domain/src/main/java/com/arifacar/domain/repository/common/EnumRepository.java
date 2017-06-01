package com.arifacar.domain.repository.common;

import com.arifacar.domain.model.common.EnumValue;
import com.arifacar.domain.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnumRepository extends CrudRepository<EnumValue, Long> {

}
