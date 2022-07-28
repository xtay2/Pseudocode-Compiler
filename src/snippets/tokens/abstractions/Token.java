package snippets.tokens.abstractions;

import java.util.*;

import structural.*;

public sealed interface Token permits PatternMatchToken, LiteralMatchToken {

	/**
	 * This should get returned by {@link #next(SourceFile)}, if no further tokens are expected in that
	 * {@link ProgramLine}.
	 */
	Token[] NONE = {};
	
	/**
	 * Checks, if this {@link Token} matches the passed context and evaluates, which tokens can follow
	 * in this context.
	 *
	 * @param context is the currently known context without this token.
	 * @return an array of possible followers or {@link Optional#empty()} if it doesn't match anything.
	 */
	Optional<Token[]> check(SourceFile context);
	
	/**
	 * This is identical to {@link Enum#values()} and should only get used, when iterating over all
	 * tokens of that particular class.
	 */
	@SuppressWarnings("unchecked")
	static <T extends Token> T[] values(Class<T> cls) {
		try {
			return (T[]) cls.getMethod("values").invoke(null);
		} catch (Exception e) {
			throw new AssertionError("Every subclass of token should be an enum and therefor support values().", e);
		}
	}
}
