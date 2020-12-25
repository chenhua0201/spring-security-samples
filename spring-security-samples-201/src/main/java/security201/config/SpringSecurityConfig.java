package security201.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring security配置。
 */
@Configuration
class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf()
				.disable()// 关闭csrf，避免postman之类的客户端无法获得csrf token
				.authorizeRequests()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin();
	}

}
