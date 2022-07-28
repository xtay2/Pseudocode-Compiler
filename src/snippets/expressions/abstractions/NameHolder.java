package snippets.expressions.abstractions;

import snippets.expressions.names.*;

public interface NameHolder {

	/** Returns the name-object of this {@link NameHolder}. */
	Name name();

	/**
	 * Returns a literal {@link String} representation of the {@link Name}. This has to be overridden in
	 * {@link Name}.
	 */
	default String nameAsString() {
		return name().nameAsString();
	}
}
