package de.chw.util;

public class StacklessException extends RuntimeException {

	private static final long serialVersionUID = -9040980547278981254L;

	@Override
	public synchronized Throwable fillInStackTrace() { // greatly improves performance when constructing
		return this;
	}

}
