package com.example.api.rest.common;

import com.example.WebServiceClientSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/soap")
public class WebServiceController {

    @Autowired
    WebServiceClientSample webServiceClientSample;

    @RequestMapping("/webservice")
    public boolean webservice() {
        return webServiceClientSample.simpleSendAndReceive();
    }


}
