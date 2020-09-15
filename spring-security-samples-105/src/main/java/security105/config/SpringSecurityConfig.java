package security105.config;

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
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 创建多个内存用户
		auth.inMemoryAuthentication()
				.withUser("zhangsan")// 用户张三
				.password(passwordEncoder.encode("87654321"))
				.roles("")// 本例不需要角色
				.and()
				.withUser("lisi")// 用户李四
				.password(passwordEncoder.encode("222222"))
				.roles("");// 本例不需要角色
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorize -> authorize.mvcMatchers("/")// 不需要鉴权
				.permitAll()
				.anyRequest()// 所有其他请求都需要鉴权，会自动排除登录和注销接口
				.authenticated())
				.formLogin();
	}

}
