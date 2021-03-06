package security202.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import security202.auth.entity.AuthAccount;
import security202.auth.repository.AuthAccountRepository;

/**
 * 实现Spring Security的{@link UserDetailsService}。
 */
@Service
@Slf4j
public class AuthAccountUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AuthAccountRepository authAccountRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			log.warn("Username is empty");
			throw new UsernameNotFoundException("账号不存在：" + username);
		}

		final AuthAccount account = authAccountRepository.findByUsername(username);
		if (account == null) {
			log.warn("Username not found: {}", username);
			throw new UsernameNotFoundException("账号不存在：" + username);
		}

		return new AuthAccountUserDetails(account);
	}

}
