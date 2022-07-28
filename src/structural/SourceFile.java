package structural;

import static misc.helper.StringHelper.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import misc.*;
import misc.Main.*;
import misc.helper.*;
import misc.helper.util.*;
import snippets.expressions.abstractions.*;
import snippets.expressions.blueprints.*;
import snippets.tokens.*;
import snippets.tokens.abstractions.*;
import throwables.*;

/**
 * A {@link SourceFile}, often named as "context" is regular file of sourcecode that overviews the
 * parsing of said code. Most of the methods only return proper results while
 * {@link #tokenizeFile()}
 * is running, so they assert to be only called when the program {@link #isCurrentlyParsing}.
 */
public final class SourceFile implements Pathable {
	
	public static final String EXTENSION = ".pc";
	public static final String IMPORT_KEYWORD = "import";
	
	/**
	 * This contains all imported
	 *
	 * Key: Blueprint/FileName
	 * Value: All imported SourceFiles
	 */
	private final Map<String, SourcePath> imports;
	private final List<Tuple<String, Integer>> textLines;
	private final List<ProgramLine> programLines = new ArrayList<>();
	private final SourcePath path;

	public SourceFile(SourcePath path, List<Tuple<String, Integer>> textList) {
		this.path = path;
		imports = textList.stream() // Load imports
				.takeWhile(l -> l.x().startsWith(IMPORT_KEYWORD)) //
				.map(l -> {
					try {
						return new SourcePath(l.x().substring(IMPORT_KEYWORD.length()));
					} catch (IOException e) {
						throw new CompilerError(path, "Invalid import/-statement:\n" + l.x(), e);
					}
				}).collect(Collectors.toMap(SourcePath::fileName, f -> f));
		textLines = textList.stream().dropWhile(l -> l.x().startsWith(IMPORT_KEYWORD)).toList();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Registers and imports every {@link SourceFile} recursivly.
	 */
	public void importAll() {
		assert Main.currentStage() == CompilationStage.IMPORTING;
		for (SourcePath p : imports.values())
			Main.get().register(p);
	}

	private String currentSnippet = "";
	private Character nextChar;
	private Token[] expectedTokens;
	
	/** This method parses the {@link #textLines} into tokens. */
	public void tokenizeFile() {
		assert Main.currentStage() == CompilationStage.LEXING;
		for (int lineIdx = 0; lineIdx < textLines.size(); lineIdx++) {
			String line = textLines.get(lineIdx).x();
			programLines.add(new ProgramLine(this, lineIdx));
			IterableLine lnBuffer = new IterableLine(line);
			expectedTokens = startOfLineTokens();
			try {
				for (char c : lnBuffer) {
					currentSnippet += c;
					nextChar = lnBuffer.peek();
					if (doesOneTokenMatch()) {
						lnBuffer.foundMatch(currentSnippet.length());
						currentSnippet = "";
					}
				}
				if (expectedTokens.length == 1)
					throw new MissingTokenError(this, getCurrentLine(), line, expectedTokens[0]);
				if (expectedTokens.length > 0)
					throw new UnresolvedCompilerError("Expected another token after:\n"//
							+ pointUnderline(line, line.length() + 1, 5));
			} catch (UnresolvedCompilerError e) {
				if (expectedTokens.length == 1) {
					String propName = lnBuffer.peekWord();
					int propNameLen = propName.length();
					if (expectedTokens[0] == NameToken.VAR_NAME)
						NameToken.throwNewVarNameError(propName, this,
								pointUnderline(line, lnBuffer.posInLine() - propNameLen, propNameLen));
					if (expectedTokens[0] == NameToken.TYPE_NAME)
						NameToken.throwNewTypeNameError(propName, this,
								pointUnderline(line, lnBuffer.posInLine() - propNameLen, propNameLen));
				}
				if (currentLine().lastToken() == NameToken.VAR_NAME)
					NameToken.throwNewVarNameError(lnBuffer.peekWord(), this, pointUnderline(line, lnBuffer.posInLine() - 1));
				if (currentLine().lastToken() == NameToken.TYPE_NAME)
					NameToken.throwNewTypeNameError(lnBuffer.peekWord(), this, pointUnderline(line, lnBuffer.posInLine() - 1));
				throw new CompilerError(this, lineIdx, "Unreadable line.\nExpected: " + Arrays.toString(expectedTokens), e);
			}
		}
	}
	
	/**
	 * Takes a number of expected tokens and checks if {@link #currentSnippet} matches against them. If
	 * there is exactly one {@link Token} that does, it gets added to the {@link #currentLine()} and
	 * possible following tokens get set.
	 *
	 * @return true if a match was found.
	 */
	private boolean doesOneTokenMatch() {
		assert Main.currentStage() == CompilationStage.LEXING;
		Token match = null;
		Token[] followers = null;
		for (Token t : expectedTokens) {
			Optional<Token[]> proposed = t.check(this);
			if (proposed.isPresent()) {
				if (match != null)
					return false;
				match = t;
				followers = proposed.get();
			}
		}
		if (match == null)
			return false;
		currentLine().tokens.add(new TokenWrapper(match, currentSnippet));
		expectedTokens = followers;
		return true;
	}
	
	/** Create a {@link MainExpression} for every {@link ProgramLine}. */
	public void buildFile() {
		assert Main.currentStage() == CompilationStage.BUILDING;
		for (ProgramLine l : programLines) // All Close Blocks (Need no context)
			if (l.tokens.size() == 1 && BlockBracketToken.CLOSE_BLOCK.equals(l.tokens.get(0).token))
				l.build();
		for (ProgramLine l : programLines) // Everything else
			if (l.mainExp == null)
				l.build();
	}
	
	public void checkFile() {
		assert Main.currentStage() == CompilationStage.CHECKING;
		for (ProgramLine l : programLines) // Check all expressions
			l.mainExp.check();
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	
	/** Returns an array of tokens that are currently expected through context. */
	public Token[] expectedTokens() {
		assert Main.currentStage() == CompilationStage.LEXING;
		return expectedTokens;
	}
	
	/**
	 * Returns an array of tokens that are currently expected through context, or could potentially
	 * stand at the beginning of this line.
	 */
	public Token[] startOfLineTokens() {
		assert Main.currentStage() == CompilationStage.LEXING;
		if (!inBlueprint())
			return BlueprintToken.values();
		if (!inFunction()) // In Blueprint but not in Function
			return new Token[] {NameToken.TYPE_NAME, KeywordToken.MAIN, BlockBracketToken.CLOSE_BLOCK};
		return new Token[] {NameToken.TYPE_NAME, BlockBracketToken.CLOSE_BLOCK}; // In Blueprint / In Function
	}
	
	/** Returns the index of the line thats currently under construction. */
	public int getCurrentLine() {
		assert Main.currentStage() == CompilationStage.LEXING;
		return programLines.size() - 1;
	}
	
	/** Returns the newest, possibly unbuild line of the program. This is never null. */
	public ProgramLine currentLine() {
		assert Main.currentStage() == CompilationStage.LEXING;
		assert !programLines.isEmpty();
		return programLines.get(getCurrentLine());
	}
	
	/** Returns an array of the {@link Token}s in the {@link #currentLine()} */
	public Token[] currentTokens() {
		assert Main.currentStage() == CompilationStage.LEXING;
		return currentLine().tokens();
	}
	
	/** Returns the {@link String} that currently gets parsed. */
	public String currentSnippet() {
		assert Main.currentStage() == CompilationStage.LEXING;
		return currentSnippet;
	}
	
	/**
	 * Returns the next Character that will get added to the {@link #currentSnippet}. This can be null.
	 */
	public Character nextSnippet() {
		assert Main.currentStage() == CompilationStage.LEXING;
		return nextChar;
	}
	
	/** Returns true if the current position is in a {@link BlueprintToken}. */
	private boolean inBlueprint() {
		assert Main.currentStage() == CompilationStage.LEXING;
		for (ProgramLine line : programLines)
			if (CollectionHelper.containsAny(line.tokens(), BlueprintToken.values()))
				return true;
		return false;
	}

	/** Returns true if the current position is in a {@link Function}. */
	private boolean inFunction() {
		// TODO Implement me!
	}

	// GETTERS (Undocumented)

	/** Returns an imported {@link SourceFile}. */
	public Optional<SourceFile> getImport(String blueprintName) {
		assert Main.currentStage() == CompilationStage.CHECKING;
		return Main.get().getFile(imports.get(blueprintName));
	}
	
	public Collection<String> importNames() {
		return imports.keySet();
	}

	public Collection<SourcePath> imports() {
		return imports.values();
	}
	
	public String getText(int lineID) {
		return textLines.get(lineID).x();
	}
	
	public ProgramLine getLine(int lineID) {
		return programLines.get(lineID);
	}

	public Blueprint getBlueprint() { return (Blueprint) programLines.get(0).mainExp; }
	
	/**
	 * Returns the number of {@link #textLines}. During the {@link CompilationStage#LEXING} this
	 * can/will be different to the amount of {@link #programLines}.
	 */
	public int size() {
		return textLines.size();
	}
	
	/** Converts a lineIdx into the original line-index from the editor. */
	public int orgLineNr(int lineIdx) {
		return textLines.get(lineIdx).y();
	}
	
	// ITERATORS
	
	/** Iterates over all {@link ProgramLine}s. */
	public Iterable<ProgramLine> lineIter() {
		return () -> programLines.iterator();
	}
	
	// OVERRIDDEN
	
	@Override
	public SourcePath path() {
		return path;
	}
	
	@Override
	public String toString() {
		return "[File: " + path.toString() + "]";
	}
}
