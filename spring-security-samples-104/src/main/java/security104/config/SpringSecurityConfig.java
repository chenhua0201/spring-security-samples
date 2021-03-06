package security104.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring security配置。
 */
@Configuration
class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		// 创建多个内存用户
		auth.inMemoryAuthentication()
				.withUser("zhangsan")// 用户张三
				.password(passwordEncoder.encode("87654321"))
				.authorities(Collections.emptyList()) // 本例不需要角色
				.and()
				.withUser("lisi")// 用户李四
				.password(passwordEncoder.encode("222222"))
				.authorities(Collections.emptyList());// 本例不需要角色
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// 本例并没有提供/sys/signin和/sys/signout页面，因此浏览器会显示404错误
		http.authorizeRequests()
				.anyRequest()
				.authenticated()// 所有请求都需要鉴权
				.and()
				.formLogin()
				.loginPage("/sys/signin")
				.permitAll()
				.and()
				// 注销接口地址
				.logout()
				.logoutUrl("/sys/signout")
				.permitAll();
	}

}
