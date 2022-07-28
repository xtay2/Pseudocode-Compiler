package snippets.expressions.blockbrackets;

import java.util.*;

import modules.builder.*;
import snippets.expressions.abstractions.*;
import snippets.tokens.*;
import structural.*;

/**
 * @see OpenBlock
 * @see BlockBracketToken
 * @see BlockBracketBuilder
 */
public final class CloseBlock extends MainExpression implements BlockBracket {
	
	private static int BLOCK_SIGN = 0;
	protected final String signature;

	public CloseBlock(ProgramLine l) {
		super(BlockBracketToken.CLOSE_BLOCK, l);
		signature = "x" + HexFormat.of().toHexDigits(BLOCK_SIGN++).toString();
	}

	@Override
	public void check() {
		// TODO Implement me!
	}

	@Override
	public String toString() {
		return "[" + token + signature + "]";
	}

}
