package modules.preformatter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import structural.*;

public class Preformatter {
	
	static List<String> program;
	static SourcePath path;

	public static void format(SourcePath path) throws IOException {
		Preformatter.path = path;
		program = Files.readAllLines(path.absPath());
		FormattingPreChecks.check();
	}
}
