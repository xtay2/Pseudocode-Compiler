package modules.builder;

import snippets.expressions.blueprints.*;
import snippets.expressions.blueprints.Class;
import snippets.expressions.blueprints.Module;
import snippets.tokens.*;

public class BlueprintBuilder extends Builder {

	public static Blueprint buildBlueprint() {
		return switch ((BlueprintToken) tokens.remove(0).token) {
			case CLASS -> BlueprintBuilder.buildClass();
			case STRUCT -> BlueprintBuilder.buildStruct();
			case MODULE -> BlueprintBuilder.buildModule();
		};
	}

	private static Class buildClass() {
		return new Class(pLine, NameBuilder.buildTypeName(), BlockBracketBuilder.buildOpenBlock());
	}
	
	private static Struct buildStruct() {
		return new Struct(pLine, NameBuilder.buildTypeName(), BlockBracketBuilder.buildOpenBlock());
	}
	
	private static Module buildModule() {
		return new Module(pLine, NameBuilder.buildTypeName(), BlockBracketBuilder.buildOpenBlock());
	}
}
