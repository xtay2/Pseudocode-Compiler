package snippets.expressions.names;

import snippets.expressions.abstractions.*;
import snippets.tokens.*;
import structural.*;

public abstract sealed class Name extends Expression implements NameHolder permits TypeName, VarName {

	private final String literalName;
	
	public Name(NameToken t, ProgramLine l, String literalName) {
		super(t, l);
		assert t.pattern.matcher(literalName).matches() : "This should get checked before.";
		this.literalName = literalName;
	}
	
	@Override
	public final Name name() {
		return this;
	}
	
	@Override
	public final String nameAsString() {
		return literalName;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Name n && nameAsString().equals(n.nameAsString());
	}
	
	@Override
	public final String toString() {
		return "[" + token + ": " + nameAsString() + "]";
	}
	
}
