package snippets.expressions.abstractions;

import java.util.*;

import snippets.expressions.blockbrackets.*;
import snippets.tokens.abstractions.*;
import structural.*;

public abstract class BlockHolder extends MainExpression {
	
	private final OpenBlock ob;
	private final CloseBlock cb;

	public BlockHolder(Token t, ProgramLine l, OpenBlock ob) {
		super(t, l);
		this.ob = Objects.requireNonNull(ob);
		cb = Objects.requireNonNull(ob.partner());
	}

	public final OpenBlock openBlock() {
		return ob;
	}
	
	public final CloseBlock closeBlock() {
		return cb;
	}
}
