package snippets.expressions.declarations;

import snippets.expressions.abstractions.*;
import snippets.expressions.names.*;
import snippets.tokens.*;
import structural.*;

public final class VarDeclaration extends MainExpression {
	
	public final TypeName typeName;
	public final VarName varName;

	public VarDeclaration(ProgramLine l, TypeName tn, VarName vn) {
		super(NameToken.VAR_NAME, l);
		typeName = tn;
		varName = vn;
	}
	
	@Override
	public void check() {
		isInBlueprint(BlueprintToken.CLASS, BlueprintToken.STRUCT);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof VarDeclaration vDecl && typeName.equals(vDecl.typeName) && varName.equals(vDecl.varName);
	}

	@Override
	public String toString() {
		return typeName + " " + varName;
	}

}
