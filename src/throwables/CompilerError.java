package throwables;

import misc.*;
import misc.Main.*;
import misc.helper.*;
import structural.*;

public class CompilerError extends Error {
	
	/**
	 * Use this constructor when an error without a known location occured.
	 *
	 * @param message is a description of the error.
	 * @param cause is the {@link Throwable} that caused this.
	 */
	public CompilerError(String message, Throwable cause) {
		super("Error at an unknown location:\n" + message);
		initCause(cause);
		assert Main.currentStage() == CompilationStage.STARTUP : "After the startup-stage, the errors should be file-related.";
	}
	
	/**
	 * Use this constructor when something is wrong with a path, or a whole file.
	 *
	 * @param file is the {@link SourceFile} or {@link SourcePath} that contains the error.
	 * @param message is a description of the error.
	 */
	public CompilerError(Pathable file, String message) {
		super("Error in \"" + file.fileName() + "\":\n" + message);
		assert Main.currentStage() == CompilationStage.IMPORTING //
				: "After the importing-stage, the lines in which errors occur should be known.";
	}

	/**
	 * Identical to {@link CompilerError#CompilerError(Pathable, String)} but with an
	 * attached cause.
	 */
	public CompilerError(Pathable file, String message, Throwable cause) {
		this(file, message);
		initCause(cause);
	}
	
	/**
	 * Use this constructor when the bit of wrong code in the line is known.
	 *
	 * @param file is the {@link SourceFile} that contains the error.
	 * @param lineIdx is the generated line-index.
	 * @param line is the underlined line.
	 * @param message is a description of the error.
	 *
	 * @see StringHelper#pointUnderline(String, int, int)
	 */
	public CompilerError(SourceFile file, int lineIdx, String line, String message) {
		// TODO blueprint angeben
		super("Error in line " + file.orgLineNr(lineIdx) + " of file \"" + file.fileName() + "\":\n" //
				+ message + "\n"//
				+ "\nCurrent State of line:\n" //
				+ line //
		);
	}

	/**
	 * Identical to {@link CompilerError#CompilerError(SourceFile, int, String, String)} but with an
	 * attached cause.
	 */
	public CompilerError(SourceFile file, int lineIdx, String line, String message, Throwable cause) {
		this(file, lineIdx, line, message);
		initCause(cause);
	}
	
	/**
	 * Use this constructor when something is wrong with the whole line, or the wrong bit is
	 * unidentifieable.
	 *
	 * @param file is the {@link SourceFile} that contains the error.
	 * @param lineIdx is the generated line-index.
	 * @param message is a description of the error.
	 */
	public CompilerError(SourceFile file, int lineIdx, String message) {
		this(file, lineIdx, file.getText(lineIdx), message);
	}

	/**
	 * Identical to {@link CompilerError#CompilerError(SourceFile, int, String)} but with an
	 * attached cause.
	 */
	public CompilerError(SourceFile file, int lineIdx, String message, Throwable cause) {
		this(file, lineIdx, message);
		initCause(cause);
	}
}
