package modules.builder;

import java.util.*;

import snippets.expressions.abstractions.*;
import snippets.tokens.*;
import snippets.tokens.abstractions.*;
import structural.*;

public class Builder {
	
	static ProgramLine pLine;
	static List<TokenWrapper> tokens;
	
	public static MainExpression build(ProgramLine line) {
		pLine = line;
		tokens = new ArrayList<>(line.tokens);
		return (MainExpression) buildStartOfLine();
	}
	
	private static Expression buildStartOfLine() {
		return switch (tokens.get(0).token) {
			// Blueprint-Declaration
			case BlueprintToken bpt -> BlueprintBuilder.buildBlueprint();
			// Var-Declaration
			case NameToken nmt && nmt == NameToken.TYPE_NAME -> NameBuilder.buildVarDeclaration();
			// Close-Block
			case BlockBracketToken bbt && bbt == BlockBracketToken.CLOSE_BLOCK -> BlockBracketBuilder.buildCloseBlock();
			// Main-function
			case KeywordToken kwt && kwt == KeywordToken.MAIN -> KeywordBuilder.buildMain();
			default -> unexpectedToken();
		};
	}

	static Expression unexpectedToken() throws AssertionError {
		throw new AssertionError("Unexpected Token: " + tokens.get(0));
	}

}
