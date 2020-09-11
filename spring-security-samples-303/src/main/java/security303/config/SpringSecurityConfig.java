package security303.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import security303.auth.restful.LoginFormAuthenticationFilter;
import security303.auth.restful.RestAccessDeniedHandler;
import security303.auth.restful.RestAuthenticationFailureHandler;
import security303.auth.restful.RestAuthenticationSuccessHandler;
import security303.auth.token.TokenAuthenticationFilter;
import security303.authz.UrlAccessDecisionVoter;
import security303.authz.UrlPermissionSecurityMetadataSource;

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

	@Autowired
	private LogoutHandler tokenLogoutHandler;

	@Autowired
	private UrlAccessDecisionVoter urlAccessDecisionVoter;

	@Autowired
	private UrlPermissionSecurityMetadataSource urlPermissionSecurityMetadataSource;

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
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
						fsi.setSecurityMetadataSource(urlPermissionSecurityMetadataSource);
						fsi.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(urlAccessDecisionVoter)));
						return fsi;
					}
				})
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