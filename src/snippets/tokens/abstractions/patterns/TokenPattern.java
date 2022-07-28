package snippets.tokens.abstractions.patterns;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;

import snippets.tokens.abstractions.*;
import snippets.tokens.abstractions.patterns.PatternOperation.*;
import structural.*;

public final class TokenPattern {

	private final List<PatternOperation> operations = new ArrayList<>();
	private final Token[] followers;

	/** Creates an empty token pattern that excepts nothing. */
	public TokenPattern(Token... followers) {
		this.followers = followers;
	}

	/** Requires that exactly one of the passed tokens occures exactly once. */
	public TokenPattern one(Token... tokens) {
		operations.add(new PatternOperation(Selector.ONE, tokens));
		return this;
	}

	/** Any amount of the passed tokens can occur in any order. */
	public TokenPattern anyOf(Token... tokens) {
		operations.add(new PatternOperation(Selector.ANY, tokens));
		return this;
	}

	/** Returns true if the {@link SourceFile#currentTokens()} match this {@link TokenPattern}. */
	private boolean matches(SourceFile context) {
		Token[] currentLine = context.currentTokens();
		if (currentLine.length != operations.size())
			return false;
		int i = 0;
		for (; i < currentLine.length; i++)
			if (!operations.get(i).matches(currentLine[i]))
				return false;
		return i == currentLine.length && i == operations.size();
	}

	/**
	 * Computes if a {@link Token} matches exactly one of the passed {@link TokenPattern}s.
	 *
	 * @param context is the {@link SourceFile} without the token.
	 * @param textualMatch is either a literal {@link String}- or a {@link Pattern}-check.
	 * @param patterns are all patterns that the searched token can match.
	 * @return either an {@link Optional#empty()} if no pattern matches the token, an
	 * {@link Optional#of()} an empty array when a pattern matches, but no following tokens are
	 * expected or an {@link Optional#of()} all tokens that can follow the matching patterns.
	 */
	private static Optional<Token[]> compute(SourceFile context, Predicate<SourceFile> textualMatch, TokenPattern... patterns) {
		boolean matched = false;
		List<Token> possibleFollowers = new ArrayList<>();
		for (TokenPattern pattern : patterns)
			if (textualMatch.test(context) && pattern.matches(context)) {
				matched = true;
				possibleFollowers.addAll(List.of(pattern.followers));
			}
		return matched ? Optional.of(possibleFollowers.toArray(Token[]::new)) : Optional.empty();
	}

	/**
	 * Computes if a {@link Token} matches this {@link TokenPattern}.
	 *
	 * @param literal is a {@link String} that the {@link SourceFile#currentSnippet()} has to match.
	 * @see TokenPattern#compute(SourceFile, Predicate, TokenPattern...)
	 */
	public static Optional<Token[]> compute(SourceFile context, String literal, TokenPattern... patterns) {
		return compute(context, cnt -> cnt.currentSnippet().equals(literal), patterns);
	}

	/**
	 * Computes if a {@link Token} matches this {@link TokenPattern}.
	 *
	 * @param regex is a {@link Pattern} that the {@link SourceFile#currentSnippet()} has to match.
	 * @see TokenPattern#compute(SourceFile, Predicate, TokenPattern...)
	 */
	public static Optional<Token[]> compute(SourceFile context, Pattern regex, TokenPattern... patterns) {
		return compute(context, cnt -> {
			String snippet = cnt.currentSnippet();
			Character nextSnippet = cnt.nextSnippet();
			return regex.matcher(snippet).matches() && (nextSnippet == null || !regex.matcher(snippet + cnt.nextSnippet()).matches());
		}, patterns);
	}
}
