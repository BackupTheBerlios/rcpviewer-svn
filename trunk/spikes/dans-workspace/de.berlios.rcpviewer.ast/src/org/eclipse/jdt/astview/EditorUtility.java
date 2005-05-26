/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.astview;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;

import de.berlios.rcpviewer.ast.AstPlugin;

/**
 *
 */
public class EditorUtility {
	private EditorUtility() {
		super();
	}

	public static IEditorPart getActiveEditor() {
		IWorkbenchWindow window = AstPlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				return page.getActiveEditor();
			}
		}
		return null;
	}

	public static IOpenable getJavaInput(IEditorPart part) {
		IEditorInput editorInput = part.getEditorInput();
		if (editorInput != null) {
			IJavaElement input = (IJavaElement) editorInput
					.getAdapter(IJavaElement.class);
			if (input instanceof IOpenable) {
				return (IOpenable) input;
			}
		}
		return null;
	}

	public static void selectInEditor(ITextEditor editor, int offset, int length) {
		IEditorPart active = getActiveEditor();
		if (active != editor) {
			editor.getSite().getPage().activate(editor);
		}

		editor.selectAndReveal(offset, length);
	}
}
