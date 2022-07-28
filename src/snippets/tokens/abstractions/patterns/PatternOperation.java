package snippets.tokens.abstractions.patterns;

import java.util.*;

import misc.helper.*;
import snippets.tokens.abstractions.*;

public final class PatternOperation {

	/**
	 * Specifies, how many of the passed tokens from the constructor has to get matched by
	 * {@link PatternOperation#matches(Token)}.
	 */
	public enum Selector {
		ONE, ANY;
	}

	final Selector selected;
	final Token[] expected;

	/** A sample of tokens that have to get matched against a selector. */
	public PatternOperation(Selector s, Token... t) {
		selected = Objects.requireNonNull(s);
		expected = Objects.requireNonNull(t);
	}
	
	/** Returns true if the passed token matches the {@link Selector}. */
	boolean matches(Token token) {
		return switch (selected) {
			case ONE -> CollectionHelper.contains(expected, token);
			case ANY -> Arrays.stream(expected).anyMatch(exp -> exp.equals(token));
		};
	}
}
