package security301.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import security301.auth.restful.LoginFormAuthenticationFilter;
import security301.auth.restful.RestAccessDeniedHandler;
import security301.auth.restful.RestAuthenticationFailureHandler;
import security301.auth.restful.RestAuthenticationSuccessHandler;
import security301.auth.token.TokenAuthenticationFilter;

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

	@Autowired
	private TokenAuthenticationFilter tokenAuthenticationFilter;

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
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// spring security不创建session
				.and()
				.authorizeRequests()
				.antMatchers("/hello/**")// /hello接口需要ADMIN角色
				.hasRole("ADMIN")
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

		// token过滤器
		http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
	}
}