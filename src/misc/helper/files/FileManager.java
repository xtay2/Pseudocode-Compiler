package misc.helper.files;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import misc.helper.*;
import modules.assembler.*;
import modules.preformatter.*;
import structural.*;

public class FileManager {
	
	public static SourceFile generate(SourcePath path) {
		try {
			Preformatter.format(path);
			List<String> unIndexed = Files.readAllLines(path.absPath());
			return new SourceFile(path, Assembler.assemble(unIndexed));
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	/**
	 * Tries to find a File, when a bit of the path is missing.
	 *
	 * <pre>
	 * subfolder: "C:\Users\Pseudocode\MyProject"
	 * target: "Main.pc"
	 * returns: "C:\Users\Pseudocode\MyProject\package\subPackage\Main.pc"
	 * </pre>
	 *
	 * @param subfolder is the starting point of the search. This has to be an absolute Path.
	 * @param target is the name of the target File or Folder.
	 * @return the whole {@link Path}
	 * @throws IOException when multiple, or no {@link File}s were found.
	 */
	public static Path findPath(String subfolder, String target) throws IOException {
		List<Path> paths;
		try {
			paths = Files.walk(Path.of(subfolder)).filter(e -> e.endsWith(target)).toList();
		} catch (IOException e) {
			throw (FileNotFoundException) new FileNotFoundException( //
					"Couldn't find file \"" + subfolder + ".." + target + "\""//
			).initCause(e);
		}
		if (paths.size() == 1)
			return Path.of(paths.get(0).toString().replace('\\', '/') + SourceFile.EXTENSION);
		if (paths.isEmpty())
			throw new FileNotFoundException("Couldn't find any file at the search-path: \n\"" + subfolder + ".." + target + "\"");
		throw new IOException("There are multiple matches for the search-path: \n\"" + subfolder + ".." + target + "\"" //
				+ "\nMatches:" + StringHelper.enumerate(paths));
	}
	
	public static void clear(Path path) throws IOException {
		if (Files.exists(path))
			Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		Files.createDirectory(path);
	}
}
