package edu.utdallas.seers.bre.javabre.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TestVisitor extends ASTVisitor {
	private CompilationUnit cu;
	Set<String> names = new HashSet<String>();

	public TestVisitor(CompilationUnit cu) {
		this.cu = cu;
	}

	// public boolean visit(VariableDeclarationFragment node) {
	// SimpleName name = node.getName();
	// this.names.add(name.getIdentifier());
	// System.out.println("Declaration of '" + name + "' at line "
	// + cu.getLineNumber(name.getStartPosition()));
	// return true;
	// }
	//
	// // public boolean visit(SimpleName node) {
	// // if (this.names.contains(node.getIdentifier())) {
	// // System.out.println("Usage of '" + node + "' at line "
	// // + cu.getLineNumber(node.getStartPosition()));
	// // }
	// // return true;
	// // }
	//
	// @Override
	// public boolean visit(EnumDeclaration node) {
	// SimpleName name = node.getName();
	// System.out.println("Enum '" + name + "' at line "
	// + cu.getLineNumber(name.getStartPosition()));
	// return true;
	// }
	//
	// @Override
	// public boolean visit(EnumConstantDeclaration node) {
	// SimpleName name = node.getName();
	// System.out.println("Enum const '" + name + "' at line "
	// + cu.getLineNumber(name.getStartPosition()));
	// return true;
	// }
	//
	// @Override
	// public boolean visit(IfStatement node) {
	// Expression name = node.getExpression();
	// System.out.println("if st '" + name + "' at line "
	// + cu.getLineNumber(name.getStartPosition()));
	// return true;
	// }
	//
	// @Override
	// public boolean visit(ForStatement node) {
	// Expression name = node.getExpression();
	// System.out.println("for st '" + name + "' at line "
	// + cu.getLineNumber(name.getStartPosition()));
	// return true;
	// }

	@Override
	public boolean visit(TypeDeclaration node) {
		SimpleName name = node.getName();

		System.out.println("class name '" + name + "' at line "
				+ cu.getLineNumber(name.getStartPosition()));
		Type superclassType = node.getSuperclassType();
		System.out.println("superclass name '" + superclassType + "' at line "
				+ cu.getLineNumber(superclassType.getStartPosition()));

		ITypeBinding resolveBinding = superclassType.resolveBinding();
		if (resolveBinding != null) {
			System.out.println("bind: " + resolveBinding.getQualifiedName());
		}
		return true;
	}
}
