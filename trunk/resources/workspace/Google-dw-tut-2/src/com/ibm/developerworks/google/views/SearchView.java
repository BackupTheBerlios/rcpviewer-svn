package com.ibm.developerworks.google.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.dialogs.ViewContentProvider;
import org.eclipse.ui.part.ViewPart;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchFault;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;
import com.ibm.developerworks.google.wizards.LicenseKeyWizard;

public class SearchView extends ViewPart implements IDoubleClickListener
{
    public static final String ID = "com.ibm.developerworks.google.views.SearchView";

    private TableViewer tableViewer;

    private Text searchText;

    private GoogleSearchResultElement model;

    public void createPartControl(Composite parent)
    {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginHeight = 5;
        gridLayout.marginWidth = 5;

        parent.setLayout(gridLayout);

        Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText("Search:");

        searchText = new Text(parent, SWT.BORDER);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));

        Button searchButton = new Button(parent, SWT.PUSH);
        searchButton.setText(" Search ");
        searchButton.addSelectionListener(new SelectionListener()
        {

            public void widgetSelected(SelectionEvent e)
            {
                String licenseKey = LicenseKeyWizard.getLicenseKey();

                if (licenseKey == null)
                {
                    MessageDialog.openError(e.display.getActiveShell(),
                            "License Key",
                            "You must define a Google API license key.");
                    return;
                }
                GoogleSearch search = new GoogleSearch();
                search.setKey(licenseKey);
                search.setQueryString(searchText.getText());
                try
                {
                    GoogleSearchResult result = search.doSearch();

                    tableViewer.setInput(model);
                    tableViewer.add(result.getResultElements());

                } catch (GoogleSearchFault ex)
                {
                    MessageDialog.openWarning(e.display.getActiveShell(),
                            "Google Error", ex.getMessage());
                }

            }

            public void widgetDefaultSelected(SelectionEvent e)
            {

            }
        });

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
        tableViewer.setLabelProvider(new SearchViewLabelProvider());
        tableViewer.setContentProvider(new ViewContentProvider());
        tableViewer.setInput(model);
        tableViewer.getControl().setLayoutData(gridData);
        tableViewer.addDoubleClickListener(this);

        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn titleColumn = new TableColumn(table, SWT.NONE);
        titleColumn.setText("Title");
        titleColumn.setWidth(250);

        TableColumn urlColumn = new TableColumn(table, SWT.NONE);
        urlColumn.setText("URL");
        urlColumn.setWidth(200);

    }

    public void setFocus()
    {
       searchText.setFocus();
    }

    public void doubleClick(DoubleClickEvent event)
    {
        if (!tableViewer.getSelection().isEmpty())
        {

            IStructuredSelection ss = (IStructuredSelection) tableViewer
                    .getSelection();
            GoogleSearchResultElement element = (GoogleSearchResultElement) ss
                    .getFirstElement();

            BrowserView.browser.setUrl(element.getURL());
        }

    }
}