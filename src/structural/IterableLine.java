package structural;

import java.util.*;

import misc.helper.*;
import misc.helper.util.*;
import throwables.*;

public class IterableLine implements Iterable<Character>, Iterator<Character> {
	
	private final String rawLine;
	private final List<Tuple<Integer, String>> words = new ArrayList<>();
	private int wordPos = 0, charPos = 0;

	/**
	 * Creates a new {@link IterableLine} and splits it into words.
	 */
	public IterableLine(String line) {
		rawLine = line;
		String wordpart = "";
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ' ') {
				words.add(new Tuple<>(i - wordpart.length(), wordpart));
				wordpart = "";
			} else
				wordpart += c;
		}
		words.add(new Tuple<>(line.length() - wordpart.length(), wordpart));
	}
	
	@Override
	public boolean hasNext() {
		return wordPos < words.size();
	}

	@Override
	public Character next() {
		Character c = peek();
		if (c == null)
			throwNewLineError();
		charPos++;
		return c;
	}
	
	/**
	 * Returns the next Character in the current word without advancing the iterator. Null gets returned
	 * when there is no next character in the word.
	 */
	public Character peek() {
		String w = words.get(wordPos).y();
		return w.length() > charPos ? w.charAt(charPos) : null;
	}
	
	/**
	 * Returns the whole current word without advancing the iterator.
	 */
	public String peekWord() {
		return words.get(wordPos).y();
	}
	
	/** Returns the current charPos in the raw line. */
	public int posInLine() {
		return words.get(wordPos).x() + charPos;
	}
	
	/**
	 * This has to get called after a match to advance to the next word.
	 *
	 * @param length is the length of the match.
	 */
	public void foundMatch(int length) {
		int currWordL = words.get(wordPos).y().length();
		if (currWordL == length) {
			wordPos++;
			charPos = 0;
			return;
		}
		if (currWordL < length) {
			charPos += length;
			return;
		}
		throwNewLineError();
	}
	
	@Override
	public Iterator<Character> iterator() {
		return this;
	}
	
	@Override
	public String toString() {
		return words.toString();
	}
	
	private final void throwNewLineError() throws UnresolvedCompilerError {
		Tuple<Integer, String> word = words.get(wordPos);
		throw new UnresolvedCompilerError("This line doesn't match any expected pattern:\n" //
				+ StringHelper.pointUnderline(rawLine, word.x(), word.y().length()));
	}
}
