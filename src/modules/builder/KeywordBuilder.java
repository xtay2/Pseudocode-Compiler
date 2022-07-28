package modules.builder;

import snippets.expressions.keywords.*;

public class KeywordBuilder extends Builder {

	public static MainFunction buildMain() {
		return new MainFunction(pLine, BlockBracketBuilder.buildOpenBlock());
	}
}
