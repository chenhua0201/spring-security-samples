package security302.auth.token;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 与String互相转换。
 */
@Component
public class GrantedAuthorityConverter {

	public Collection<? extends GrantedAuthority> decode(Set<String> strings) {
		if (strings == null) {
			return Collections.emptySet();
		}

		return strings.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}

	public Set<String> encode(Collection<? extends GrantedAuthority> authorities) {
		if (authorities == null) {
			return Collections.emptySet();
		}

		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}
}
