package org.essentialplatform.louis.authentication;

import org.eclipse.core.runtime.CoreException;

import net.sf.acegisecurity.Authentication;

public interface IAuthenticationCommand {
	public Authentication run() throws CoreException;
}
