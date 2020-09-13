package security204.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import security204.auth.restful.LoginFormAuthenticationFilter;
import security204.auth.restful.RestAccessDeniedHandler;
import security204.auth.restful.RestAuthenticationFailureHandler;
import security204.auth.restful.RestAuthenticationSuccessHandler;
import security204.auth.token.GrantedAuthorityConverter;
import security204.auth.token.TokenAuthenticationFilter;
import security204.auth.token.TokenAuthenticationProvider;
import security204.auth.token.TokenProperties;
import security204.auth.token.TokenService;

/**
 * Spring security配置。
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private GrantedAuthorityConverter grantedAuthorityConverter;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestAccessDeniedHandler restAccessDeniedHandler;

	@Autowired
	private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

	@Autowired
	private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private LogoutHandler tokenLogoutHandler;

	@Autowired
	private TokenProperties tokenProperties;

	@Autowired
	private TokenService tokenService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new TokenAuthenticationProvider(tokenService));
	}

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
		final TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(
				authenticationManagerBean(), grantedAuthorityConverter, tokenProperties, tokenService);
		http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
	}
}