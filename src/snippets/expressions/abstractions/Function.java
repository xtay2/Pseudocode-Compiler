package snippets.expressions.abstractions;

import snippets.expressions.blockbrackets.*;
import snippets.tokens.abstractions.*;
import structural.*;

public abstract class Function extends BlockHolder {

	public Function(Token t, ProgramLine l, OpenBlock ob) {
		super(t, l, ob);
	}

	public abstract String generateCode();
	
}
