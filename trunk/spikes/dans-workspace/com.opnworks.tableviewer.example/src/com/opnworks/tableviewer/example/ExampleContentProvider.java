package com.opnworks.tableviewer.example;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * InnerClass that acts as a proxy for the ExampleTaskList 
 * providing content for the Table. It implements the ITaskListViewer 
 * interface since it must register changeListeners with the 
 * ExampleTaskList 
 */
class ExampleContentProvider implements IStructuredContentProvider, ITaskListViewer {
	/**
	 * 
	 */
	private final TableViewerExample example;

	/**
	 * @param example
	 */
	ExampleContentProvider(TableViewerExample example) {
		this.example = example;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (newInput != null)
			((ExampleTaskList) newInput).addChangeListener(this);
		if (oldInput != null)
			((ExampleTaskList) oldInput).removeChangeListener(this);
	}

	public void dispose() {
		this.example.taskList.removeChangeListener(this);
	}

	// Return the tasks as an array of Objects
	public Object[] getElements(Object parent) {
		return this.example.taskList.getTasks().toArray();
	}

	/* (non-Javadoc)
	 * @see ITaskListViewer#addTask(ExampleTask)
	 */
	public void addTask(ExampleTask task) {
		this.example.tableViewer.add(task);
	}

	/* (non-Javadoc)
	 * @see ITaskListViewer#removeTask(ExampleTask)
	 */
	public void removeTask(ExampleTask task) {
		this.example.tableViewer.remove(task);			
	}

	/* (non-Javadoc)
	 * @see ITaskListViewer#updateTask(ExampleTask)
	 */
	public void updateTask(ExampleTask task) {
		this.example.tableViewer.update(task, null);	
	}
}