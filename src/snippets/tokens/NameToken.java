package snippets.tokens;

import static snippets.tokens.BlockBracketToken.*;
import static snippets.tokens.abstractions.patterns.TokenPattern.*;

import java.util.*;
import java.util.regex.*;

import misc.*;
import misc.Main.*;
import snippets.tokens.abstractions.*;
import snippets.tokens.abstractions.patterns.*;
import structural.*;
import throwables.*;

public enum NameToken implements PatternMatchToken {

	TYPE_NAME("\\b([A-Z][a-z]*)+\\b"),

	VAR_NAME("\\b[a-z](_|([a-z_]?[a-z])*)\\d*\\b");

	public final Pattern pattern;

	NameToken(String regex) {
		pattern = Pattern.compile(regex);
	}

	@Override
	public Optional<Token[]> check(SourceFile context) {
		return switch (this) {
			case TYPE_NAME -> compute(context, pattern, //
					new TokenPattern(OPEN_BLOCK).anyOf(BlueprintToken.values()), // Blueprint-Definition
					new TokenPattern(VAR_NAME) // Var-Definition
				);
			case VAR_NAME -> compute(context, pattern, //
					new TokenPattern(NONE).one(TYPE_NAME) // Var-Definition
				);
		};
	}

	@Override
	public Pattern pattern() {
		return pattern;
	}
	
	public static final void throwNewTypeNameError(String proposedTypeName, SourceFile context, String pointUnderlined) {
		Main.get();
		assert Main.currentStage() == CompilationStage.LEXING;
		String error = "Couldn't identify type-name.";
		if (proposedTypeName.matches(".*_.*"))
			error = "A type-name cannot contain underscores.";
		else if (proposedTypeName.matches(".*\\d.*"))
			error = "A type-name cannot contain any digits.";
		else if (proposedTypeName.matches("[a-z].*"))
			error = "A type-name has to start with an uppercase character.";
		throw new CompilerError(context, context.currentLine().lineIdx, pointUnderlined, error //
				+ "\nWas: " + proposedTypeName//
		);
	}
	
	public static final void throwNewVarNameError(String proposedVarName, SourceFile context, String pointUnderlined) {
		Main.get();
		assert Main.currentStage() == CompilationStage.LEXING;
		String error = "Couldn't identify var-name.";
		if (proposedVarName.matches(".*[A-Z].*"))
			error = "A var-name cannot contain uppercase characters.";
		else if (proposedVarName.matches(".*[0-9][a-z]+"))
			error = "A var-name can only contain numbers at its very end.";
		else if (proposedVarName.matches("(_.*)|(.*_)"))
			error = "A var-name cannot start or end with an underscore.";
		else if (proposedVarName.matches("(.*__.*)"))
			error = "A var-name cannot contain multiple following underscores.";
		else if (proposedVarName.matches("\\d+"))
			error = "A var-name cannot only consist of digits.";
		throw new CompilerError(context, context.currentLine().lineIdx, pointUnderlined, error //
				+ "\nWas: " + proposedVarName//
		);
	}
}
