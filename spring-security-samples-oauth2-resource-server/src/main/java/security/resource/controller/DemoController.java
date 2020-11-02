package security.resource.controller;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demos")
public class DemoController {

	@GetMapping
	public Object findAuthentication() throws IOException {
		return SecurityContextHolder.getContext()
				.getAuthentication();
	}

}
