package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.MultiLine;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Named;

@InDomain
public class CustomerToTestMultiLine {


	/**
	 * No annotations at all.
	 * 
	 * @return
	 */
	public String getAttributeWithNoAnnotations() {
		return attributeWithNoAnnotations;
	}
	private String attributeWithNoAnnotations;

	/**
	 * Operation to check
	 * No annotations at all.
	 * 
	 * @return
	 */
	public void operationToUpdateAttributeWithNoAnnotations(
			@Named("attributeWithNoAnnotations")
			final String attributeWithNoAnnotations) {
		this.attributeWithNoAnnotations = attributeWithNoAnnotations;
	}


	/**
	 * Non-string attribute; annotations should be ignored.
	 * 
	 * @return
	 */
	@MultiLine(5)
	public int getNonStringAttributeWithMultiLineAnnotation() {
		return nonStringAttributeWithMultiLineAnnotation;
	}
	private int nonStringAttributeWithMultiLineAnnotation;

	/**
	 * Non-string attribute; annotations should be ignored.
	 * 
	 * @return
	 */
	public void operationToUpdateNonStringAttributeWithMultiLineAnnotation(
			@Named("nonStringAttributeWithMultiLineAnnotation")
			@MultiLine(5)
			final int nonStringAttributeWithMultiLineAnnotation) {
		this.nonStringAttributeWithMultiLineAnnotation = nonStringAttributeWithMultiLineAnnotation;
	}

	/**
	 * Valid multiline annotation.
	 * 
	 * @return
	 */
	@MultiLine(5)
	public String getComments() {
		return comments;
	}
	private String comments;
	/**
	 * Operation to update comments.
	 * 
	 * @return
	 */
	public void updateComments(
			@Named("Comments")
			@MultiLine(5)
			final String coments) {
		this.comments = coments;
	}


	/**
	 * Operation to check:
	 * Error conditions: -ve value for multiline.
	 */
	public void operationToUpdateAttributeWithNegativeMultiLine(
			@Named("attributeWithNegativeMultiLine")
			@MultiLine(-2)
			final String attributeWithNegativeMultiLine) {
		this.attributeWithNegativeMultiLine = attributeWithNegativeMultiLine;
	}

	
	/**
	 * Error conditions: -ve values for multiline
	 * 
	 * @return
	 */
	@MultiLine(-2)
	public String getAttributeWithNegativeMultiLine() {
		return attributeWithNegativeMultiLine;
	}
	private String attributeWithNegativeMultiLine;


}