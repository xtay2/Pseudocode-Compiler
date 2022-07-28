package snippets.tokens.abstractions;

import java.util.*;

public class TokenWrapper {

	public final Token token;
	public final String snippet;

	public TokenWrapper(Token token, String content) {
		this.token = Objects.requireNonNull(token);
		snippet = Objects.requireNonNull(content);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Token t)
			return t == token;
		if (obj instanceof TokenWrapper tw)
			return tw.token == token;
		return false;
	}

	@Override
	public String toString() {
		return snippet;
	}
}
