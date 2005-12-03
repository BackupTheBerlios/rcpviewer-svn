	/**
	 * call from createPartControl(Composite parent) using:
	 * <tt>
	 * createAutomationPart(parent, formToolkit, tabFolder);
	 * </tt>
	 * 
	 * Doesn't work though :-(
	 * 
	 * @param parent
	 * @param formToolkit
	 * @param tabFolder
	 */
	private void createAutomationPart(Composite parent, FormToolkit formToolkit, CTabFolder tabFolder) {
		//		///////////////////////////////////////////////////
		//		// automation experiment
		//		// ... isn't working, though - probably because I'm passing in a
				_automationComposite = createTab(formToolkit, tabFolder, "Automation");
				tabFolder.setSelection(3);
				OleFrame oleFrame = new OleFrame(_automationComposite, SWT.NONE);
		//		OleFrame oleFrame = new OleFrame(parent.getShell(), SWT.NONE);
				
				MenuItem fileMenu =
				  new MenuItem(parent.getShell().getMenuBar(), SWT.CASCADE);
				fileMenu.setText("[File]");
				
				MenuItem containerMenu =
				      new MenuItem(parent.getShell().getMenuBar(), SWT.CASCADE);
				    containerMenu.setText("[Container]");
				
				MenuItem windowMenu =
				      new MenuItem(parent.getShell().getMenuBar(), SWT.CASCADE);
				    windowMenu.setText("[Window]");
		
				oleFrame.setFileMenus(new MenuItem[] { fileMenu });
				oleFrame.setContainerMenus(new MenuItem[] { containerMenu });
				oleFrame.setWindowMenus(new MenuItem[] { windowMenu });
		
				IWorkbenchWindow activeWorkbenchWindow = LouisPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		
				oleFrame.setLayout(new FillLayout());
		//		OleClientSite clientSite = new OleControlSite(oleFrame, SWT.NULL, "Shell.Explorer"); //$NON-NLS-1$
		
		//		OleControlSite controlSite = new OleControlSite(oleFrame, SWT.NONE, "Shell.Explorer"); //$NON-NLS-1$
				OleClientSite clientSite = 
					new OleClientSite(oleFrame, SWT.NULL, 
							new File("c:/temp/test.doc")); //$NON-NLS-1$
		//		clientSite.setLayout(new FillLayout());
		//		OleControlSite clientSite = new OleControlSite(oleFrame, SWT.NONE, "Word.Document"); //$NON-NLS-1$
		//		OleControlSite controlSite = new OleControlSite(oleFrame, SWT.NONE, "OVCtl.OVCtl"); //$NON-NLS-1$
		//		OleControlSite controlSite = new OleControlSite(oleFrame, SWT.NONE, "opendocument.WriterDocument.1"); //$NON-NLS-1$
		
				//clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
				clientSite.doVerb(OLE.OLEIVERB_SHOW);
		
				//navigateTo(clientSite, "http://www.haywood-associates.co.uk/rcpviewer/space/start");
				//newMessage(automation);
	}
	
	public void newMessage(OleClientSite clientSite) {
		OleAutomation automation = new OleAutomation(clientSite);
		// dispid=27, type=METHOD, name="NewMessage"
		int[] rgdispid = automation.getIDsOfNames(
				new String[]{"NewMessage"});
		int dispIdMember = rgdispid[0];
	
		automation.invoke(dispIdMember);
	}

	public void navigateTo(OleClientSite clientSite, String url) {
		OleAutomation automation = new OleAutomation(clientSite);
		// dispid=104, type=METHOD, name="Navigate"
		int[] rgdispid = 
			automation.getIDsOfNames(new String[]{"Navigate", "URL"});
		int dispIdMember = rgdispid[0];
	
		Variant[] rgvarg = new Variant[] {new Variant(url)};
		int[] rgdispidNamedArgs = new int[1];
		rgdispidNamedArgs[0] = rgdispid[1]; // identifier of argument
		automation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
	}


