package de.berlios.rcpviewer.compilemodel.standard;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.astview.EditorUtility;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.ITextEditor;

public class AstFacade {

//	private CompilationUnit createAST(IOpenable input, int astLevel) throws JavaModelException, CoreException {
//		long startTime;
//		long endTime;
//		CompilationUnit root;
//		boolean useReconciler = false; //input instanceof ICompilationUnit && fDoUseReconciler;
//		
//		if (useReconciler) {
//			ICompilationUnit wc= ((ICompilationUnit) input).getWorkingCopy(
//					new WorkingCopyOwner() {/*useless subclass*/},
//					new IProblemRequestor() { //TODO: strange: don't get bindings when supplying null as problemRequestor
//						public void acceptProblem(IProblem problem) {/*not interested*/}
//						public void beginReporting() {/*not interested*/}
//						public void endReporting() {/*not interested*/}
//						public boolean isActive() {
//							return true;
//						}
//					},
//					null);
//			try {
//				//make inconsistent (otherwise, no AST is generated):
////				IBuffer buffer= wc.getBuffer();
////				buffer.append(new char[] {' '});
////				buffer.replace(buffer.getLength() - 1, 1, new char[0]);
//				
//				startTime= System.currentTimeMillis();
//				root= wc.reconcile(getCurrentASTLevel(), true, null, null);
//				endTime= System.currentTimeMillis();
//			} finally {
//				wc.discardWorkingCopy();
//			}
//		} else {
//			boolean fCreateBindings = true; // hacking
//			
//			ASTParser parser= ASTParser.newParser(astLevel);
//			parser.setResolveBindings(fCreateBindings);
//			if (input instanceof ICompilationUnit) {
//				parser.setSource((ICompilationUnit) input);
//			} else {
//				parser.setSource((IClassFile) input);
//			}
//			startTime= System.currentTimeMillis();
//			root= (CompilationUnit) parser.createAST(null);
//			endTime= System.currentTimeMillis();
//		}
//		if (root == null) {
//			//throw new CoreException(getErrorStatus("Could not create AST", null)); //$NON-NLS-1$
//			throw new CoreException(null); //$NON-NLS-1$
//		}
////		updateContentDescription((IJavaElement) input, root, endTime - startTime, useReconciler);
//		return root;
//	}

//	private ITextEditor fEditor;
//	public void setInput(ITextEditor editor) throws CoreException {
////		if (fEditor != null) {
////			uninstallModificationListener();
////		}
//		
//		fEditor= null;
//		fRoot= null;
//		
//		if (editor != null) {
//			IOpenable openable= EditorUtility.getJavaInput(editor);
//			if (openable == null) {
//				throw new CoreException(getErrorStatus("Editor not showing a CU or classfile", null)); //$NON-NLS-1$
//			}
//			fOpenable= openable;
//			int astLevel= getInitialASTLevel((IJavaElement) openable);
//			
//			ISelection selection= editor.getSelectionProvider().getSelection();
//			if (selection instanceof ITextSelection) {
//				ITextSelection textSelection= (ITextSelection) selection;
//				fRoot= internalSetInput(openable, textSelection.getOffset(), textSelection.getLength(), astLevel);
//				fEditor= editor;
//				setASTLevel(astLevel, false);
//			}
////			installModificationListener();
//		}
//
//	}
	


}
