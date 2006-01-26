//$Id: Bid.java,v 1.3 2005/07/26 04:57:09 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Bid {
	private Integer id;
	private String description;
	private Starred note;
	private Starred editorsNote;
	private Boolean approved;

	@Column(columnDefinition = "VARCHAR(10)")
	public Starred getEditorsNote() {
		return editorsNote;
	}

	public void setEditorsNote(Starred editorsNote) {
		this.editorsNote = editorsNote;
	}

	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Starred getNote() {
		return note;
	}

	public void setNote(Starred note) {
		this.note = note;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

}
