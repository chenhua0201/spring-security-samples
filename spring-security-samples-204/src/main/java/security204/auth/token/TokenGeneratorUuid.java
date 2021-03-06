package security204.auth.token;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * 生成UUID。
 */
@Component
public class TokenGeneratorUuid implements TokenGenerator {

	@Override
	public String generate(final Object principal) {
		return UUID.randomUUID()
				.toString();
	}

}
