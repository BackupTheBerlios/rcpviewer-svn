package org.essentialplatform.runtime.shared.remoting.packaging;

public class PackagingException extends RuntimeException {

	public PackagingException() {
		super();
	}

	public PackagingException(String message) {
		super(message);
	}

	public PackagingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackagingException(Throwable cause) {
		super(cause);
	}

}
