package misc.helper;

import static misc.helper.StringHelper.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public final class ProgramHelper {
	
	public static final char SLC = '#';
	
	private ProgramHelper() {
		// Dead constructor
	}
	
	/**
	 * Tells, if a char at a specified index is in an executable area.
	 *
	 * @return true if the index is runnable and false if its either out of bounds, in a string or in a
	 * comment.
	 */
	public static boolean isRunnableCode(int index, String line) {
		return isNotInComment(index, line) && isNotInString(index, line);
	}
	
	/**
	 * Tells, if a char at a specified index is not in the string and char boundaries. If index points
	 * into a comment, true gets returned.
	 *
	 * <pre>
	 * The symbols ' and " themselves are considered "in a string". (Returns false)
	 * If the index is out of bounds for line, its also considered as "in a string",
	 * because that means "don't execute this".
	 * </pre>
	 *
	 * @return true if the index is not in a string.
	 */
	public static boolean isNotInString(int index, String line) {
		if (index < 0 && index >= line.length())
			return false;
		boolean inString = false;
		for (int i = 0; i < index; i++)
			if (inString && line.charAt(i) == '\\')
				i++;
			else if (line.charAt(i) == '"')
				inString = !inString;
			else if (!inString && SLC == line.charAt(i))
				break;
		return !inString && isNotAChar(index, line);
	}
	
	/**
	 * Tells, if a char at a specified index is not directly enclosed in char quotes.
	 *
	 * <pre>
	 * The symbols ' itself is considered "in a char". (Returns false)
	 * If the index is out of bounds for line, its also considered as "in a char",
	 * because that means "don't execute this".
	 * </pre>
	 *
	 * @return true if the index is not in a string.
	 */
	public static boolean isNotAChar(int index, String line) {
		if (index < 0 || index >= line.length())
			return false;
		if (index - 1 >= 0 && index + 1 < line.length())
			return line.charAt(index) != '\'' && !line.substring(index - 1, index + 2).matches("'.'");
		return line.charAt(index) != '\'';
	}
	
	/**
	 * Tells, if a char at a specified index is not in a comment.
	 *
	 * <pre>
	 * The symbol # itself is considered "in a comment". (Returns false)
	 * If the index is out of bounds for line, its also considered as "in a comment",
	 * because that means "don't execute this".
	 * </pre>
	 *
	 * @return true if the index is not in a comment.
	 */
	private static boolean isNotInComment(int index, String line) {
		int idxOfSLC = indexOfSLC(line);
		return (idxOfSLC == -1 || index < idxOfSLC) && index >= 0 && index < line.length();
	}
	
	/**
	 * Replaces all matches of the regex in the line with the replacement, if they are runnable code.
	 *
	 * @param line is the input line that gets tested
	 * @param regex is the pattern
	 * @param replacement is the replacement of the matches
	 * @param isFullyRunnable if the line was tested as fully runnable, the
	 * {@link String#replaceAll(String, String)} method gets chosen instead.
	 * @return the formatted string
	 */
	public static String replaceAllIfRunnable(String line, String regex, String replacement, boolean isFullyRunnable) {
		if (isFullyRunnable)
			return line.replaceAll(regex, replacement);
		Matcher m = Pattern.compile(regex).matcher(line);
		final String unedited = line;
		// Filter out all matches that aren't runnable
		List<MatchResult> matches = m.results().filter(r -> isRunnableCode(r.start(), unedited)).collect(Collectors.toList());
		// Replace all matches, back to front
		Collections.reverse(matches);
		for (MatchResult match : matches)
			line = line.substring(0, match.start()) + replacement + line.substring(match.end());
		return line;
	}
	
	/**
	 * Works like line += suffix, but if the line ends with a {@link Formatter#SLC}, the last spaces get
	 * stripped as well.
	 *
	 * <pre>
	 * line: burg #Comment
	 * suffix: er
	 * result: burger #Comment
	 * </pre>
	 */
	public static String appendRunnable(String line, String suffix) {
		int idxOfEnd = indexOfSLC(line);
		if (idxOfEnd == -1)
			return line + suffix;
		return line.substring(0, idxOfEnd).stripTrailing() + suffix + " " + line.substring(idxOfEnd);
	}
	
	/**
	 * This function detects a {@link Formatter#SLC} and safely removes it from a line.
	 */
	public static String lineWithoutSLC(String line) {
		int idxOfSLC = indexOfSLC(line);
		return idxOfSLC == -1 ? line : line.substring(0, idxOfSLC).stripTrailing();
	}

	/** Returns the index of a single-line-comment, or -1 if there is none in this line. */
	public static int indexOfSLC(String line) {
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == SLC && isNotInString(i, line))
				return i;
		}
		return -1;
	}
	
	/**
	 * Returns the index of the matching bracket in the same line.
	 *
	 * @param fstIdx is the index of the first bracket.
	 * @param line is the whole line.
	 * @param isFullyRunnable should be true if the line contains no literal strings or comments.
	 * Default: false
	 * @return the index of the matching bracket or -1 if none was found.
	 *
	 * @throws AssertionError if the first index doesn't point to a bracket.
	 */
	public static int findMatchingBrackInLine(int fstIdx, String line, boolean isFullyRunnable) {
		char opened = line.charAt(fstIdx);
		char closed = switch (line.charAt(fstIdx)) {
			case '(' -> ')';
			case '[' -> ']';
			case '{' -> '}';
			default -> throw new AssertionError("Expected a bracket at index " + fstIdx + " got : \"" + line.charAt(fstIdx)
					+ "\" instead:\n" + line + "\n" + " ".repeat(fstIdx) + "^");
		};
		int brack = 1;
		for (int i = fstIdx + 1; i < line.length(); i++)
			if (line.charAt(i) == opened && (isFullyRunnable || isRunnableCode(i, line)))
				brack++;
			else if (line.charAt(i) == closed && (isFullyRunnable || isRunnableCode(i, line)))
				if (--brack == 0)
					return i;
		return -1;
	}
	
	/**
	 * This function tells, if there is any match of the regex in the line, that is also runnable.
	 *
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return true if the line contains that runnable expression.
	 */
	public static boolean containsRunnable(String line, String regex) {
		return firstRunnableMatch(line, regex) != null;
	}
	
	/**
	 * Works like {@link String#endsWith(String)}, but if there is comment at the end of the line, it
	 * gets ignored.
	 *
	 * <pre>
	 * false for suffix "burger" in line:
	 * I like #burger
	 *
	 * true for suffix "like" in line:
	 * I like #burger
	 * </pre>
	 *
	 * @param line is the whole line.
	 * @param suffix is the part that the end of the line gets checked against.
	 */
	public static boolean lineEndsWith(String line, String suffix) {
		int idxOfSLC = indexOfSLC(line);
		if (idxOfSLC == -1)
			return line.endsWith(suffix);
		return line.substring(0, idxOfSLC).stripTrailing().endsWith(suffix);
	}
	
	/**
	 * This function returns the first index of a match of the regex, in the line, that is also
	 * runnable.
	 *
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return the index of the start of the match.
	 */
	public static int indexOfRunnable(String line, String regex) {
		MatchResult m = firstRunnableMatch(line, regex);
		return m == null ? -1 : m.start();
	}
	
	/**
	 * This function returns the first runnable {@link String}-match of the regex, in the line. If the
	 * match contains a string, or ends in a comment it still counts as runnable.
	 *
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return the first match.
	 */
	public static String getFirstRunnable(String line, String regex) {
		MatchResult m = firstRunnableMatch(line, regex);
		return m == null ? null : m.group();
	}
	
	private static MatchResult firstRunnableMatch(String line, String regex) {
		return Pattern.compile(regex).matcher(line).results() //
				.filter(mRes -> isRunnableCode(mRes.start(), line)) //
				.findFirst().orElse(null);
	}
	
	/**
	 * This function returns an immutable {@link List} of all runnable {@link String}-matches of the
	 * regex, in the line. If the match contains a string, or ends in a comment it still counts as
	 * runnable.
	 *
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return the first match.
	 */
	public static Set<String> getAllRunnable(String line, String regex) {
		Pattern p = Pattern.compile(regex);
		return p.matcher(line).results().filter(mRes -> isRunnableCode(mRes.start(), line)).map(MatchResult::group)
				.collect(Collectors.toSet());
	}
	
	/**
	 * This function returns number of runnable occurences of the regex, in the line.
	 *
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return the number of runnable matches.
	 */
	public static int runnableMatches(String line, String regex) {
		return getAllRunnable(line, regex).size();
	}
	
	/**
	 * Underlines the first runnable match of a regex.
	 *
	 * @see StringHelper#pointUnderline(String, int, int)
	 * @param line is the whole line.
	 * @param regex is the regular expression that gets matched.
	 * @return the possibly underlined line.
	 */
	public static String underlineFirstRunnable(String line, String regex) {
		MatchResult m = firstRunnableMatch(line, regex);
		if (m == null)
			return line;
		return pointUnderline(line, m.start(), m.end() - m.start());
	}
	
	/** Returns true if the line doesn't contain comments or strings. */
	public static boolean isFullyRunnable(String line) {
		for (int i = 0; i < line.length() - 1; i++) {
			char c = line.charAt(i);
			if (SLC == c || '"' == c)
				return false;
		}
		return true;
	}
}
