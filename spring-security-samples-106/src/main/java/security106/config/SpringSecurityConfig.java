package security106.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

}
