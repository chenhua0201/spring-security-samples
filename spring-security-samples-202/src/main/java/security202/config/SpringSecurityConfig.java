package security202.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import security202.restful.LoginFormAuthenticationFilter;
import security202.restful.RestAccessDeniedHandler;
import security202.restful.RestAuthenticationFailureHandler;
import security202.restful.RestAuthenticationSuccessHandler;

/**
 * Spring security配置。
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestAccessDeniedHandler restAccessDeniedHandler;

	@Autowired
	private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

	@Autowired
	private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 认证过滤器
		final LoginFormAuthenticationFilter filter = new LoginFormAuthenticationFilter(objectMapper);
		filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
		filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
		filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		filter.setPostOnly(true);
		filter.setAuthenticationManager(authenticationManagerBean());

		http.csrf()
				.disable()// 关闭csrf，避免postman之类的客户端无法获得csrf token
				.authorizeRequests()
				.anyRequest()
				.authenticated()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 未登录
				.accessDeniedHandler(restAccessDeniedHandler)// 已登录，无权限
				.and()
				.formLogin()
				.and()
				.logout()
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

		// 用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}
}