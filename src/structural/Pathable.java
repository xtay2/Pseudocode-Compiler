package structural;

import java.nio.file.*;

public sealed interface Pathable permits SourcePath, SourceFile {
	
	SourcePath path();

	default Path absPath() {
		return path().absPath();
	}

	default String fileName() {
		return path().fileName();
	}

}
