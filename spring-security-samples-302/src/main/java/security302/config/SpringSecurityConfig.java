package security302.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import security302.auth.restful.LoginFormAuthenticationFilter;
import security302.auth.restful.RestAccessDeniedHandler;
import security302.auth.restful.RestAuthenticationFailureHandler;
import security302.auth.restful.RestAuthenticationSuccessHandler;
import security302.auth.token.TokenAuthenticationFilter;

/**
 * Spring security配置。
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestAccessDeniedHandler restAccessDeniedHandler;

	@Autowired
	private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

	@Autowired
	private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private TokenAuthenticationFilter tokenAuthenticationFilter;

	@Autowired
	private LogoutHandler tokenLogoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.disable()// 关闭csrf，避免postman之类的客户端无法获得csrf token
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// spring security不创建session
				.and()
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
				.addLogoutHandler(tokenLogoutHandler)
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

		// 认证过滤器
		final LoginFormAuthenticationFilter filter = new LoginFormAuthenticationFilter(objectMapper);
		filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
		filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
		filter.setAuthenticationManager(authenticationManagerBean());

		// 用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		// token过滤器
		http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
	}
}