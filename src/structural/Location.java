package structural;

import java.util.*;

import misc.*;

public enum Location {
	
	SRC("src"), STD_LIB("stdlib"), USR_LIB("usrlib");
	
	private final String txt;
	
	Location(String txt) {
		this.txt = txt;
	}
	
	/** Returns the absolute path of this {@link Location}. */
	String absPath() {
		return switch (this) {
			case SRC -> Main.get().launchPath;
			case STD_LIB -> Main.get().stdlibPath;
			case USR_LIB -> Main.get().usrlibPath;
		};
	}
	
	/** Translates the prefix of an import into a {@link Location}-Object. */
	static Location fromString(String input) {
		for (Location l : values())
			if (l.toString().equals(input))
				return l;
		throw new NoSuchElementException("There is no location called" + input);
	}
	
	@Override
	public String toString() {
		return txt;
	}
}
