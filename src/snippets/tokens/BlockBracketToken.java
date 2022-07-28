package snippets.tokens;

import static snippets.tokens.abstractions.patterns.TokenPattern.*;

import java.util.*;

import snippets.expressions.blockbrackets.*;
import snippets.tokens.abstractions.*;
import snippets.tokens.abstractions.patterns.*;
import structural.*;

/**
 * A {@link BlockBracket} is either { or }. It can occur once at the end of any {@link ProgramLine}.
 */
public enum BlockBracketToken implements LiteralMatchToken {
	
	OPEN_BLOCK("{"),
	
	CLOSE_BLOCK("}");

	public final String symbol;

	BlockBracketToken(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public Optional<Token[]> check(SourceFile context) {
		return switch (this) {
			case OPEN_BLOCK -> compute(context, symbol, //
					new TokenPattern(NONE).anyOf(BlueprintToken.values()).one(NameToken.TYPE_NAME), // Blueprint-Definition
					new TokenPattern(NONE).one(KeywordToken.MAIN) //
				);
			case CLOSE_BLOCK -> compute(context, symbol, //
					new TokenPattern(NONE) // Normal close block
				);
		};
	}
	
	@Override
	public String literal() {
		return symbol;
	}
}
