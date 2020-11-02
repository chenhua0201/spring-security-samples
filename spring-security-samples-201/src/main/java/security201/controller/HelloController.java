package security201.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示API。
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

	/**
	 * 返回当前用户的Authentication。
	 *
	 * @return 当前用户的Authentication
	 */
	@GetMapping
	public Object hello() {
		return SecurityContextHolder.getContext()
				.getAuthentication();
	}

}
