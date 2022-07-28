package snippets.expressions.blueprints;

import snippets.expressions.blockbrackets.*;
import snippets.expressions.names.*;
import snippets.tokens.*;
import structural.*;

public final class Struct extends Blueprint {

	public Struct(ProgramLine l, TypeName name, OpenBlock ob) {
		super(BlueprintToken.STRUCT, l, name, ob);
	}
	
	@Override
	public void check() {
		super.check();
	}

}
