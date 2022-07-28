package snippets.expressions.names;

import snippets.tokens.*;
import structural.*;

public final class TypeName extends Name {
	
	public TypeName(ProgramLine l, String literalString) {
		super(NameToken.TYPE_NAME, l, literalString);
	}
}
