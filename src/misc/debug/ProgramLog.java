package misc.debug;

import java.util.*;
import java.util.stream.*;

import snippets.tokens.abstractions.*;
import structural.*;

public class ProgramLog {
	
	public static String log(Object input) {
		System.out.println(input);
		return input.toString();
	}

	public static String logExpressions(SourceFile file) {
		StringBuilder sb = new StringBuilder();
		for (ProgramLine line : file.lineIter())
			sb.append(log(line.mainExp));
		return sb.toString();
	}
	
	public static String logTokens(SourceFile file) {
		StringBuilder sb = new StringBuilder();
		for (ProgramLine line : file.lineIter())
			sb.append(logTokens(line));
		return sb.toString();
	}
	
	public static String logTokens(ProgramLine line) {
		return logTokens(line.tokens.stream().map(w -> w.token).toList());
	}
	
	public static String logExpectedTokens(SourceFile file) {
		return logTokens(List.of(file.expectedTokens()));
	}
	
	public static String logTokens(List<Token> tokens) {
		return log(tokens.stream().map(Token::toString).collect(Collectors.joining("] [", "[", "]")) + "\n");
	}
	
}
