package de.berlios.rcpviewer;


public interface RCPViewer {
	public static final String PLUGIN_ID= "de.berlios.rcpviewer.rcp"; 
	public static final String RCP_VIEWER_PERSPECTIVE_ID= PLUGIN_ID+".viewerPerspective"; 
	public static final String OBJECT_EDITOR_ID= PLUGIN_ID+".objectEditor";
	public static final String DOMAINS_EXTENSION_POINT_ID = PLUGIN_ID+".domains";
	public static final String FIELDBUILDERS_EXTENSION_POINT_ID =  PLUGIN_ID+".fieldbuilders";

}
