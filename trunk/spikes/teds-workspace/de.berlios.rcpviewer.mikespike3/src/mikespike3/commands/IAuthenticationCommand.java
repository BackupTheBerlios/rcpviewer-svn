package mikespike3.commands;

import org.eclipse.core.runtime.CoreException;

import net.sf.acegisecurity.Authentication;

public interface IAuthenticationCommand {
	public Authentication run() throws CoreException;
}
