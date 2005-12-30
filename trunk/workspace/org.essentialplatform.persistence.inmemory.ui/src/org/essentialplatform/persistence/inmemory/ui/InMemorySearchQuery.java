package org.essentialplatform.persistence.inmemory.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.graphics.Image;

import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;
import org.essentialplatform.runtime.shared.RuntimePlugin;

public final class InMemorySearchQuery
implements ISearchResult, ISearchQuery, ITableLabelProvider, IStructuredContentProvider
{
	Map<IObjectStore, IClientSession> _sessionByObjectStore= new HashMap<IObjectStore, IClientSession>(); 
	Collection<IDomainObject> _allObjects= new ArrayList<IDomainObject>();
	HashSet<ISearchResultListener> _searchListeners= new HashSet<ISearchResultListener>();

	/*
	 * @see org.eclipse.search.ui.ISearchQuery#canRerun()
	 */
	public boolean canRerun() {
		return true;
	}
	/*
	 * @see org.eclipse.search.ui.ISearchQuery#canRunInBackground()
	 */
	public boolean canRunInBackground() {
		return false;
	}
	/*
	 * @see org.eclipse.search.ui.ISearchQuery#getSearchResult()
	 */
	public ISearchResult getSearchResult() {
		return this;
	}
	/*
	 * @see org.eclipse.search.ui.ISearchQuery#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus run(IProgressMonitor pMonitor) throws OperationCanceledException {
		try {
			
			for (IClientSession session: RuntimePlugin.getDefault().getSessionManager().getAllSessions()) {
//				IObjectStore objectStore= session.getObjectStore();
//				if (objectStore instanceof InMemoryObjectStore) {
//					InMemoryObjectStore memoryObjectStore= (InMemoryObjectStore)objectStore;
//					for (Object pojo: memoryObjectStore.allInstances()) {
//						IDomainObject<?> domainObject= session.getDomainObjectFor(pojo, pojo.getClass()); 
//						_allObjects.add(domainObject);
//					}
//				}
			}
			
			for (ISearchResultListener listener: _searchListeners)
				listener.searchResultChanged(new SearchResultEvent(this) { });
			
			return new Status(Status.OK, "org.essentialplatform.persistence.inmemory.ui", 0, "", null);

		} catch (CoreException e) {
			throw new OperationCanceledException(e.getMessage());
		}
	}			
	
	/*
	 * @see org.eclipse.search.ui.ISearchResult#addListener(org.eclipse.search.ui.ISearchResultListener)
	 */
	public void addListener(ISearchResultListener pL) {
		_searchListeners.add(pL);
	}
	/*
	 * @see org.eclipse.search.ui.ISearchResult#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	/*
	 * @see org.eclipse.search.ui.ISearchQuery#getLabel()
	 */
	public String getLabel() {
		return "All objects in all in-memory object stores";
	}
	/*
	 * @see org.eclipse.search.ui.ISearchResult#getQuery()
	 */
	public ISearchQuery getQuery() {
		return this;
	}
	/*
	 * @see org.eclipse.search.ui.ISearchResult#getTooltip()
	 */
	public String getTooltip() {
		return "All objects in all in-memory object stores";
	}
	/*
	 * @see org.eclipse.search.ui.ISearchResult#removeListener(org.eclipse.search.ui.ISearchResultListener)
	 */
	public void removeListener(ISearchResultListener pL) {
		// do nothing				
	}

	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer pViewer, Object pOldInput, Object pNewInput) {
		// do nothing		
	}
	/*
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object pInputElement) {
		return _allObjects.toArray(new IDomainObject[_allObjects.size()]);
	}
	/*
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object pElement, int pColumnIndex) {
		return null;
	}
	/*
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object pElement, int pColumnIndex) {
		switch (pColumnIndex) {
		case 0:
			return ((IDomainObject)pElement).getDomainClass().getName();

		default:
			return ((IDomainObject)pElement).title();
		}
	}
	/*
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener pListener) {
	}
	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// do nothing
	}
	/*
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object pElement, String property) {
		return true;
	}
	/*
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener pListener) {
		//do nothing
		
	}
	/**
	 * @return copy of all objects that match.
	 */
	public Collection<IDomainObject> getAllObjects() {
		return new ArrayList<IDomainObject>(_allObjects);
	}

	
	
}
