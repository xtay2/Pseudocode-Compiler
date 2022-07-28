package structural;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import misc.helper.*;
import misc.helper.files.*;

public final class SourcePath implements Pathable {

	private final String rawPath;
	private final Path path;
	private final Location location;
	
	/**
	 * Creates a {@link SourcePath} from a textual raw path. For example:
	 * <pre>
	 * src..Main
	 * stdlib.lang.System
	 * usrlib.textfunctions.ListHelper
	 * </pre>
	 *
	 * @param path is this textual raw path
	 * @throws IOException if there is no matching path, or multiple matches were found.
	 * @see FileManager#findPath(String, String)
	 */
	public SourcePath(String path) throws IOException {
		path = path.strip();
		if (!path.matches("([a-z]+\\.)+\\.?([a-z]+\\.)*([A-Z][a-z]+)+"))
			throw new InvalidPathException(path, "The entered path has an invalid format:");
		rawPath = path;
		String prefix = path.substring(0, path.indexOf('.'));
		location = Location.fromString(prefix);
		path = StringHelper.stripLeading('.', path.substring(location.toString().length()));
		this.path = srcToAbs(location, path);
	}
	
	/**
	 * Turns a textual source-path to an absolute java-{@link Path}.
	 *
	 * @param l is the {@link Location} of the path.
	 * @param srcPath is the string without the location. It may contain a ".." operator.
	 * @return the actual full {@link Path}.
	 * @throws IOException if there is no matching path, or multiple matches were found.
	 * @see FileManager#findPath(String, String)
	 */
	private static Path srcToAbs(Location l, String srcPath) throws IOException {
		int idxOfSearch = srcPath.indexOf("..");
		if (idxOfSearch != -1) {
			String startpoint = l.absPath() + srcPath.substring(0, idxOfSearch);
			String target = srcPath.substring(srcPath.lastIndexOf('.') + 1);
			return FileManager.findPath(startpoint, target + SourceFile.EXTENSION);
		}
		return Path.of(l.absPath() + "\\" + srcPath + SourceFile.EXTENSION);
	}

	@Override
	public SourcePath path() {
		return this;
	}
	
	@Override
	public Path absPath() {
		return path.toAbsolutePath();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SourcePath p && absPath().equals(p.absPath());
	}

	@Override
	public int hashCode() {
		return Objects.hash(absPath());
	}
	
	@Override
	public String fileName() {
		String fileNameWithExtension = path.getFileName().toString();
		int idxOfPt = fileNameWithExtension.indexOf('.');
		return idxOfPt == -1 ? fileNameWithExtension : fileNameWithExtension.substring(0, idxOfPt);
	}
	
	@Override
	public String toString() {
		return rawPath;
	}
}
