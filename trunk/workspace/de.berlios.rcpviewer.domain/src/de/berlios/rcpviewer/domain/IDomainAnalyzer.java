package de.berlios.rcpviewer.domain;


public interface IDomainAnalyzer {
	
	public <V> void analyze(IDomainClass<V> domainClass);
	

}
