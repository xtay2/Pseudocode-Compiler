package snippets.expressions.blueprints;

import snippets.expressions.blockbrackets.*;
import snippets.expressions.names.*;
import snippets.tokens.*;
import structural.*;

public final class Class extends Blueprint {

	public Class(ProgramLine l, TypeName name, OpenBlock ob) {
		super(BlueprintToken.CLASS, l, name, ob);
	}
	
	@Override
	public void check() {
		super.check();
	}

}
