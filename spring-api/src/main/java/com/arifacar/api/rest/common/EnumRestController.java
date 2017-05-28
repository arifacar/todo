package com.arifacar.api.rest.common;

import com.arifacar.domain.model.common.EnumValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum")
public class EnumRestController extends BaseController {

	@RequestMapping("/value")
	public EnumValue findValueByCategoryIdAndValueId(
			@RequestParam("categoryId") Long categoryId,
			@RequestParam("valueId") Long valueId) {

		return new EnumValue();
	}

}
