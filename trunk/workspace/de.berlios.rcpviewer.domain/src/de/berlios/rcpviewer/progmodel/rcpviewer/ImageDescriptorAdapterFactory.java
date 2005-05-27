package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.domain.IAdapterFactory;

public class ImageDescriptorAdapterFactory<T> implements IAdapterFactory<T> {

	private String url;

	public ImageDescriptorAdapterFactory() {}
	public ImageDescriptorAdapterFactory(String url) {
		this.url = url;
	}
	public Map<String, String> getDetails() {
		Map<String, String> details = new HashMap<String, String>();
		details.put("url", url);
		return details;
	}
	public T createAdapter(Map<String, String> details) {
		String url = details.get("url");
		URL imageUrl;
		try {
			imageUrl = new URL(url);
			return (T)ImageDescriptor.createFromURL(imageUrl);
		} catch (MalformedURLException e) {
			// TODO should log?
			return null;
		}
	}

}
