package misc.helper;

import java.util.*;
import java.util.stream.*;

/**
 * A (static) helper class with methods for arrays and {@link Collection}s.
 */
public final class CollectionHelper {
	
	/** Returns true if the given array (non-null) contains the given element. */
	@SafeVarargs
	public static <T> boolean containsAll(T[] array, T... elem) {
		for (T e : elem)
			if (!contains(array, e))
				return false;
		return true;
	}
	
	/** Returns true if the given array (non-null) contains the given element. */
	@SafeVarargs
	public static <T> boolean containsAny(T[] array, T... elem) {
		return Arrays.stream(elem).anyMatch(e -> contains(array, e));
	}
	
	/** Returns true if the given array (non-null) contains the given element. */
	public static <T> boolean contains(T[] array, T elem) {
		return indexOf(array, elem) != null;
	}
	
	/**
	 * Returns the index of the given element in the given array (non-null), or null if the array
	 * doesn't contain the element.
	 *
	 * @return an {@link Integer}, so dealing with a not-found-case is assured.
	 */
	public static <T> Integer indexOf(T[] array, T elem) {
		Objects.requireNonNull(array);
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(elem))
				return i;
		return null;
	}
	
	/** Merges multiple lists into one. */
	@SafeVarargs
	public static <T> List<T> merge(List<T>... lists) {
		return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
	}

}
