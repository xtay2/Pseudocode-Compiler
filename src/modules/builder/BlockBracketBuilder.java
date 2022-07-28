package modules.builder;

import snippets.expressions.blockbrackets.*;

public class BlockBracketBuilder extends Builder {

	public static OpenBlock buildOpenBlock() {
		return new OpenBlock(pLine);
	}
	
	public static CloseBlock buildCloseBlock() {
		return new CloseBlock(pLine);
	}
	
}
