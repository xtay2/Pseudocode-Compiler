package modules.codegeneration;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import misc.helper.*;
import misc.helper.files.*;
import snippets.expressions.abstractions.*;
import snippets.expressions.blueprints.*;
import snippets.expressions.declarations.*;
import structural.*;

public class FileGenerator {
	
	private static SourceFile source;

	private static final String C_PATH = "C:/Users/x-tay/git/Pseudocode-Compiler/compiledFiles/";
	private static final Path HEADER_PATH = Path.of(C_PATH + "headers");
	private static final Path CONTENT_PATH = Path.of(C_PATH + "classes");

	static {
		try {
			FileManager.clear(HEADER_PATH);
			FileManager.clear(CONTENT_PATH);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	public static void generate(SourceFile source) {
		FileGenerator.source = source;
		try {
			// TODO Check for empty source-file
			generateHeader();
			generateSource();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	private static void generateHeader() throws IOException {
		Path p = Path.of(HEADER_PATH + "/" + source.fileName() + ".h");
		String guardName = source.getBlueprint().nameAsString().toUpperCase().replace(' ', '_') + "_H";
		List<String> headerOpenGuard = List.of("#ifndef " + guardName, "#define " + guardName + "\n");
		List<String> headerCloseGuard = List.of("#endif");
		Files.createFile(p);
		Files.write(p, CollectionHelper.merge(headerOpenGuard, generateImports(), generateBlueprint(), headerCloseGuard));
	}
	
	/** Adds all imports to the top of a headerfile. */
	private static List<String> generateImports() {
		ArrayList<String> content = new ArrayList<>();
		for (String bpName : source.importNames())
			content.add("#include \"" + bpName + ".h\"");
		content.add("");
		return content;
	}
	
	/** Turns all attributes into a typedef struct. */
	private static List<String> generateBlueprint() {
		ArrayList<String> content = new ArrayList<>();
		Blueprint bp = source.getBlueprint();
		String typename = bp.nameAsString();
		content.add("typedef struct " + typename + " {");
		for (VarDeclaration decl : bp.attributes)
			content.add("\t" + decl.typeName.nameAsString() + "* " + decl.varName.nameAsString() + ";");
		content.add("} " + typename + ";\n");
		return content;
	}

	private static void generateSource() throws IOException {
		Path p = Path.of(CONTENT_PATH + "/" + source.fileName() + ".c");
		List<String> importHeader = List.of("#include \"" + source.fileName() + ".h\"");
		List<String> functions = new ArrayList<>();
		Blueprint bp = source.getBlueprint();
		for (Function func : bp.functions)
			functions.add("\n" + func.generateCode());
		Files.createFile(p);
		Files.write(p, CollectionHelper.merge(importHeader, functions));
	}
}
