package modules.assembler;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import misc.helper.*;
import misc.helper.util.*;
import snippets.tokens.*;
import snippets.tokens.abstractions.*;

public class Assembler {
	
	/** The org-line-id for a generated line. */
	public static final int GENERATED = -1;

	public static List<Tuple<String, Integer>> assemble(List<String> unIndexed) {
		List<Tuple<String, Integer>> lines = IntStream.range(0, unIndexed.size()) //
				.mapToObj(idx -> new Tuple<>(unIndexed.get(idx), idx + 1)) //
				.collect(Collectors.toList());
		removeNonRunnable(lines);
		expandBlueprintSign(lines);
		System.out.println(StringHelper.enumerate(lines));
		System.out.println("-".repeat(30));
		return lines;
	}
	
	/** Removes all leading and trailing whitespaces, all empty lines and all comments. */
	private static void removeNonRunnable(List<Tuple<String, Integer>> lines) {
		for (int i = 0; i < lines.size();) {
			Tuple<String, Integer> t = lines.remove(i);
			String l = ProgramHelper.lineWithoutSLC(t.x()).strip();
			if (l.isBlank())
				continue;
			lines.add(i, new Tuple<>(l, t.y()));
			i++;
		}
	}
	
	/** Replaces the blueprint-signature-symbol with block brackets. */
	private static void expandBlueprintSign(List<Tuple<String, Integer>> lines) {
		Pattern p = Pattern.compile(".*(" + LiteralMatchToken.anyRegex(BlueprintToken.class) + ").*");
		for (int i = 0; i < lines.size(); i++) {
			Tuple<String, Integer> t = lines.get(i);
			if (p.matcher(t.x()).matches()) {
				if (t.x().endsWith(":")) {
					String rep = ProgramHelper.replaceAllIfRunnable(t.x(), ":", " " + BlockBracketToken.OPEN_BLOCK.literal(), false);
					lines.set(i, new Tuple<>(rep, t.y()));
					lines.add(new Tuple<>(BlockBracketToken.CLOSE_BLOCK.literal(), GENERATED));
				}
				return;
			}
		}
	}
	
}
