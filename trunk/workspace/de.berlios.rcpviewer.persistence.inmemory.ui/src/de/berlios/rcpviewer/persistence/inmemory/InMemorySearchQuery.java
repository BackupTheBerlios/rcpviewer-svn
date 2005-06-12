package de.berlios.rcpviewer.persistence.inmemory;

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

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

public class InMemorySearchQuery
implements ISearchResult, ISearchQuery, ITableLabelProvider, IStructuredContentProvider
{
	Map<InMemoryObjectStore, ISession> _sessionByObjectStore= new HashMap<InMemoryObjectStore, ISession>(); 
	Collection<IDomainObject> _allObjects= new ArrayList<IDomainObject>();
	HashSet<ISearchResultListener> _searchListeners= new HashSet<ISearchResultListener>();
	
	public boolean canRerun() {
		return true;
	}
	public boolean canRunInBackground() {
		return false;
	}
	public ISearchResult getSearchResult() {
		return this;
	}
	public IStatus run(IProgressMonitor pMonitor) throws OperationCanceledException {
		try {
			
			for (ISession session: RuntimePlugin.getDefault().getSessionManager().getAllSessions()) {
				IObjectStore objectStore= session.getObjectStore();
				if (objectStore instanceof InMemoryObjectStore) {
					InMemoryObjectStore memoryObjectStore= (InMemoryObjectStore)objectStore;
					for (Object pojo: memoryObjectStore.getAllStoredObjects()) {
						IDomainObject domainObject= session.getDomainObjectFor(pojo, pojo.getClass()); 
						_allObjects.add(domainObject);
					}
				}
			}
			
			for (ISearchResultListener listener: _searchListeners)
				listener.searchResultChanged(new SearchResultEvent(this) { });
			
			return new Status(Status.OK, "de.berlios.rcpviewer.persistence.inmemory.ui", 0, "", null);

		} catch (CoreException e) {
			throw new OperationCanceledException(e.getMessage());
		}
	}			
	
	
	
	
	public void addListener(ISearchResultListener pL) {
		_searchListeners.add(pL);
	}
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	public String getLabel() {
		return "All objects in all in-memory object stores";
	}
	public ISearchQuery getQuery() {
		return this;
	}
	public String getTooltip() {
		return "All objects in all in-memory object stores";
	}
	public void removeListener(ISearchResultListener pL) {
		// do nothing				
	}

	
	
	public void inputChanged(Viewer pViewer, Object pOldInput, Object pNewInput) {
		// do nothing		
	}
	public Object[] getElements(Object pInputElement) {
		return _allObjects.toArray(new IDomainObject[_allObjects.size()]);
	}
	public Image getColumnImage(Object pElement, int pColumnIndex) {
		return null;
	}

	public String getColumnText(Object pElement, int pColumnIndex) {
		switch (pColumnIndex) {
		case 0:
			return ((IDomainObject)pElement).getDomainClass().getName();

		default:
			return ((IDomainObject)pElement).title();
		}
	}

	public void addListener(ILabelProviderListener pListener) {
	}

	public void dispose() {
		// do nothing
		
	}

	public boolean isLabelProperty(Object pElement, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener pListener) {
		//do nothing
		
	}
	public Collection<IDomainObject> getAllObjects() {
		return new ArrayList<IDomainObject>(_allObjects);
	}

	
	
}
