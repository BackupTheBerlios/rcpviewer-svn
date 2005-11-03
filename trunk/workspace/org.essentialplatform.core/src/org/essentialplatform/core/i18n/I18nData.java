package org.essentialplatform.core.i18n;


public final class I18nData implements II18nData {

	public I18nData(final String nameKey, final String descriptionKey) {
		this.nameKey = nameKey;
		this.descriptionKey = descriptionKey;
	}

	private final String nameKey;
	public String nameKey() {
		return nameKey;
	}

	private final String descriptionKey;
	public String descriptionKey() {
		return descriptionKey;
	}

}
