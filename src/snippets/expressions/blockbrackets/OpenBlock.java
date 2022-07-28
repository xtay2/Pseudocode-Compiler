package snippets.expressions.blockbrackets;

import java.util.*;

import modules.builder.*;
import snippets.expressions.abstractions.*;
import snippets.tokens.*;
import snippets.tokens.abstractions.*;
import structural.*;

/**
 * @see CloseBlock
 * @see BlockBracketToken
 * @see BlockBracketBuilder
 */
public final class OpenBlock extends Expression implements BlockBracket {
	
	private final CloseBlock partner;
	
	public OpenBlock(ProgramLine l) {
		super(BlockBracketToken.OPEN_BLOCK, l);
		partner = findPartner();
	}
	
	/**
	 * This method finds the corresponding {@link CloseBlock}. It assumes that:
	 * <pre>
	 * -the matching bracket exists.
	 * -that its already an initialised {@link MainExpression}.
	 * -that every {@link ProgramLine} that contains a
	 *  {@link BlockBracketToken} has just one at the end of its line.
	 * </pre>
	 */
	private CloseBlock findPartner() {
		int brack = 0;
		for (int ln = line.lineIdx; ln < sourceFile().size(); ln++) {
			List<TokenWrapper> tokens = sourceFile().getLine(ln).tokens;
			Token pbbt = tokens.get(tokens.size() - 1).token;
			if (pbbt == BlockBracketToken.OPEN_BLOCK)
				brack++;
			else if (pbbt == BlockBracketToken.CLOSE_BLOCK) {
				brack--;
				if (brack == 0) {
					CloseBlock cb = (CloseBlock) sourceFile().getLine(ln).mainExp;
					assert cb != null : "MainExpression of " + sourceFile().getLine(ln) + " should be initialised as a CloseBlock.";
					return cb;
				}
			}
		}
		throw new AssertionError("Found no matching close-block for " + this);
	}

	public CloseBlock partner() {
		return partner;
	}
	
	@Override
	public String toString() {
		return "[" + token + (partner != null ? partner.signature : "") + "]";
	}

}
