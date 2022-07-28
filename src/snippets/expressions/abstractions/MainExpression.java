package snippets.expressions.abstractions;

import java.util.*;

import misc.helper.*;
import snippets.expressions.blueprints.*;
import snippets.tokens.*;
import snippets.tokens.abstractions.*;
import structural.*;
import throwables.*;

public abstract class MainExpression extends Expression {

	public MainExpression(Token t, ProgramLine l) {
		super(t, l);
	}

	/** Checks if this {@link MainExpression} is valid. */
	public abstract void check();

	/**
	 * Checks if this lies anywhere between the brackets of any one of the passed
	 * {@link BlueprintToken}s.
	 */
	protected final void isInBlueprint(BlueprintToken... blueprints) {
		SourceFile file = sourceFile();
		for (ProgramLine ln : file.lineIter())
			if (ln.mainExp instanceof Blueprint bp) {
				if (!CollectionHelper.contains(blueprints, bp.token))
					throw new CompilerError(file, line.lineIdx,
							"A " + clsName() + " can only occur in " + Arrays.toString(blueprints) + "\n" + this //
					);
				if (bp.openBlock().line.lineIdx >= line.lineIdx || bp.closeBlock().line.lineIdx <= line.lineIdx)
					throw new CompilerError(file, line.lineIdx,
							"This " + clsName() + " has to be defined inside the " + bp.token + "\n" + this //
					);
			}
	}
}
