package de.berlios.rcpviewer.persistence.inmemory;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class InMemorySearchPage
extends DialogPage
implements ISearchPage
{
	
	ISearchPageContainer _searchPageContainer;
	

	public boolean performAction() {
		

		InMemorySearchQuery searchResult= new InMemorySearchQuery();
		
		
		NewSearchUI.activateSearchResultView();
		NewSearchUI.runQueryInForeground(_searchPageContainer.getRunnableContext(), searchResult);
		
		return true;
	}

	public void setContainer(ISearchPageContainer pContainer) {
		_searchPageContainer= pContainer;
		
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite partControl= new Composite(parent, SWT.NO_FOCUS);
		FillLayout layout= new FillLayout();
		layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		partControl.setLayout(layout);
		
		Label label= new Label(partControl, SWT.NO_FOCUS | SWT.WRAP);
		label.setText("No search configuration is necessary.\nAll objects in all in-memory object stores will be displayed in the search results.");
		
		setControl(partControl);
	}

}
