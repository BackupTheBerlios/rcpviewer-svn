package com.ibm.developerworks.google.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.google.soap.search.GoogleSearchResultElement;

public class SearchViewLabelProvider extends LabelProvider implements
        ITableLabelProvider
{

    public Image getColumnImage(Object element, int columnIndex)
    {
        return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
        switch (columnIndex)
        {
        case 0:
            return ((GoogleSearchResultElement) element).getTitle();
        case 1:
            return ((GoogleSearchResultElement) element).getURL();

        }
        return "";

    }

}