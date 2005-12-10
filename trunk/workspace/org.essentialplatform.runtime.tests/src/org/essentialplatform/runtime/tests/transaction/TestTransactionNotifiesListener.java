package org.essentialplatform.runtime.tests.transaction;

import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.AddToCollectionChange;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.Interaction;
import org.essentialplatform.runtime.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.transaction.changes.RemoveFromCollectionChange;
import org.essentialplatform.runtime.transaction.event.TransactionEvent;

/**
 * 
 * @author Dan Haywood
 */
public class TestTransactionNotifiesListener extends AbstractTransactionManagerTestCase {

	private MyTransactionListener listener = new MyTransactionListener();
	private ITransaction calculatorTransaction;
	private ITransaction customerTransaction;

	public TestTransactionNotifiesListener() {
		super(true);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		listener = new MyTransactionListener(); 

		// create a transaction on calculators to listen to.
		calculator.setInitialResult(20);
		calculatorTransaction = transactionManager.getCurrentTransactionFor(calculator, false);
		calculatorTransaction.addTransactionListener(listener);

		// create a transaction on calculators to listen to.
		customer.setFirstName("Joe");
		customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		customerTransaction.addTransactionListener(listener);

	}
	
	@Override
	public void tearDown() throws Exception {
		calculatorTransaction.removeTransactionListener(listener);
		listener = null;
		calculatorTransaction = null;
		super.tearDown();
	}

	/**
	 * Note that the object on which the operation is invoked is not
	 * necessarily enlisted within the calculatorTransaction.
	 */
	public void testInvokingAnOperationNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		calculator.noop();
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);
		
		assertSame(calculatorTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(0, change.getModifiedPojos().size());
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(0, interaction.size());
	}

	public void testSettingAttributeForTransactionNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		calculator.setInitialResult(40);
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);
		
		assertSame(calculatorTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(1, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(calculator));
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(AttributeChange.class, change0.getClass());
		AttributeChange attributeChange = (AttributeChange)change0;
		assertEquals(new Integer(20), attributeChange.getPreValue());
		assertEquals(new Integer(40), attributeChange.getPostValue());
	}

	public void testSettingOneToOneReferenceForTransactionNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		customer.setEmailAddress(emailAddress);
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);
		
		assertSame(customerTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(2, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(customer));
		assertTrue(change.getModifiedPojos().contains(emailAddress));
		
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(OneToOneReferenceChange.class, change0.getClass());
		OneToOneReferenceChange oneToOneChange = (OneToOneReferenceChange)change0;
		assertEquals(null, oneToOneChange.getPreValue());
		assertEquals(emailAddress, oneToOneChange.getPostValue());
	}

	public void testClearingOneToOneReferenceForTransactionNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		customer.setEmailAddress(null);
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);
		
		assertSame(customerTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(1, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(customer));
		
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(OneToOneReferenceChange.class, change0.getClass());
		OneToOneReferenceChange oneToOneChange = (OneToOneReferenceChange)change0;
		assertEquals(null, oneToOneChange.getPostValue());
	}

	public void testAddingToCollectionReferenceForTransactionNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		customer.addToOrders(order);
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);

		assertSame(customerTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(2, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(customer));
		assertTrue(change.getModifiedPojos().contains(order));
		
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(AddToCollectionChange.class, change0.getClass());
		AddToCollectionChange addToCollectionChange = (AddToCollectionChange)change0;
		assertEquals(order, addToCollectionChange.getReferencedObject());
		assertEquals("orders", addToCollectionChange.getDescription());
	}

	public void testRemovingFromCollectionReferenceForTransactionNotifiesListenersOfAddingChange() {
		// add the order first, then reset the listener
		customer.addToOrders(order);
		listener.addedChangeEvent = null;
		
		// now do the remove
		assertNull(listener.addedChangeEvent);
		customer.removeFromOrders(order);
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);

		assertSame(customerTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(2, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(customer));
		assertTrue(change.getModifiedPojos().contains(order));
		
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(RemoveFromCollectionChange.class, change0.getClass());
		RemoveFromCollectionChange removeFromCollectionChange = (RemoveFromCollectionChange)change0;
		assertEquals(order, removeFromCollectionChange.getReferencedObject());
		assertEquals("orders", removeFromCollectionChange.getDescription());
	}

	/**
	 * Not implemented.
	 */
	public void incompletetestRemovingFromCollectionReferenceUsingIteratorForTransactionNotifiesListenersOfAddingChange() {
		
	}

	public void testDeletingAnObjectForTransactionNotifiesListenersOfAddingChange() {
		assertNull(listener.addedChangeEvent);
		calculator.delete();
		TransactionEvent event = listener.addedChangeEvent;
		assertNotNull(event);

		assertSame(calculatorTransaction, event.getTransaction());
		IChange change = event.getChange();
		assertNotNull(change);
		assertEquals(1, change.getModifiedPojos().size());
		assertTrue(change.getModifiedPojos().contains(calculator));
		
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size());
		IChange change0 = interaction.get(0);
		assertSame(DeletionChange.class, change0.getClass());
		DeletionChange deletionChange = (DeletionChange)change0;
		// nothing additional to assert here.
	}

	public void testCommittingTransactionNotifiesTransactionListeners() {
		assertNull(listener.committedEvent);
		calculatorTransaction.commit();
		TransactionEvent event = listener.committedEvent;
		assertNotNull(event);
		assertNull(event.getChange());
		assertSame(calculatorTransaction, event.getTransaction());
	}

	public void testUndoingChangeInTransactionNotifiesTransactionListeners() {
		// have already done one change in order to set up xactn; do one further
		// so that performing an undo does not discard the entire xactn.
		calculator.noop();

		assertNull(listener.undonePendingChangeEvent);
		calculatorTransaction.undoPendingChange();
		TransactionEvent event = listener.undonePendingChangeEvent;
		assertNotNull(event);
		assertSame(calculatorTransaction, event.getTransaction());
		
		IChange change = event.getChange();
		assertNotNull(change);
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(0, interaction.size()); // for the noop()
	}

	public void testDiscardingTransactionImplicitlyNotifiesTransactionListeners() {
		assertNull(listener.discardedEvent);
		assertNull(listener.undonePendingChangeEvent);
		calculatorTransaction.undoPendingChange();
		
		TransactionEvent discardedEvent = listener.discardedEvent;
		assertNotNull(discardedEvent);
		assertSame(calculatorTransaction, discardedEvent.getTransaction());
		assertNull(discardedEvent.getChange());
		
		// in addition, there will also be an undonePendingChangeEvent...
		TransactionEvent undonePendingChangeEvent = listener.undonePendingChangeEvent;
		assertNotNull(undonePendingChangeEvent);
		assertSame(calculatorTransaction, undonePendingChangeEvent.getTransaction());
		
		IChange change = undonePendingChangeEvent.getChange();
		assertNotNull(change);
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size()); // for the setInitialResult in the fixture
		IChange change0 = interaction.get(0);
		assertSame(AttributeChange.class, change0.getClass());
		AttributeChange attributeChange = (AttributeChange)change0;
		assertEquals(new Integer(0), attributeChange.getPreValue());
		assertEquals(new Integer(20), attributeChange.getPostValue());
	}

	public void testDiscardingTransactionExplicitlyNotifiesTransactionListeners() {
		assertNull(listener.discardedEvent);
		assertNull(listener.undonePendingChangeEvent);
		calculatorTransaction.discard();
		
		TransactionEvent discardedEvent = listener.discardedEvent;
		assertNotNull(discardedEvent);
		assertSame(calculatorTransaction, discardedEvent.getTransaction());
		assertNull(discardedEvent.getChange());
		
		// for an explicit discard, there will be no undonePendingChangeEvent...
		assertNull(listener.undonePendingChangeEvent);
	}

	public void testRedoingChangeInTransactionExplicitlyNotifiesTransactionListeners() {
		// have already done one change in order to set up xactn; do one further
		// so that performing an undo does not discard the entire xactn.
		calculator.setInitialResult(40);
		// undo, so that we can redo
		calculatorTransaction.undoPendingChange();

		// test start here
		assertNull(listener.redonePendingChangeEvent);
		calculatorTransaction.redoPendingChange();
		TransactionEvent event = listener.redonePendingChangeEvent;
		assertNotNull(event);
		assertSame(calculatorTransaction, event.getTransaction());
		
		IChange change = event.getChange();
		assertNotNull(change);
		assertSame(Interaction.class, change.getClass());
		Interaction interaction = (Interaction)change;
		assertEquals(1, interaction.size()); // for the setInitialResult
		IChange change0 = interaction.get(0);
		assertSame(AttributeChange.class, change0.getClass());
		AttributeChange attributeChange = (AttributeChange)change0;
		assertEquals(new Integer(20), attributeChange.getPreValue());
		assertEquals(new Integer(40), attributeChange.getPostValue());
	}

	public void testRedoingChangesInTransactionExplicitlyNotifiesTransactionListenersOfRedonePendingChanges() {
		// have already done one change in order to set up xactn; do two further
		// so that performing two undoes does not discard the entire xactn.
		calculator.setInitialResult(40);
		calculator.setInitialResult(60);
		// undo both, so that we can redo changes
		calculatorTransaction.undoPendingChange();
		calculatorTransaction.undoPendingChange();

		// test start here
		assertNull(listener.redonePendingChangesEvent);
		calculatorTransaction.redoPendingChanges();
		TransactionEvent event = listener.redonePendingChangesEvent;
		assertNotNull(event);
		assertSame(calculatorTransaction, event.getTransaction());
		
		IChange change = event.getChange();
		assertNull(change);
	}

	public void testRedoingChangesInTransactionExplicitlyNotifiesTransactionListenersOfRedonePendingChangeMultipleTimes() {
		// have already done one change in order to set up xactn; do two further
		// so that performing two undoes does not discard the entire xactn.
		calculator.setInitialResult(40);
		calculator.setInitialResult(60);
		// undo both, so that we can redo changes
		calculatorTransaction.undoPendingChange();
		calculatorTransaction.undoPendingChange();

		// test start here
		assertEquals(0, listener.redonePendingChangeEventList.size());
		calculatorTransaction.redoPendingChanges();
		assertEquals(2, listener.redonePendingChangeEventList.size());

		TransactionEvent event1 = listener.redonePendingChangeEventList.get(0);
		assertNotNull(event1);
		assertSame(calculatorTransaction, event1.getTransaction());
		IChange change1 = event1.getChange();
		assertNotNull(change1);
		assertSame(Interaction.class, change1.getClass());
		Interaction interaction1 = (Interaction)change1;
		assertEquals(1, interaction1.size()); // for the setInitialResult
		IChange change1_0 = interaction1.get(0);
		assertSame(AttributeChange.class, change1_0.getClass());
		AttributeChange attributeChange1_0 = (AttributeChange)change1_0;
		assertEquals(new Integer(20), attributeChange1_0.getPreValue());
		assertEquals(new Integer(40), attributeChange1_0.getPostValue());

		TransactionEvent event2 = listener.redonePendingChangeEventList.get(1);
		assertNotNull(event2);
		assertSame(calculatorTransaction, event2.getTransaction());
		IChange change2 = event2.getChange();
		assertNotNull(change2);
		assertSame(Interaction.class, change2.getClass());
		Interaction interaction2 = (Interaction)change2;
		assertEquals(1, interaction2.size()); // for the setInitialResult
		IChange change2_0 = interaction2.get(0);
		assertSame(AttributeChange.class, change2_0.getClass());
		AttributeChange attributeChange2_0 = (AttributeChange)change2_0;
		assertEquals(new Integer(40), attributeChange2_0.getPreValue());
		assertEquals(new Integer(60), attributeChange2_0.getPostValue());
	}

	public void incompletetestReversingTransactionNotifiesTransactionListeners() {
		assertNull(listener.reversedEvent);
		
	}

	public void incompletetestReapplyingTransactionNotifiesTransactionListeners() {
		assertNull(listener.reappliedEvent);
		
	}

}
