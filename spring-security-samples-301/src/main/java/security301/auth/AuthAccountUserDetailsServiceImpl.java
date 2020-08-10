package security301.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import security301.auth.entity.AuthAccount;
import security301.auth.entity.AuthRole;
import security301.auth.repository.AuthAccountRepository;
import security301.auth.repository.AuthRoleRepository;

/**
 * 实现Spring Security的{@link UserDetailsService}。
 */
@Service
@Slf4j
public class AuthAccountUserDetailsServiceImpl implements UserDetailsService {

	private final AuthAccountRepository authAccountRepository;

	private final AuthRoleRepository authRoleRepository;

	@Autowired
	public AuthAccountUserDetailsServiceImpl(AuthAccountRepository authAccountRepository,
			AuthRoleRepository authRoleRepository) {
		this.authAccountRepository = authAccountRepository;
		this.authRoleRepository = authRoleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			log.warn("Username is empty");
			throw new UsernameNotFoundException("账号不存在：" + username);
		}

		final AuthAccount account = authAccountRepository.findByUsername(username);
		if (account == null || account.isDeleted()) {
			log.warn("Username not found: {}", username);
			throw new UsernameNotFoundException("账号不存在：" + username);
		}

		if (!account.isEnabled()) {
			log.warn("Username disabled: {}", username);
			throw new DisabledException("账号被禁用：" + username);
		}

		// 角色的名称作为GrantedAuthority
		final Set<GrantedAuthority> grantedAuthorities = authRoleRepository.findByAccountId(account.getId())
				.stream()
				.map(AuthRole::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());

		return new AuthAccountUserDetails(account, grantedAuthorities);
	}
}