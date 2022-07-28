package snippets.tokens;

import static snippets.tokens.BlockBracketToken.*;
import static snippets.tokens.abstractions.patterns.TokenPattern.*;

import java.util.*;

import snippets.tokens.abstractions.*;
import snippets.tokens.abstractions.patterns.*;
import structural.*;

public enum KeywordToken implements LiteralMatchToken {

	MAIN("main");

	public final String keyword;

	KeywordToken(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public Optional<Token[]> check(SourceFile context) {
		return switch (this) {
			case MAIN -> compute(context, keyword, //
					new TokenPattern(OPEN_BLOCK) //
				);
		};
	}
	
	@Override
	public String literal() {
		return keyword;
	}
}
