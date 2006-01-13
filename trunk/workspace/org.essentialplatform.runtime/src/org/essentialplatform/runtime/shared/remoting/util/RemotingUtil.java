package org.essentialplatform.runtime.shared.remoting.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public final class RemotingUtil {
	
	private RemotingUtil(){}

	public static void dumpTo(byte[] bytes, String filename) {
		dumpTo(new ByteArrayInputStream(bytes), filename);
	}
	
	public static void dumpTo(InputStream is, String filename) {
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			IOUtils.copy(is, fos);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

}
