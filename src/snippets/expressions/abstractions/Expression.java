package snippets.expressions.abstractions;

import snippets.tokens.abstractions.*;
import structural.*;

public abstract class Expression {
	
	public final Token token;
	public final ProgramLine line;
	
	public Expression(Token t, ProgramLine line) {
		token = t;
		this.line = line;
	}
	
	public final SourceFile sourceFile() {
		return line.file;
	}
	
	protected String clsName() {
		return getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return "[" + token + "]";
	}

}
