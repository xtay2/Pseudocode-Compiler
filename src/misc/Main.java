package misc;

import java.io.*;
import java.util.*;

import misc.helper.*;
import misc.helper.files.*;
import modules.codegeneration.*;
import snippets.expressions.abstractions.*;
import snippets.tokens.abstractions.*;
import structural.*;
import throwables.*;

public final class Main {

	public enum CompilationStage {
		/** Describes the initialization of the compiler. */
		STARTUP,
		/** Describes the stage in which all constructors of {@link SourceFile}s are called. */
		IMPORTING,
		/** Describes the stage in which all {@link SourceFile}s convert their text to {@link Token}s. */
		LEXING,
		/** Describes the stage in which all {@link Token}s get converted to {@link Expression}s. */
		BUILDING,
		/**
		 * Describes the stage in which every {@link MainExpression} undergoes a context-check.
		 *
		 * @see MainExpression#check()
		 */
		CHECKING,
		/** Describes the stage in which the c code gets created. */
		GENERATING;
	}

	private static CompilationStage currentStage;
	
	public static CompilationStage currentStage() {
		return currentStage;
	}

	/////////// MAIN-INIT //////////////////////////////////////////////////
	private static Main MAIN;

	public static void main(String[] args) {
		currentStage = CompilationStage.STARTUP;
		//@formatter:off
		if (args.length < 3)
			throw new IllegalArgumentException("The Compiler has to get launched with following arguments:"
				+ "\n-The project-path"         + (args.length >= 1 ? "\t Was: " + args[0] : "")
				+ "\n-The std-library-path"     + (args.length >= 2 ? "\t Was: " + args[1] : "")
				+ "\n-The usr-library-path"     + (args.length >= 3 ? "\t Was: " + args[2] : "")
			);
		//@formatter:on
		MAIN = new Main(args[0], args[1], args[2]);
		MAIN.importAll();
		MAIN.tokenizeAll();
		MAIN.buildAll();
		MAIN.checkAll();
		MAIN.generateAll();
		System.out.println("Compiled successfully");
	}
	
	public static Main get() {
		return MAIN;
	}
	
	////////////////////////////////////////////////////////////////////////

	public final String launchPath, stdlibPath, usrlibPath;

	private final Map<SourcePath, SourceFile> files = new HashMap<>();

	private static final String MAIN_FILE = "Main" + SourceFile.EXTENSION;

	/**
	 * Creates a new {@link Main}-Singleton.
	 *
	 * @param launchPath is the path to the root dir of the project.
	 * @param stdlibPath is the path to the stdlib-dir.
	 * @param usrlibPath is the path to the usrlib-dir.
	 */
	private Main(String launchPath, String stdlibPath, String usrlibPath) {
		this.launchPath = launchPath;
		this.stdlibPath = stdlibPath;
		this.usrlibPath = usrlibPath;
	}

	/**
	 * Saves a compiled {@link SourceFile} if its not already registered. After that, all of its import
	 * also get registered.
	 *
	 * @return the {@link SourceFile}
	 */
	public SourceFile register(SourcePath path) {
		if (files.containsKey(path))
			return files.get(path);
		SourceFile file = FileManager.generate(path);
		files.put(path, file);
		file.importAll();
		return file;
	}

	/**
	 * Looks up a compiled {@link SourceFile}. If it doesn't exist yet, {@link Optional#empty()} gets
	 * returned.
	 */
	public Optional<SourceFile> getFile(SourcePath path) {
		return Optional.ofNullable(files.get(path));
	}

	private void importAll() {
		SourcePath mainPath;
		try {
			mainPath = new SourcePath(Location.SRC + ".." + "Main");
		} catch (IOException e) {
			throw new CompilerError("A project has to have exactly one \"" + MAIN_FILE + "\"-file.", e);
		}
		currentStage = CompilationStage.IMPORTING;
		register(mainPath);
		System.out.println("Importing-stage completed successfully");
		System.out.println(StringHelper.enumerateLines(files.values()));
	}

	private void tokenizeAll() {
		currentStage = CompilationStage.LEXING;
		files.forEach((p, f) -> f.tokenizeFile());
		System.out.println("Lexing-stage completed successfully");
	}

	private void buildAll() {
		currentStage = CompilationStage.BUILDING;
		files.forEach((p, f) -> f.buildFile());
		System.out.println("Building-stage completed successfully");
	}

	private void checkAll() {
		currentStage = CompilationStage.CHECKING;
		files.forEach((p, f) -> f.checkFile());
		System.out.println("Checking-stage completed successfully");
	}
	
	private void generateAll() {
		currentStage = CompilationStage.GENERATING;
		files.forEach((p, f) -> FileGenerator.generate(f));
		System.out.println("Generator-stage completed successfully");
	}

}
