package throwables;

import misc.helper.*;
import snippets.tokens.abstractions.*;
import structural.*;

/**
 * A {@link CompilerError} for when exactly one {@link Token} is missing, but the compiler can guess
 * which one.
 */
public class MissingTokenError extends CompilerError {
	
	public MissingTokenError(SourceFile file, int lineID, String line, Token t) {
		super(file, lineID, "Missing " + t + " after:\n" //
				+ StringHelper.pointUnderline(line + " " + t, line.length() + 1, t.toString().length()));
	}
	
}
