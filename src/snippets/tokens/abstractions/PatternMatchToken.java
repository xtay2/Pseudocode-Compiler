package snippets.tokens.abstractions;

import java.util.regex.*;

import snippets.tokens.*;

/**
 * @see LiteralMatchToken
 */
public non-sealed interface PatternMatchToken extends Token {
	
	/**
	 * {@link Token}s like {@link NameToken} match the source code with regex-patterns. This method
	 * returns the pattern for a specific {@link PatternMatchToken}.
	 */
	Pattern pattern();

	/** Tells if a string matches any pattern of the passed {@link PatternMatchToken} class. */
	static boolean matchesAnyPattern(String s, Class<? extends PatternMatchToken> cls) {
		for (PatternMatchToken t : Token.values(cls))
			if (t.pattern().matcher(s).matches())
				return true;
		return false;
	}
}
