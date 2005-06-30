package de.berlios.rcpviewer.persistence.inmemory;

import net.sf.plugins.utils.SWTUtils;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPageSite;

import de.berlios.rcpviewer.gui.jobs.OpenDomainObjectJob;
import de.berlios.rcpviewer.session.IDomainObject;

public class InMemorySearchResultPage
implements ISearchResultPage
{
	
	private String _id;
	private InMemorySearchQuery _searchResult;
	private Object _uiState;
	private ISearchResultViewPart _searchResultViewPart;
	IPageSite _pageSite;
	IActionBars _actionBars;
	
	Composite _partControl;
	TableViewer _tableViewer;
	
	

	public String getID() {
		return _id;
	}

	public String getLabel() {
		return "All objects in all in-memory object stores";
	}

	public Object getUIState() {
		return _uiState;
	}

	public void restoreState(IMemento pMemento) {
		// do nothing		
	}

	public void saveState(IMemento pMemento) {
		// do nothing		
	}

	public void setID(String pId) {
		_id= pId;
	}

	public void setInput(ISearchResult pSearch, Object pUiState) {
		if (pSearch == null)
			pSearch= new InMemorySearchQuery();
		_searchResult= (InMemorySearchQuery)pSearch;
		_uiState= pUiState;
		_tableViewer.setContentProvider(_searchResult);
		_tableViewer.setLabelProvider(_searchResult);
		_tableViewer.setInput(_searchResult);
		
		_searchResult.addListener(new ISearchResultListener() {
			public void searchResultChanged(org.eclipse.search.ui.SearchResultEvent e) {
				_partControl.getDisplay().asyncExec(new Runnable() {
					public void run() {
						Table table= _tableViewer.getTable();
						table.setRedraw(false);
						try {
							_tableViewer.refresh();
							SWTUtils.packTableColumns(table);
						}
						finally {
							table.setRedraw(true);
						}
					};
				});
			};
		});
	}

	public void setViewPart(ISearchResultViewPart part) {
		_searchResultViewPart= part;
	}

	public IPageSite getSite() {
		return _pageSite;
	}

	public void init(IPageSite pSite) throws PartInitException {
		_pageSite= pSite;
	}

	public void createControl(Composite parent) {
		_partControl= new Composite(parent, SWT.NO_FOCUS);
		_partControl.setLayout(new FillLayout());
		
		Table table= new Table(_partControl, SWT.FULL_SELECTION | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		
		TableColumn column= new TableColumn(table, SWT.LEFT);
		column.setText("Class");
		column.setWidth(200);
		
		column= new TableColumn(table, SWT.LEFT);
		column.setText("Title");
		column.setWidth(200);

		_tableViewer= new TableViewer(table);
		_tableViewer.addOpenListener(new IOpenListener() {
			public void open(OpenEvent event) {
				IStructuredSelection selection= (IStructuredSelection)event.getSelection();
				if (selection.isEmpty())
					return;
				IDomainObject domainObject= (IDomainObject)selection.getFirstElement();
				// REVIEW_CHANGE for Ted - using job mechanism now
				/* old ...
				OpenDomainObjectActionDelegate action= 
					new  OpenDomainObjectActionDelegate(_pageSite.getWorkbenchWindow(), domainObject);
				action.run();
				*/
				new OpenDomainObjectJob( domainObject ).schedule();
				
			};
		});
	}

	public void dispose() {
		// do nothing
	}

	public Control getControl() {
		return _partControl;
	}

	public void setActionBars(IActionBars pActionBars) {
		_actionBars= pActionBars;
	}

	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
