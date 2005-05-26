package de.berlios.rcpviewer.ast.compiletime.util;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.compiletime.util.IdeFacade;

public class TestIdeFacade extends AbstractTestCase {

	private IdeFacade ideFacade;
	protected void setUp() throws Exception {
		super.setUp();
		ideFacade = new IdeFacade();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanOpenFile() {
		IFile file = 
			ideFacade.loadFileFor(
					"de.berlios.rcpviewer.domain.fixture", 
					"fixture",
					"de/berlios/rcpviewer/domain", 
					"Department.java");
		assertNotNull(file);
	}
	
	public void testCanObtainCompilationUnit() {
		ICompilationUnit compilationUnit =
				ideFacade.loadCompilationUnitFor(
						"de.berlios.rcpviewer.domain.fixture", 
						"fixture",
						"de/berlios/rcpviewer/domain", 
						"Department.java");
		assertNotNull(compilationUnit);
		IType departmentType = compilationUnit.getType("Department");
		assertNotNull(departmentType);
		assertEquals("de.berlios.rcpviewer.domain.Department", 
				departmentType.getFullyQualifiedName());
	}

	
	public void testCanObtainTypeDeclaration() {
		TypeDeclaration typeDeclaration =
				ideFacade.loadTypeDeclarationFor(
						"de.berlios.rcpviewer.domain.fixture", 
						"fixture",
						"de/berlios/rcpviewer/domain", 
						"Department.java");
		assertNotNull(typeDeclaration);
		ChildListPropertyDescriptor modifiersProperty = 
			typeDeclaration.getModifiersProperty();
		System.out.println(modifiersProperty.getElementType());
		List modifiers = (List)typeDeclaration.getStructuralProperty(modifiersProperty);
		System.out.println(modifiers);
		for(Object o: modifiers) {
			System.out.print(o+":");
			if (o instanceof Modifier) {
				Modifier modifier = (Modifier)o;
				System.out.println(Modifier.isPublic(modifier.getFlags()));
			}
			if (o instanceof MarkerAnnotation) {
				MarkerAnnotation ma = (MarkerAnnotation)o;
				System.out.println(ma.getTypeName());
			}
//			System.out.println(o.getClass());
		}
	}

}
