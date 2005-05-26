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

package org.eclipse.jdt.astview.views;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import org.eclipse.jdt.astview.ASTViewImages;
import org.eclipse.jdt.astview.ASTViewPlugin;
import org.eclipse.jdt.astview.EditorUtility;
import org.eclipse.jdt.astview.NodeFinder;
import org.eclipse.jdt.astview.TreeInfoCollector;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.ShowInPackageViewAction;

public class ASTView extends ViewPart implements IShowInSource {
	
	private class ASTViewSelectionProvider implements ISelectionProvider {
		ListenerList fListeners= new ListenerList();

		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			fListeners.add(listener);
		}

		public ISelection getSelection() {
			IStructuredSelection selection= (IStructuredSelection) fViewer.getSelection();
			ArrayList externalSelection= new ArrayList();
			for (Iterator iter= selection.iterator(); iter.hasNext();) {
				Object element= iter.next();
				if (element instanceof JavaElement)
					externalSelection.add(((JavaElement) element).getJavaElement());
				else {
					//TODO: support for other node types?
				}
			}
			return new StructuredSelection(externalSelection);
		}

		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			fListeners.remove(listener);
		}

		public void setSelection(ISelection selection) {
			//not supported
		}
	}

	private class ASTLevelToggle extends Action {
		private int fLevel;

		public ASTLevelToggle(String label, int level) {
			super(label, AS_RADIO_BUTTON);
			fLevel= level;
			if (level == getCurrentASTLevel()) {
				setChecked(true);
			}
		}
	
		public int getLevel() {
			return fLevel;
		}
		
		public void run() {
			setASTLevel(fLevel, true);
		}
	}
	
	private static class ListenerMix implements ISelectionListener, IFileBufferListener, IDocumentListener, ISelectionChangedListener, IDoubleClickListener, IPartListener2 {
		
		private boolean fASTViewVisible= true;
		private ASTView fView;
		
		public ListenerMix(ASTView view) {
			fView= view;
		}
		
		public void dispose() {
			fView= null;
		}

		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (fASTViewVisible) {
				fView.handleEditorPostSelectionChanged(part, selection);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferCreated(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void bufferCreated(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferDisposed(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void bufferDisposed(IFileBuffer buffer) {
			if (buffer instanceof ITextFileBuffer) {
				fView.handleDocumentDisposed(((ITextFileBuffer) buffer).getDocument());
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferContentAboutToBeReplaced(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferContentReplaced(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void bufferContentReplaced(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateChanging(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void stateChanging(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#dirtyStateChanged(org.eclipse.core.filebuffers.IFileBuffer, boolean)
		 */
		public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateValidationChanged(org.eclipse.core.filebuffers.IFileBuffer, boolean)
		 */
		public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#underlyingFileMoved(org.eclipse.core.filebuffers.IFileBuffer, org.eclipse.core.runtime.IPath)
		 */
		public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#underlyingFileDeleted(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void underlyingFileDeleted(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateChangeFailed(org.eclipse.core.filebuffers.IFileBuffer)
		 */
		public void stateChangeFailed(IFileBuffer buffer) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent)
		 */
		public void documentAboutToBeChanged(DocumentEvent event) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
		 */
		public void documentChanged(DocumentEvent event) {
			fView.handleDocumentChanged(event.getDocument());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fView.handleSelectionChanged(event.getSelection());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
		 */
		public void doubleClick(DoubleClickEvent event) {
			fView.handleDoubleClick(event);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partHidden(IWorkbenchPartReference partRef) {
			IWorkbenchPart part= partRef.getPart(false);
			if (part == fView) {
				fASTViewVisible= false;
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partVisible(IWorkbenchPartReference partRef) {
			IWorkbenchPart part= partRef.getPart(false);
			if (part == fView) {
				fASTViewVisible= true;
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partActivated(IWorkbenchPartReference partRef) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partClosed(IWorkbenchPartReference partRef) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partDeactivated(IWorkbenchPartReference partRef) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partOpened(IWorkbenchPartReference partRef) {
			// not interesting
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partInputChanged(IWorkbenchPartReference partRef) {
			// not interesting
		}
	}
		
	private final static String SETTINGS_LINK_WITH_EDITOR= "link_with_editor"; //$NON-NLS-1$
	private final static String SETTINGS_USE_RECONCILER= "use_reconciler"; //$NON-NLS-1$
	private final static String SETTINGS_NO_BINDINGS= "create_bindings"; //$NON-NLS-1$
	private final static String SETTINGS_JLS= "jls"; //$NON-NLS-1$

	
	private TreeViewer fViewer;
	private DrillDownAdapter fDrillDownAdapter;
	private Action fFocusAction;
	private Action fRefreshAction;
	private Action fUseReconcilerAction;
	private Action fCreateBindingsAction;
	private Action fCollapseAction;
	private Action fExpandAction;
	private Action fClearAction;
	private TreeCopyAction fCopyAction;
	private Action fDoubleClickAction;
	private Action fLinkWithEditor;
	
	private ASTLevelToggle[] fASTVersionToggleActions;
	private int fCurrentASTLevel;
	
	private ITextEditor fEditor;
	private IOpenable fOpenable;
	private CompilationUnit fRoot;
	private IDocument fCurrentDocument;
	
	private boolean fDoLinkWithEditor;
	private boolean fDoUseReconciler;
	private boolean fCreateBindings;
	private Object fPreviousDouble;
	
	private ListenerMix fSuperListener;

	private SashForm fSash;
	private TreeViewer fTray;
	private ArrayList fTrayRoots;
	private Action fAddToTrayAction;
	private ISelectionChangedListener fTrayUpdater;
	private Action fDeleteAction;
	private IDialogSettings fDialogSettings;

	
	public ASTView() {
		fSuperListener= null;
		fDialogSettings= ASTViewPlugin.getDefault().getDialogSettings();
		fDoLinkWithEditor= fDialogSettings.getBoolean(SETTINGS_LINK_WITH_EDITOR);
		fDoUseReconciler= fDialogSettings.getBoolean(SETTINGS_USE_RECONCILER);
		fCreateBindings= !fDialogSettings.getBoolean(SETTINGS_NO_BINDINGS); // inverse so that default is to create bindings
		fCurrentASTLevel= AST.JLS2;
		try {
			int level= fDialogSettings.getInt(SETTINGS_JLS);
			if (level == AST.JLS2 || level == AST.JLS3) {
				fCurrentASTLevel= level;
			}
		} catch (NumberFormatException e) {
			// ignore
		}
	}
	
	/*(non-Javadoc)
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
	 */
	public void init(IViewSite site) throws PartInitException {
		super.setSite(site);
		if (fSuperListener == null) {
			fSuperListener= new ListenerMix(this);
			
			ISelectionService service= site.getWorkbenchWindow().getSelectionService();
			service.addPostSelectionListener(fSuperListener);
			site.getPage().addPartListener(fSuperListener);
			FileBuffers.getTextFileBufferManager().addFileBufferListener(fSuperListener);
		}
	}
	
	public int getCurrentASTLevel() {
		return fCurrentASTLevel;
	}

	public void setInput(ITextEditor editor) throws CoreException {
		if (fEditor != null) {
			uninstallModificationListener();
		}
		
		fEditor= null;
		fRoot= null;
		
		if (editor != null) {
			IOpenable openable= EditorUtility.getJavaInput(editor);
			if (openable == null) {
				throw new CoreException(getErrorStatus("Editor not showing a CU or classfile", null)); //$NON-NLS-1$
			}
			fOpenable= openable;
			int astLevel= getInitialASTLevel((IJavaElement) openable);
			
			ISelection selection= editor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection= (ITextSelection) selection;
				fRoot= internalSetInput(openable, textSelection.getOffset(), textSelection.getLength(), astLevel);
				fEditor= editor;
				setASTLevel(astLevel, false);
			}
			installModificationListener();
		}

	}
	
	private int getInitialASTLevel(IJavaElement openable) {
		IJavaProject project= (IJavaProject) openable.getAncestor(IJavaElement.JAVA_PROJECT);
		if (JavaCore.VERSION_1_5.equals(project.getOption(JavaCore.COMPILER_SOURCE, true))) {
			return AST.JLS3;
		}
		return AST.JLS2;
	}

	private CompilationUnit internalSetInput(IOpenable input, int offset, int length, int astLevel) throws CoreException {
		if (input.getBuffer() == null) {
			throw new CoreException(getErrorStatus("Input has no buffer", null)); //$NON-NLS-1$
		}
		
		try {
			CompilationUnit root= createAST(input, astLevel);
			
			resetView(root);
			ASTNode node= NodeFinder.perform(root, offset, length);
			if (node != null) {
				fViewer.getTree().setRedraw(false);
				fViewer.setSelection(new StructuredSelection(node), true);
				fViewer.getTree().setRedraw(true);
			}
			return root;
			
		} catch (RuntimeException e) {
			throw new CoreException(getErrorStatus("Could not create AST:\n" + e.getMessage(), e)); //$NON-NLS-1$
		}
	}

	private void resetView(CompilationUnit root) {
		if (root == null)
			setContentDescription("Open a Java editor and press the 'Show AST of active editor' toolbar button"); //$NON-NLS-1$
		fViewer.setInput(root);
		fSash.setMaximizedControl(fViewer.getTree());
		fTrayRoots= new ArrayList();
		if (fTray != null)
			fTray.setInput(fTrayRoots);
		setASTUptoDate(root != null);
		fClearAction.setEnabled(root != null);
	}
	
	private CompilationUnit createAST(IOpenable input, int astLevel) throws JavaModelException, CoreException {
		long startTime;
		long endTime;
		CompilationUnit root;
		boolean useReconciler= input instanceof ICompilationUnit && fDoUseReconciler;
		
		if (useReconciler) {
			ICompilationUnit wc= ((ICompilationUnit) input).getWorkingCopy(
					new WorkingCopyOwner() {/*useless subclass*/},
					new IProblemRequestor() { //TODO: strange: don't get bindings when supplying null as problemRequestor
						public void acceptProblem(IProblem problem) {/*not interested*/}
						public void beginReporting() {/*not interested*/}
						public void endReporting() {/*not interested*/}
						public boolean isActive() {
							return true;
						}
					},
					null);
			try {
				//make inconsistent (otherwise, no AST is generated):
//				IBuffer buffer= wc.getBuffer();
//				buffer.append(new char[] {' '});
//				buffer.replace(buffer.getLength() - 1, 1, new char[0]);
				
				startTime= System.currentTimeMillis();
				root= wc.reconcile(getCurrentASTLevel(), true, null, null);
				endTime= System.currentTimeMillis();
			} finally {
				wc.discardWorkingCopy();
			}
		} else {
			ASTParser parser= ASTParser.newParser(astLevel);
			parser.setResolveBindings(fCreateBindings);
			if (input instanceof ICompilationUnit) {
				parser.setSource((ICompilationUnit) input);
			} else {
				parser.setSource((IClassFile) input);
			}
			startTime= System.currentTimeMillis();
			root= (CompilationUnit) parser.createAST(null);
			endTime= System.currentTimeMillis();
		}
		if (root == null) {
			throw new CoreException(getErrorStatus("Could not create AST", null)); //$NON-NLS-1$
		}
		updateContentDescription((IJavaElement) input, root, endTime - startTime, useReconciler);
		return root;
	}

	private void updateContentDescription(IJavaElement element, CompilationUnit root, long time, boolean useReconciler) {
		String version= root.getAST().apiLevel() == AST.JLS2 ? "AST Level 2" : "AST Level 3";  //$NON-NLS-1$//$NON-NLS-2$
		if (useReconciler)
			version+= ", from reconciler"; //$NON-NLS-1$
		TreeInfoCollector collector= new TreeInfoCollector(root);

		String msg= "{0} ({1}).  Creation time: {2,number} ms.  Size: {3,number} nodes, {4,number} bytes (AST nodes only)."; //$NON-NLS-1$
		Object[] args= { element.getElementName(), version, new Long(time),  new Integer(collector.getNumberOfNodes()), new Integer(collector.getSize())};
		setContentDescription(MessageFormat.format(msg, args));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		if (fSuperListener != null) {
			if (fEditor != null) {
				uninstallModificationListener();
			}
			ISelectionService service= getSite().getWorkbenchWindow().getSelectionService();
			service.removePostSelectionListener(fSuperListener);
			getSite().getPage().removePartListener(fSuperListener);
			FileBuffers.getTextFileBufferManager().removeFileBufferListener(fSuperListener);
			fSuperListener.dispose(); // removes reference to view
			fSuperListener= null;
		}
		if (fTrayUpdater != null) {
			fViewer.removePostSelectionChangedListener(fTrayUpdater);
			fTray.removePostSelectionChangedListener(fTrayUpdater);
			fTrayUpdater= null;
		}
		super.dispose();
	}
	
	private IStatus getErrorStatus(String message, Throwable th) {
		return new Status(IStatus.ERROR, ASTViewPlugin.getPluginId(), IStatus.ERROR, message, th);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		fSash= new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
		fViewer = new TreeViewer(fSash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		fDrillDownAdapter = new DrillDownAdapter(fViewer);
		fViewer.setContentProvider(new ASTViewContentProvider());
		fViewer.setLabelProvider(new ASTViewLabelProvider());
		fViewer.addSelectionChangedListener(fSuperListener);
		fViewer.addDoubleClickListener(fSuperListener);
		fViewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (!fCreateBindings && element instanceof Binding)
					return false;
				return true;
			}
		});
		
		
		ViewForm trayForm= new ViewForm(fSash, SWT.NONE);
		Label label= new Label(trayForm, SWT.NONE);
		label.setText(" Comparison Tray (* = selection in the upper tree):"); //$NON-NLS-1$
		trayForm.setTopLeft(label);
		
		fTray= new TreeViewer(trayForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		trayForm.setContent(fTray.getTree());
		
		fTrayRoots= new ArrayList();
		fTray.setContentProvider(new TrayContentProvider());
		final TrayLabelProvider trayLabelProvider= new TrayLabelProvider();
		fTray.setLabelProvider(trayLabelProvider);
		fTray.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		fTrayUpdater= new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection viewerSelection= (IStructuredSelection) fViewer.getSelection();
				if (viewerSelection.size() == 1 && viewerSelection.getFirstElement() instanceof Binding) {
					trayLabelProvider.setViewerElement((Binding) viewerSelection.getFirstElement());
				} else {
					trayLabelProvider.setViewerElement(null);
				}
			}
		};
		fTray.addPostSelectionChangedListener(fTrayUpdater);
		fViewer.addPostSelectionChangedListener(fTrayUpdater);
		fTray.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				performTrayDoubleClick();
			}
		});
		fTray.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection= (IStructuredSelection) event.getSelection();
				boolean deleteEnabled= false;
				if (selection.size() == 1 && selection.getFirstElement() instanceof Binding)
					deleteEnabled= fTray.getTree().isFocusControl();
				fDeleteAction.setEnabled(deleteEnabled);
			}
		});
		fTray.getTree().addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				IStructuredSelection selection= (IStructuredSelection) fTray.getSelection();
				boolean deleteEnabled= false;
				if (selection.size() == 1 && selection.getFirstElement() instanceof Binding)
					deleteEnabled= true;
				fDeleteAction.setEnabled(deleteEnabled);
			}
			public void focusLost(FocusEvent e) {
				fDeleteAction.setEnabled(false);
			}
		});
		//TODO: hook tray context menu with copy & delete actions, ...
		
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		getSite().setSelectionProvider(new ASTViewSelectionProvider());
		
		try {
			IEditorPart part= EditorUtility.getActiveEditor();
			if (part instanceof ITextEditor) {
				setInput((ITextEditor) part);
			}
		} catch (CoreException e) {
			// ignore
		}
		if (fOpenable == null) {
			resetView(null);
		} else {
			setASTUptoDate(fOpenable != null);
		}
	}


	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ASTView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(fViewer.getControl());
		fViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, fViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), fCopyAction);
		bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), fFocusAction);
		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fDeleteAction);
	}

	private void fillLocalPullDown(IMenuManager manager) {
		for (int i= 0; i < fASTVersionToggleActions.length; i++) {
			manager.add(fASTVersionToggleActions[i]);	
		}
		manager.add(new Separator());
		manager.add(fUseReconcilerAction);
		manager.add(fCreateBindingsAction);
		manager.add(new Separator());
		manager.add(fLinkWithEditor);
	}

	protected void fillContextMenu(IMenuManager manager) {
		manager.add(fFocusAction);
		manager.add(fRefreshAction);
		manager.add(fClearAction);
		manager.add(fCollapseAction);
		manager.add(fExpandAction);
		manager.add(new Separator());
		manager.add(fCopyAction);
		if (fAddToTrayAction.isEnabled())
			manager.add(fAddToTrayAction);
		manager.add(new Separator());

		fDrillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fFocusAction);
		manager.add(fRefreshAction);
		manager.add(fClearAction);
		manager.add(new Separator());
		fDrillDownAdapter.addNavigationActions(manager);
		manager.add(new Separator());
		manager.add(fExpandAction);
		manager.add(fCollapseAction);
		manager.add(fLinkWithEditor);
	}
	
	private void setASTUptoDate(boolean isuptoDate) {
		fRefreshAction.setEnabled(!isuptoDate && fOpenable != null);
	}
	
	private void makeActions() {
		fRefreshAction = new Action() {
			public void run() {
				performRefresh();
			}
		};
		fRefreshAction.setText("&Refresh AST"); //$NON-NLS-1$
		fRefreshAction.setToolTipText("Refresh AST"); //$NON-NLS-1$
		fRefreshAction.setEnabled(false);
		ASTViewImages.setImageDescriptors(fRefreshAction, ASTViewImages.REFRESH);

		fClearAction = new Action() {
			public void run() {
				performClear();
			}
		};
		fClearAction.setText("&Clear AST"); //$NON-NLS-1$
		fClearAction.setToolTipText("Clear AST and release memory"); //$NON-NLS-1$
		fClearAction.setEnabled(false);
		ASTViewImages.setImageDescriptors(fClearAction, ASTViewImages.CLEAR);
		
		fUseReconcilerAction = new Action("&Use Reconciler", IAction.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				performUseReconciler();
			}
		};
		fUseReconcilerAction.setChecked(fDoUseReconciler);
		fUseReconcilerAction.setToolTipText("Use Reconciler to create AST"); //$NON-NLS-1$
		fUseReconcilerAction.setEnabled(true);
		
		fCreateBindingsAction = new Action("&Create Bindings", IAction.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				performCreateBindings();
			}
		};
		fCreateBindingsAction.setChecked(fCreateBindings);
		fCreateBindingsAction.setToolTipText("Create Bindings"); //$NON-NLS-1$
		fCreateBindingsAction.setEnabled(true);

		fFocusAction = new Action() {
			public void run() {
				performSetFocus();
			}
		};
		fFocusAction.setText("&Show AST of active editor"); //$NON-NLS-1$
		fFocusAction.setToolTipText("Show AST of active editor"); //$NON-NLS-1$
		fFocusAction.setActionDefinitionId("org.eclipse.ui.file.refresh"); //$NON-NLS-1$
		ASTViewImages.setImageDescriptors(fFocusAction, ASTViewImages.SETFOCUS);

		fCollapseAction = new Action() {
			public void run() {
				performCollapse();
			}
		};
		fCollapseAction.setText("C&ollapse"); //$NON-NLS-1$
		fCollapseAction.setToolTipText("Collapse Selected Node"); //$NON-NLS-1$
		fCollapseAction.setEnabled(false);
		ASTViewImages.setImageDescriptors(fCollapseAction, ASTViewImages.COLLAPSE);
		
		fExpandAction = new Action() {
			public void run() {
				performExpand();
			}
		};
		fExpandAction.setText("E&xpand"); //$NON-NLS-1$
		fExpandAction.setToolTipText("Expand Selected Node"); //$NON-NLS-1$
		fExpandAction.setEnabled(false);
		ASTViewImages.setImageDescriptors(fExpandAction, ASTViewImages.EXPAND);
		
		fCopyAction= new TreeCopyAction(new Tree[] {fViewer.getTree(), fTray.getTree()});
		
		fDoubleClickAction = new Action() {
			public void run() {
				performDoubleClick();
			}
		};
		
		fLinkWithEditor = new Action() {
			public void run() {
				performLinkWithEditor();
			}
		};
		fLinkWithEditor.setChecked(fDoLinkWithEditor);
		fLinkWithEditor.setText("&Link with Editor"); //$NON-NLS-1$
		fLinkWithEditor.setToolTipText("Link With Editor"); //$NON-NLS-1$
		ASTViewImages.setImageDescriptors(fLinkWithEditor, ASTViewImages.LINK_WITH_EDITOR);
			
		fASTVersionToggleActions= new ASTLevelToggle[] {
				new ASTLevelToggle("AST Level &2.0", AST.JLS2), //$NON-NLS-1$
				new ASTLevelToggle("AST Level &3.0", AST.JLS3) //$NON-NLS-1$
		};
		
		fAddToTrayAction= new Action() {
			public void run() {
				performAddToTray();
			}
		};
		fAddToTrayAction.setText("&Add to Comparison Tray"); //$NON-NLS-1$
		fAddToTrayAction.setToolTipText("Add Selected Node to Comparison Tray"); //$NON-NLS-1$
		fAddToTrayAction.setEnabled(false);
		ASTViewImages.setImageDescriptors(fAddToTrayAction, ASTViewImages.ADD_TO_TRAY);
		
		fDeleteAction= new Action() {
			public void run() {
				performDelete();
			}
		};
		fDeleteAction.setText("&Delete"); //$NON-NLS-1$
		fDeleteAction.setToolTipText("Delete Binding from Tray"); //$NON-NLS-1$
		fDeleteAction.setEnabled(false);
		fDeleteAction.setImageDescriptor(ASTViewPlugin.getDefault().getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		fDeleteAction.setId(ActionFactory.DELETE.getId());
		fDeleteAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);
	}
	
	private void refreshAST() throws CoreException {
		ASTNode node= getASTNodeNearSelection((IStructuredSelection) fViewer.getSelection());
		int offset= 0;
		int length= 0;
		if (node != null) {
			offset= node.getStartPosition();
			length= node.getLength();
		}

		internalSetInput(fOpenable, offset, length, getCurrentASTLevel());
	}
		
	protected void setASTLevel(int level, boolean doRefresh) {
		int oldLevel= fCurrentASTLevel;
		fCurrentASTLevel= level;

		fDialogSettings.put(SETTINGS_JLS, fCurrentASTLevel);
		
		if (doRefresh && fOpenable != null && oldLevel != fCurrentASTLevel) {
			try {
				refreshAST();
			} catch (CoreException e) {
				showAndLogError("Could not set AST to new level.", e); //$NON-NLS-1$
				// set back to old level
				fCurrentASTLevel= oldLevel;
			}
		}
		// update action state
		for (int i= 0; i < fASTVersionToggleActions.length; i++) {
			ASTLevelToggle curr= fASTVersionToggleActions[i];
			curr.setChecked(curr.getLevel() == fCurrentASTLevel);
		}
	}
	
	
	private ASTNode getASTNodeNearSelection(IStructuredSelection selection) {
		Object elem= selection.getFirstElement();
		if (elem instanceof ASTAttribute) {
			return ((ASTAttribute) elem).getParentASTNode();
		} else if (elem instanceof ASTNode) {
			return (ASTNode) elem;
		}
		return null;
	}
	
	private void installModificationListener() {
		fCurrentDocument= fEditor.getDocumentProvider().getDocument(fEditor.getEditorInput());
		fCurrentDocument.addDocumentListener(fSuperListener);
	}
	
	private void uninstallModificationListener() {
		if (fCurrentDocument != null) {
			fCurrentDocument.removeDocumentListener(fSuperListener);
			fCurrentDocument= null;
		}
	}
		
	protected void handleDocumentDisposed(IDocument document) {
		uninstallModificationListener();
	}
	
	protected void handleDocumentChanged(IDocument document) {
		setASTUptoDate(false);
	}
	
	protected void handleSelectionChanged(ISelection selection) {
		fExpandAction.setEnabled(!selection.isEmpty());
		fCollapseAction.setEnabled(!selection.isEmpty());
		fCopyAction.setEnabled(!selection.isEmpty());
		
		boolean addEnabled= false;
		IStructuredSelection structuredSelection= (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			Object first= structuredSelection.getFirstElement();
			if (first instanceof Binding)
				addEnabled= ((Binding) first).getBinding() != null && fViewer.getTree().isFocusControl();
		}
		fAddToTrayAction.setEnabled(addEnabled);
	}

	protected void handleEditorPostSelectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!fDoLinkWithEditor || !(selection instanceof ITextSelection)) {
			return;
		}
		if (fRoot == null || part != fEditor) {
			if (part instanceof ITextEditor && (EditorUtility.getJavaInput((ITextEditor) part) != null)) {
				try {
					setInput((ITextEditor) part);
				} catch (CoreException e) {
					setContentDescription(e.getStatus().getMessage());
				}
			}
			
		} else { // fRoot != null && part == fEditor
			doLinkWithEditor(selection);
		}
	}
	
	private void doLinkWithEditor(ISelection selection) {
		ITextSelection textSelection= (ITextSelection) selection;
		int offset= textSelection.getOffset();
		int length= textSelection.getLength();
		
		NodeFinder finder= new NodeFinder(offset, length);
		fRoot.accept(finder);
		ASTNode covering= finder.getCoveringNode();
		if (covering != null) {
			fViewer.reveal(covering);
			fViewer.setSelection(new StructuredSelection(covering));
		}
	}

	protected void handleDoubleClick(DoubleClickEvent event) {
		fDoubleClickAction.run();
	}

	protected void performLinkWithEditor() {
		fDoLinkWithEditor= fLinkWithEditor.isChecked();
		fDialogSettings.put(SETTINGS_LINK_WITH_EDITOR, fDoLinkWithEditor);
		
		if (fDoLinkWithEditor && fEditor != null)
			doLinkWithEditor(fEditor.getSelectionProvider().getSelection());
	}

	protected void performCollapse() {
		IStructuredSelection selection= (IStructuredSelection) fViewer.getSelection();
		if (selection.isEmpty()) {
			fViewer.collapseAll();
		} else {
			Object[] selected= selection.toArray();
			fViewer.getTree().setRedraw(false);
			for (int i= 0; i < selected.length; i++) {
				fViewer.collapseToLevel(selected[i], AbstractTreeViewer.ALL_LEVELS);
			}
			fViewer.getTree().setRedraw(true);
		}
	}

	protected void performExpand() {	
		IStructuredSelection selection= (IStructuredSelection) fViewer.getSelection();
		if (selection.isEmpty()) {
			fViewer.expandToLevel(3);
		} else {
			Object[] selected= selection.toArray();
			fViewer.getTree().setRedraw(false);
			for (int i= 0; i < selected.length; i++) {
				fViewer.expandToLevel(selected[i], 3);
			}
			fViewer.getTree().setRedraw(true);
		}
	}

	protected void performSetFocus() {
		IEditorPart part= EditorUtility.getActiveEditor();
		if (part instanceof ITextEditor) {
			try {
				setInput((ITextEditor) part);
			} catch (CoreException e) {
				showAndLogError("Could not set AST view input ", e); //$NON-NLS-1$
			}
		}
	}
	
	protected void performRefresh() {
		if (fOpenable != null) {
			try {
				refreshAST();
			} catch (CoreException e) {
				showAndLogError("Could not set AST view input ", e); //$NON-NLS-1$
			}
		}
	}

	protected void performClear() {
		fOpenable= null;
		try {
			setInput(null);
		} catch (CoreException e) {
			showAndLogError("Could not reset AST view ", e); //$NON-NLS-1$
		}
		resetView(null);
	}

	private void showAndLogError(String message, CoreException e) {
		ASTViewPlugin.log(message, e);
		ErrorDialog.openError(getSite().getShell(), "AST View", message, e.getStatus()); //$NON-NLS-1$
	}
	
	private void showAndLogError(String message, Exception e) {
		IStatus status= new Status(IStatus.ERROR, ASTViewPlugin.getPluginId(), 0, message, e);
		ASTViewPlugin.log(status);
		ErrorDialog.openError(getSite().getShell(), "AST View", null, status); //$NON-NLS-1$
	}
	
	protected void performUseReconciler() {
		fDoUseReconciler= fUseReconcilerAction.isChecked();
		fDialogSettings.put(SETTINGS_USE_RECONCILER, fDoUseReconciler);
		
		performRefresh();
	}
	
	protected void performCreateBindings() {
		fCreateBindings= fCreateBindingsAction.isChecked();
		fDialogSettings.put(SETTINGS_NO_BINDINGS, !fCreateBindings);
		performRefresh();
	}
	
	protected void performDoubleClick() {
		ISelection selection = fViewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();

		boolean isTrippleClick= (obj == fPreviousDouble);
		fPreviousDouble= isTrippleClick ? null : obj;
		
		if (obj instanceof ExceptionAttribute) {
			RuntimeException exception= ((ExceptionAttribute) obj).getException();
			if (exception != null) {
				String label= ((ExceptionAttribute) obj).getLabel();
				showAndLogError("An error occurred while calculating an AST View Label:\n" + label, exception); //$NON-NLS-1$
				return;
			}
		}
		
		ASTNode node= null;
		if (obj instanceof ASTNode) {
			node= (ASTNode) obj;
		} else if (obj instanceof NodeProperty) {
			Object val= ((NodeProperty) obj).getNode();
			if (val instanceof ASTNode) {
				node= (ASTNode) val;
			}
		} else if (obj instanceof Binding) {
			IBinding binding= ((Binding) obj).getBinding();
			ASTNode declaring= fRoot.findDeclaringNode(binding);
			if (declaring != null) {
				fViewer.reveal(declaring);
				fViewer.setSelection(new StructuredSelection(declaring));
			}
			return;
		} else if (obj instanceof ProblemNode) {
			ProblemNode problemNode= (ProblemNode) obj;
			EditorUtility.selectInEditor(fEditor, problemNode.getOffset(), problemNode.getLength());
			return;
		} else if (obj instanceof JavaElement) {
			IJavaElement javaElement= ((JavaElement) obj).getJavaElement();
			if (javaElement instanceof IPackageFragment) {
				ShowInPackageViewAction showInPackageViewAction= new ShowInPackageViewAction(getViewSite());
				showInPackageViewAction.run(javaElement);
			} else {
				try {
					IEditorPart editorPart= JavaUI.openInEditor(javaElement);
					if (editorPart != null)
						JavaUI.revealInEditor(editorPart, javaElement);
				} catch (PartInitException e) {
					showAndLogError("Could not open editor.", e); //$NON-NLS-1$
				} catch (JavaModelException e) {
					showAndLogError("Could not open editor.", e); //$NON-NLS-1$
				}
			}
			return;
		}
		
		if (node != null) {
			int offset= isTrippleClick ? fRoot.getExtendedStartPosition(node) : node.getStartPosition();
			int length= isTrippleClick ? fRoot.getExtendedLength(node) : node.getLength();

			EditorUtility.selectInEditor(fEditor, offset, length);
		}
	}

	protected void performAddToTray() {
		IStructuredSelection selection= (IStructuredSelection) fViewer.getSelection();
		Object firstElement= selection.getFirstElement();
		if (! fTrayRoots.contains(firstElement)) {
			fTrayRoots.add(firstElement);
			fTray.setInput(fTrayRoots);
		}
		if (fSash.getMaximizedControl() != null) {
			int trayHeight= fTray.getTree().getItemHeight() * (2 + TrayContentProvider.DEFAULT_CHILDREN_COUNT);
			int sashHeight= fSash.getClientArea().height;
			fSash.setWeights(new int[] { sashHeight - trayHeight, trayHeight });
			fSash.setMaximizedControl(null);
		}
		setTraySelection(selection);
	}
	
	private void setTraySelection(IStructuredSelection selection) {
		fTray.setSelection(selection, true);
		TreeItem[] itemSelection= fTray.getTree().getSelection();
		if (itemSelection.length > 0)
			fTray.getTree().setTopItem(itemSelection[0]);
	}

	protected void performTrayDoubleClick() {
		IStructuredSelection selection= (IStructuredSelection) fTray.getSelection();
		if (selection.size() != 1)
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof ExceptionAttribute) {
			RuntimeException exception= ((ExceptionAttribute) obj).getException();
			if (exception != null) {
				String label= ((ExceptionAttribute) obj).getLabel();
				showAndLogError("An error occurred while calculating an AST View Label:\n" + label, exception); //$NON-NLS-1$
				return;
			}
		}
		if (obj instanceof Binding) {
			Binding binding= (Binding) obj;
			fViewer.setSelection(new StructuredSelection(binding), true);
		}
	}
		
	protected void performDelete() {
		IStructuredSelection selection= (IStructuredSelection) fTray.getSelection();
		if (selection.size() != 1)
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof Binding) {
			int index= fTrayRoots.indexOf(obj);
			if (index != -1) {
				fTrayRoots.remove(index);
				fTray.setInput(fTrayRoots);
				int newSize= fTrayRoots.size();
				if (newSize == 0)
					return;
				else if (index == newSize)
					setTraySelection(new StructuredSelection(fTrayRoots.get(newSize - 1)));
				else
					setTraySelection(new StructuredSelection(fTrayRoots.get(index)));
			}
		}
	}
	
	public void setFocus() {
		fViewer.getControl().setFocus();
	}

	public ShowInContext getShowInContext() {
		return new ShowInContext(null, getSite().getSelectionProvider().getSelection());
	}
}