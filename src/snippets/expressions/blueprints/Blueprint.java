package snippets.expressions.blueprints;

import java.util.*;

import misc.helper.*;
import snippets.expressions.abstractions.*;
import snippets.expressions.blockbrackets.*;
import snippets.expressions.declarations.*;
import snippets.expressions.names.*;
import snippets.tokens.*;
import structural.*;
import throwables.*;

public abstract sealed class Blueprint extends BlockHolder implements NameHolder permits Class, Struct, Module {
	
	private final Name name;
	
	public Set<Function> functions;
	public Set<VarDeclaration> attributes;
	
	public Blueprint(BlueprintToken token, ProgramLine l, TypeName name, OpenBlock ob) {
		super(token, l, ob);
		this.name = name;
	}

	private final void initFunctions() {
		functions = new HashSet<>();
		for (ProgramLine l : sourceFile().lineIter())
			if (l.mainExp instanceof Function f)
				functions.add(f);
	}
	
	private final void initAttributes() {
		attributes = new HashSet<>();
		for (ProgramLine l : sourceFile().lineIter())
			if (l.mainExp instanceof VarDeclaration varDecl) {
				if (attributes.stream().anyMatch(v -> v.equals(varDecl)))
					throw new CompilerError(sourceFile(), l.lineIdx,
							"The " + clsName() + " " + nameAsString() + " contains multiple identical attributes:\n" + varDecl);
				attributes.add(varDecl);
			}
	}

	@Override
	public void check() {
		if (!nameAsString().equals(sourceFile().fileName()))
			throw new CompilerError(sourceFile(), line.lineIdx,
					"The name of the " + clsName() + " \"" + nameAsString() + "\" has to match the name of its file \""
							+ sourceFile().fileName() + "\".", //
					StringHelper.pointUnderlineRunnables(line.textLine(), nameAsString()));
		initFunctions();
		initAttributes();
		attributes.forEach(var -> {
			String varName = var.typeName.nameAsString();
			if (sourceFile().getImport(varName).isEmpty())
				throw new CompilerError(sourceFile(), var.line.lineIdx, StringHelper.pointUnderlineRunnables(var.line.textLine(), varName),
						"Type \"" + varName + "\" isn't imported.\nImports:\n" + StringHelper.enumerateLines(sourceFile().imports()));
		});
	}

	public final void allowsNoAttributes() {
		if (!attributes.isEmpty()) {
			VarDeclaration v = (VarDeclaration) attributes.toArray()[0];
			throw new CompilerError(sourceFile(), v.line.lineIdx, "A " + clsName() + " allows no attributes.");
		}
	}
	
	@Override
	protected String clsName() {
		return super.clsName().toLowerCase();
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + name() + " " + openBlock();
	}

	@Override
	public Name name() {
		return name;
	}
}
