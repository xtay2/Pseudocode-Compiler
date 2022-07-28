package snippets.tokens.abstractions;

import java.util.*;
import java.util.stream.*;

import snippets.tokens.*;

/**
 * @see PatternMatchToken
 */
public non-sealed interface LiteralMatchToken extends Token {

	/**
	 * {@link Token}s like {@link NameToken} match the source code with regex-patterns. This method
	 * returns the pattern for a specific {@link PatternMatchToken}.
	 */
	String literal();
	
	/** Tells if a string matches any literal of the passed {@link LiteralMatchToken} class. */
	static boolean matchesAnyLiteral(String s, Class<? extends LiteralMatchToken> cls) {
		for (LiteralMatchToken t : Token.values(cls))
			if (t.literal().equals(s))
				return true;
		return false;
	}

	/**
	 * Returns a regex-pattern-string that matches any of the literal tokens.
	 *
	 * For example: {@link BlockBracketToken} is "({|})".
	 */
	static String anyRegex(Class<? extends LiteralMatchToken> cls) {
		return Arrays.stream(Token.values(cls)).map(LiteralMatchToken::literal).collect(Collectors.joining("|", "(", ")"));
	}
}
