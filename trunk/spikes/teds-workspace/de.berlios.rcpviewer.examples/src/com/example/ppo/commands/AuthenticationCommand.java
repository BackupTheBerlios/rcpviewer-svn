package com.example.ppo.commands;

import org.eclipse.core.runtime.CoreException;

import net.sf.acegisecurity.Authentication;

public interface AuthenticationCommand {
	public Authentication run() throws CoreException;
}
