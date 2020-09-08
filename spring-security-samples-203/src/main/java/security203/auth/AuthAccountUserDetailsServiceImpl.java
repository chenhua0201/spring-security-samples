package security203.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import security203.auth.entity.AuthAccount;
import security203.auth.repository.AuthAccountRepository;

/**
 * 实现Spring Security的{@link UserDetailsService}。
 */
@Service
@Slf4j
public class AuthAccountUserDetailsServiceImpl implements UserDetailsService {

	private final AuthAccountRepository authAccountRepository;

	@Autowired
	public AuthAccountUserDetailsServiceImpl(AuthAccountRepository authAccountRepository) {
		this.authAccountRepository = authAccountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
