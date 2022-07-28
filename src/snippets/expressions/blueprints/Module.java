package snippets.expressions.blueprints;

import snippets.expressions.blockbrackets.*;
import snippets.expressions.names.*;
import snippets.tokens.*;
import structural.*;

public final class Module extends Blueprint {
	
	public Module(ProgramLine l, TypeName name, OpenBlock ob) {
		super(BlueprintToken.MODULE, l, name, ob);
	}
	
	@Override
	public void check() {
		super.check();
		allowsNoAttributes();
	}
}
