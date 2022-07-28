package misc.helper.util;

/**
 * A container for a variable that can get set, and changed exactly once.
 *
 * If this is used as a attribute it should be made final to avoid being replaced.
 */
public class OneTimeConstant<T> {

	private boolean changed = false;
	private T value;

	/** Initialise the value. */
	public OneTimeConstant(T value) {
		this.value = value;
	}
	
	public T get() {
		return value;
	}
	
	/** This method can get called only once. */
	public synchronized void set(T value) {
		if (changed)
			throw new AssertionError("This value was already changed once.");
		this.value = value;
		changed = true;
	}
}