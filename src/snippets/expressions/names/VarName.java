package snippets.expressions.names;

import snippets.tokens.*;
import structural.*;

public final class VarName extends Name {
	
	public VarName(ProgramLine l, String literalString) {
		super(NameToken.VAR_NAME, l, literalString);
	}
}