package structural;

import java.util.*;

import modules.builder.*;
import snippets.expressions.abstractions.*;
import snippets.tokens.abstractions.*;
import throwables.*;

public class ProgramLine {

	public final List<TokenWrapper> tokens = new ArrayList<>();
	public MainExpression mainExp;
	public final SourceFile file;
	public final int lineIdx;

	public ProgramLine(SourceFile file, int lineIdx) {
		this.file = file;
		this.lineIdx = lineIdx;
	}

	/** Set the {@link #mainExp} of this {@link ProgramLine}. */
	public void build() {
		mainExp = Builder.build(this);
		if (mainExp == null)
			throw new CompilerError(file, lineIdx, file.getText(lineIdx), "Couldn't detect any expression.");
	}
	
	/** Returns the last build token of this line, or null if nothing exists. */
	public Token lastToken() {
		return tokens.isEmpty() ? null : tokens.get(tokens.size() - 1).token;
	}
	
	public Token[] tokens() {
		return tokens.stream().map(w -> w.token).toArray(Token[]::new);
	}

	public String textLine() {
		return file.getText(lineIdx);
	}
	
	@Override
	public String toString() {
		return "line " + file.orgLineNr(lineIdx);
	}

}
