/**
 * 
 */
package org.essentialplatform.louis.views.ops;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.part.PageSite;

/**
 * Equivalent to the <code>PropertiesSheet</code> but displays operations for
 * whatever <code>IDomainObject</code> (if any) that the current 
 * <code>IEditorPart</code> is displaying.
 * <br>Works exactly like <code>PropertiesSheet</code> except for:
 * <ul>
 * <li>only interested in open <code>IEditorPart</code>
 * <li>asks them for <code>getAdapter(IOpsViewPage.class)</code>
 * </ul>
 * @author Mike
 */
public class OpsView extends PageBookView {

	public static final String ID = OpsView.class.getName();
	
    // The initial selection when the property sheet opens
    private ISelection _bootstrapSelection = null;


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
	 */
	@Override
	protected IPage createDefaultPage(PageBook book) {
        DefaultOpsViewPage page = new DefaultOpsViewPage();
        page.init( new PageSite( getViewSite() ) );
        page.createControl(book);
        return page;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
        
		//look for adpater
		IOpsViewPage page
			= (IOpsViewPage) part.getAdapter(IOpsViewPage.class );
        if ( page != null ) {
            if (page instanceof IPageBookViewPage) {
                initPage((IPageBookViewPage) page);
            }
            page.createControl(getPageBook());
            return new PageRec( part, page );
        }

        // Use the default page		
        return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#doDestroyPage(org.eclipse.ui.IWorkbenchPart, org.eclipse.ui.part.PageBookView.PageRec)
	 */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		IOpsViewPage page = (IOpsViewPage) pageRecord.page;
        page.dispose();
        pageRecord.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#getBootstrapPart()
	 */
	@Override
	protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
        	_bootstrapSelection = page.getSelection();
            IWorkbenchPart part = page.getActivePart();
            if ( isImportant( part ) ) return part;
        } 
        return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return ( part instanceof IEditorPart );
	}


}
