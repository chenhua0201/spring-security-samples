package security202.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页。
 */
@RestController
@RequestMapping("/")
public class IndexController {

	@GetMapping
	public String index() {
		return "这是首页";
	}

}
