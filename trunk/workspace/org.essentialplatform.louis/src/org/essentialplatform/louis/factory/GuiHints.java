/**
 * 
 */
package org.essentialplatform.louis.factory;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.essentialplatform.louis.util.FontUtil;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Experiment...
 * @author Mike
 */
public class GuiHints {
	
	public static final int NONE = 1;
	public static final int COMPACT = 1<<2;
	public static final int INCLUDE_ATTRIBUTES=1<<3;
	public static final int INCLUDE_REFERENCES=1<<4;
	public static final int ALL = INCLUDE_ATTRIBUTES | INCLUDE_REFERENCES;
	public static final int READ_ONLY=1<<5;
	
	public static final GuiHints DUMMY = new GuiHints( NONE );
	
	
	private final int _style;
	private final int[] _columnWidths;
	private final int _maxLabelLength;
	
	public GuiHints( 
			int style,
			IDomainClass dClass, 
			Composite parent ) {
		if ( style < 1 ) throw new IllegalArgumentException();
		if ( dClass == null ) throw new IllegalArgumentException();
		if ( parent == null ) throw new IllegalArgumentException();
		
		_style = style;
		
		int maxLabelLength = 0;
		for ( EAttribute a : dClass.eAttributes() ) {       
			int length = a.getName().length();
			if ( length > maxLabelLength ) maxLabelLength = length;
		}
		for ( EReference r : dClass.eReferences() ) {
			int length = r.getName().length();
			if ( length > maxLabelLength ) maxLabelLength = length;
		}
		_maxLabelLength = maxLabelLength;
		
		// first column width is maximum label length
		int[] columnWidths = new int[]{ 0, 0, 0 };
		parent.setFont( FontUtil.getLabelFont() );
		columnWidths[0] = maxLabelLength * FontUtil.getCharWidth( 
				parent, FontUtil.CharWidthType.SAFE );
		
		// more faff - want to align labels even if references have toggle 
		// icons - could do much faff calculating size - make do with hardcode 
		// value for now
		if ( !dClass.eReferences().isEmpty() ) {
			columnWidths[0] = columnWidths[0] + 4;
		}
		
		_columnWidths = columnWidths;
	}
	
	public GuiHints( int style ) {
		_style = style;
		_columnWidths = null;
		_maxLabelLength = -1;
	}
	
	/**
	 * Whether or not the style matches the passed value.
	 * @param style
	 * @return
	 */
	public boolean styleMatches( int style ) {
		return ( ( _style & style ) != 0 );
	}

	public int[] getColumnWidths() {
		return _columnWidths;
	}

	public int getMaxLabelLength() {
		return _maxLabelLength;
	}
}

