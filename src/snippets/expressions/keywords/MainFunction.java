package snippets.expressions.keywords;

import static snippets.tokens.KeywordToken.*;

import misc.helper.*;
import snippets.expressions.abstractions.*;
import snippets.expressions.blockbrackets.*;
import structural.*;
import throwables.*;

public class MainFunction extends Function {

	public MainFunction(ProgramLine l, OpenBlock ob) {
		super(MAIN, l, ob);
	}

	@Override
	public void check() {
		if (!"Main".equals(sourceFile().fileName()))
			throw new CompilerError(sourceFile(), line.lineIdx, StringHelper.pointUnderlineRunnables(line.textLine(), MAIN.literal()),
					"A main-function can only get defined once in the \"Main.pc\".");
	}

	@Override
	public String generateCode() {
		StringBuilder sb = new StringBuilder("int main() {\n");
		return sb.append("\treturn 0;\n}").toString();
	}
	
}
