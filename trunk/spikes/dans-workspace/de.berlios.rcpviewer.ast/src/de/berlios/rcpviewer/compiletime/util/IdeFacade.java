package de.berlios.rcpviewer.compiletime.util;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public final class IdeFacade {

	public IFile loadFileFor(final String projectName,
			final String sourceFolderName, final String packageAsPath,
			final String fileName) {
		IFile file;

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getProject(projectName);

		// IPath projectLocation = project.getRawLocation();
		// File projectLocationDir = projectLocation.toFile();
		if (!project.exists()) {
			throw new IllegalArgumentException("No such project");
		}
		if (!project.isOpen()) {
			try {
				project.open(null);
			} catch (CoreException ex) {
				throw new IllegalArgumentException("Could not open project", ex);
			}
		}

		IFolder folder = project.getFolder(sourceFolderName + "/"
				+ packageAsPath);
		if (!folder.exists()) {
			throw new IllegalArgumentException("No such folder");
		}

		file = folder.getFile(fileName);
		// File ioFile = file.getRawLocation().toFile();
		if (!file.exists()) {
			throw new IllegalArgumentException("No such file");
		}
		return file;
	}

	public ICompilationUnit loadCompilationUnitFor(final String projectName,
			final String sourceFolderName, final String packageAsPath,
			final String fileName) {
		
		IFile ifile = 
			loadFileFor(projectName, sourceFolderName, packageAsPath, fileName);

		ICompilationUnit compilationUnit = JavaCore.createCompilationUnitFrom(ifile);

		return compilationUnit;
	}
	
	/**
	 * Java AST TypeDeclaration.
	 * 
	 * @param projectName
	 * @param sourceFolderName
	 * @param packageAsPath
	 * @param fileName
	 * @return
	 */
	public TypeDeclaration loadTypeDeclarationFor(final String projectName,
			final String sourceFolderName, final String packageAsPath,
			final String fileName) {
		
		ICompilationUnit compilationUnit =
			loadCompilationUnitFor(projectName, sourceFolderName, packageAsPath, fileName);
		
		IType type = compilationUnit.findPrimaryType();
		try {
//			// 1. fields
//			IField[] fields = type.getFields();
//			for (int i = 0; i < fields.length; i++) {
//				 System.out.println(fields[i].getElementName() + ":" +
//				 fields[i].getElementType() +
//				 ":" + fields[i].getTypeSignature());
//			}
//			// fields[0].delete(true, null);
//			// 2. methods
//			IMethod[] methods = type.getMethods();
//			if (methods != null) {
//				IMethod method = methods[0];
//				// System.out.println(method);
//				IJavaElement[] bodys = method.getChildren();
//				// System.out.println(bodys.length);
//				for (int i = 0; i < bodys.length; i++) {
//					// System.out.println(bodys.getElementName() + ":" +
//					// fields.getElementType());
//				}
//			}

			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(compilationUnit);
			CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
			List types = astRoot.types();
			TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//			MethodDeclaration methodDec[] = typeDec.getMethods();
			return typeDeclaration;
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}

	}

	// public static void openInActiveEditor(final String projectName,
	// final String folder, final String fileName) {
	// // AstPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().
	//
	// // PlatformUI.getWorkbench()
	// // .getActiveWorkbenchWindow()
	// // .getActivePage()
	// // .openEditor( new FileEditorInput(file), null);
	//
	// }

}
