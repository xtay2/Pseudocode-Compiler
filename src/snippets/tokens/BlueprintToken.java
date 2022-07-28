package snippets.tokens;

import static snippets.tokens.NameToken.*;
import static snippets.tokens.abstractions.patterns.TokenPattern.*;

import java.util.*;

import snippets.tokens.abstractions.*;
import snippets.tokens.abstractions.patterns.*;
import structural.*;

public enum BlueprintToken implements LiteralMatchToken {
	
	CLASS, STRUCT, MODULE;
	
	@Override
	public Optional<Token[]> check(SourceFile context) {
		String keyword = literal();
		// All blueprints currently match the same pattern.
		TokenPattern pattern = new TokenPattern(TYPE_NAME);
		return switch (this) {
			case CLASS -> compute(context, keyword, //
					pattern //
				);
			case MODULE -> compute(context, keyword, //
					pattern //
				);
			case STRUCT -> compute(context, keyword, //
					pattern //
				);
		};
	}

	@Override
	public String literal() {
		return name().toLowerCase();
	}
	
}
