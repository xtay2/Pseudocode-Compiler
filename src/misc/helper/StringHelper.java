package misc.helper;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class StringHelper {
	
	/**
	 * The symbol with which lines get underlined. This has to be a String to support the
	 * {@link String#repeat(int)} method.
	 */
	private static final String POINT = "^";

	/**
	 * Points to a char in a string.
	 *
	 * <pre>
	 * pointUnderline("Hello", 2) produces:
	 * Hello
	 *   ^
	 * </pre>
	 *
	 * @param line is the full line
	 * @param pointer is the index of the char that gets pointed at.
	 */
	public static String pointUnderline(String line, int pointer) {
		return pointUnderline(line, pointer, 1);
	}
	
	/**
	 * Points to a section in a string.
	 *
	 * <pre>
	 * pointUnderline("Hello", 2, 2) produces:
	 * Hello
	 *   ^^
	 * </pre>
	 *
	 * @param line is the full line
	 * @param pointer is the index of the char that gets pointed at.
	 * @param pointerLength is the length of the pointed section.
	 */
	public static String pointUnderline(String line, int pointer, int pointerLength) {
		int leadingTabs = 0;
		for (int i = 0; i < line.length(); i++)
			if (line.charAt(i) == '\t')
				leadingTabs++;
			else
				break;
		return line.substring(leadingTabs) + "\n" + " ".repeat(pointer - leadingTabs) + POINT.repeat(pointerLength);
	}
	
	/**
	 * Underlines all runnable matches of the passed regex in the line.
	 *
	 * @param line
	 * @param regex
	 * @return
	 */
	public static String pointUnderlineRunnables(String line, String regex) {
		List<MatchResult> matches = Pattern.compile(regex).matcher(line).results() //
				.filter(mRes -> ProgramHelper.isRunnableCode(mRes.start(), line)) //
				.toList();
		String underline = "";
		for (MatchResult m : matches)
			underline += " ".repeat(m.start() - underline.length()) + POINT.repeat(m.end() - m.start());
		return line.strip() + "\n" + underline;
	}

	/**
	 * Lists all entries, seperated by linebreaks.
	 */
	public static String enumerate(Collection<?> content) {
		if (content.isEmpty())
			return "<none>";
		return content.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
	
	/**
	 * Lists all entries, seperated by linebreaks, with a - in front.
	 */
	public static String enumerateLines(Collection<?> content) {
		if (content.isEmpty())
			return "<none>";
		return content.stream().map(l -> "-" + l.toString()).collect(Collectors.joining("\n"));
	}
	
	/** Removes the leading chars from the string. */
	public static String stripLeading(char c, String string) {
		for (int i = 0; i < string.length(); i++)
			if (string.charAt(i) != c)
				return string.substring(i);
		return string;
	}
}
