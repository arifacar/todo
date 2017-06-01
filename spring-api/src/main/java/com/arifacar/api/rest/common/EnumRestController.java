package com.arifacar.api.rest.common;

import com.arifacar.common.service.common.EnumService;
import com.arifacar.domain.model.common.EnumValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum")
public class EnumRestController extends BaseController {

    @Autowired
    private EnumService enumService;

    @RequestMapping("/test")
    public EnumValue findValueByCategoryIdAndValueId() {
        return new EnumValue("URL", "java-master", 1L);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public EnumValue save(@RequestBody EnumValue enumValue) {
        return enumService.save(enumValue);
    }

}
