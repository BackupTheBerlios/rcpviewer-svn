package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


@InDomain
public class DepartmentReferencesOnly  {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	private City city;
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public void associateCity(City city) {
		setCity(city);
	}
	public void dissociateCity(City city) {
		setCity(null);
	}
	

	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}

}
