package org.essentialplatform.server.tests.persistence;

import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.transaction.changes.AddToCollectionChange;
import org.essentialplatform.runtime.shared.transaction.changes.ApplyingChangesComparator;
import org.essentialplatform.runtime.shared.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.shared.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.transaction.changes.InstantiationChange;
import org.essentialplatform.runtime.shared.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.shared.transaction.changes.RemoveFromCollectionChange;

public class TestApplyingChangesComparator extends AbstractRuntimeClientTestCase {

	public TestApplyingChangesComparator() {
		super(null);
	}
	
	private IChange nullChange1, nullChange2;
	private IChange irreversibleChange1, irreversibleChange2;
	private IChange instantiationChange1, instantiationChange2;
	private IChange attributeChange1, attributeChange2;
	private IChange oneToOneReferenceChange1, oneToOneReferenceChange2;
	private IChange addToCollectionChange1, addToCollectionChange2;
	private IChange removeFromCollectionChange1, removeFromCollectionChange2;
	private IChange deletionChange1, deletionChange2;
	private IChange nothingChange1, nothingChange2;
	

	private ApplyingChangesComparator comparator = new ApplyingChangesComparator();
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		nullChange1 = IChange.NULL; 
		nullChange2 = IChange.NULL;
		irreversibleChange1 = IChange.IRREVERSIBLE; 
		irreversibleChange2 = IChange.IRREVERSIBLE;
		instantiationChange1 = new InstantiationChange(); 
		instantiationChange2 = new InstantiationChange();
		attributeChange1 = new AttributeChange();
		attributeChange2 = new AttributeChange();
		oneToOneReferenceChange1 = new OneToOneReferenceChange();
		oneToOneReferenceChange2 = new OneToOneReferenceChange();
		addToCollectionChange1 = new AddToCollectionChange();
		addToCollectionChange2 = new AddToCollectionChange();
		removeFromCollectionChange1 = new RemoveFromCollectionChange();
		removeFromCollectionChange2 = new RemoveFromCollectionChange();
		deletionChange1 = new DeletionChange();
		deletionChange2 = new DeletionChange();
		nothingChange1 = null;
		nothingChange2 = null;
	}

	@Override
	public void tearDown() throws Exception {
		nullChange1 = null; 
		nullChange2 = null;
		irreversibleChange1 = null; 
		irreversibleChange2 = null;
		instantiationChange1 = null; 
		instantiationChange2 = null;
		attributeChange1 = null;
		attributeChange2 = null;
		oneToOneReferenceChange1 = null;
		oneToOneReferenceChange2 = null;
		addToCollectionChange1 = null;
		addToCollectionChange2 = null;
		removeFromCollectionChange1 = null;
		removeFromCollectionChange2 = null;
		deletionChange1 = null;
		deletionChange2 = null;
		nothingChange1 = null;
		nothingChange2 = null;
		super.tearDown();
	}

	public void testNullAgainstEverythingElse() {
		IChange p = nullChange1;
		
		assertEquals(0, comparator.compare(p, nullChange2));
		assertEquals(0, comparator.compare(nullChange2, p));
		
		assertEquals(-1, comparator.compare(p, irreversibleChange2));
		assertEquals(+1, comparator.compare(irreversibleChange2, p));
		
		assertEquals(-1, comparator.compare(p, instantiationChange2));
		assertEquals(+1, comparator.compare(instantiationChange2, p));
		
		assertEquals(-1, comparator.compare(p, attributeChange2));
		assertEquals(+1, comparator.compare(attributeChange2, p));
		
		assertEquals(-1, comparator.compare(p, oneToOneReferenceChange2));
		assertEquals(+1, comparator.compare(oneToOneReferenceChange2, p));
		
		assertEquals(-1, comparator.compare(p, addToCollectionChange2));
		assertEquals(+1, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testIrreversibleAgainstEverythingElse() {
		IChange p = irreversibleChange1;
		
		assertEquals(0, comparator.compare(p, irreversibleChange2));
		assertEquals(0, comparator.compare(irreversibleChange2, p));
		
		assertEquals(-1, comparator.compare(p, instantiationChange2));
		assertEquals(+1, comparator.compare(instantiationChange2, p));
		
		assertEquals(-1, comparator.compare(p, attributeChange2));
		assertEquals(+1, comparator.compare(attributeChange2, p));
		
		assertEquals(-1, comparator.compare(p, oneToOneReferenceChange2));
		assertEquals(+1, comparator.compare(oneToOneReferenceChange2, p));
		
		assertEquals(-1, comparator.compare(p, addToCollectionChange2));
		assertEquals(+1, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testInstantiatedAgainstEverythingElse() {
		IChange p = instantiationChange1;
		
		assertEquals(0, comparator.compare(p, instantiationChange2));
		assertEquals(0, comparator.compare(instantiationChange2, p));
		
		assertEquals(-1, comparator.compare(p, attributeChange2));
		assertEquals(+1, comparator.compare(attributeChange2, p));
		
		assertEquals(-1, comparator.compare(p, oneToOneReferenceChange2));
		assertEquals(+1, comparator.compare(oneToOneReferenceChange2, p));
		
		assertEquals(-1, comparator.compare(p, addToCollectionChange2));
		assertEquals(+1, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testAttributeChangeAgainstEverythingElse() {
		IChange p = attributeChange1;
		
		assertEquals(0, comparator.compare(p, attributeChange2));
		assertEquals(0, comparator.compare(attributeChange2, p));
		
		assertEquals(-1, comparator.compare(p, oneToOneReferenceChange2));
		assertEquals(+1, comparator.compare(oneToOneReferenceChange2, p));
		
		assertEquals(-1, comparator.compare(p, addToCollectionChange2));
		assertEquals(+1, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testOneToOneReferenceChangeAgainstEverythingElse() {
		IChange p = oneToOneReferenceChange1;
		
		assertEquals(0, comparator.compare(p, oneToOneReferenceChange2));
		assertEquals(0, comparator.compare(oneToOneReferenceChange2, p));
		
		assertEquals(-1, comparator.compare(p, addToCollectionChange2));
		assertEquals(+1, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testAddToCollectionChangeAgainstEverythingElse() {
		IChange p = addToCollectionChange1;
		
		assertEquals(0, comparator.compare(p, addToCollectionChange2));
		assertEquals(0, comparator.compare(addToCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(+1, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testRemoveFromCollectionChangeAgainstEverythingElse() {
		IChange p = removeFromCollectionChange1;
		
		assertEquals(0, comparator.compare(p, removeFromCollectionChange2));
		assertEquals(0, comparator.compare(removeFromCollectionChange2, p));
		
		assertEquals(-1, comparator.compare(p, deletionChange2));
		assertEquals(+1, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testDeletionChangeAgainstEverythingElse() {
		IChange p = deletionChange1;
		
		assertEquals(0, comparator.compare(p, deletionChange2));
		assertEquals(0, comparator.compare(deletionChange2, p));
		
		assertEquals(-1, comparator.compare(p, nothingChange2));
		assertEquals(+1, comparator.compare(nothingChange2, p));
	}

	public void testNothingChangeAgainstEverythingElse() {
		IChange p = nothingChange1;
		
		assertEquals(0, comparator.compare(p, nothingChange2));
		assertEquals(0, comparator.compare(nothingChange2, p));
	}


}
