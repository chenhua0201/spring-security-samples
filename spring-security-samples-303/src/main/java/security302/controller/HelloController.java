package security302.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
	 * 返回当前用户的principal，即
	 * {@link org.springframework.security.core.userdetails.User}。
	 *
	 * @return 当前用户的principal
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public String hello() {
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal()
				.toString();
	}
}
