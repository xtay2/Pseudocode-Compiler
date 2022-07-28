package modules.builder;

import snippets.expressions.declarations.*;
import snippets.expressions.names.*;

public class NameBuilder extends Builder {
	
	public static VarDeclaration buildVarDeclaration() {
		return new VarDeclaration(pLine, buildTypeName(), buildVarName());
	}
	
	public static TypeName buildTypeName() {
		return new TypeName(pLine, tokens.remove(0).snippet);
	}

	public static VarName buildVarName() {
		return new VarName(pLine, tokens.remove(0).snippet);
	}

}
