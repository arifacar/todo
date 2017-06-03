package com.arifacar.api.rest.quote;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.common.service.common.EnumService;
import com.arifacar.domain.model.quote.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/quote")
public class QuoteRestController extends BaseController {

    @RequestMapping("/random")
    public Quote findValueByCategoryIdAndValueId() {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        return quote;
    }

}
