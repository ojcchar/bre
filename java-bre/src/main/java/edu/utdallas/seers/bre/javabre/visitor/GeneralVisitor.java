package edu.utdallas.seers.bre.javabre.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;

public class GeneralVisitor extends ASTVisitor {

	private JavaFileInfo fileInfo;

	public GeneralVisitor() {
		fileInfo = new JavaFileInfo();
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		int modifiers = node.getModifiers();
		boolean final1 = Modifier.isFinal(modifiers);
		if (final1) {
			fileInfo.getConstFields().add(node);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		fileInfo.getClasses().add(node);
		return super.visit(node);
	}

	public JavaFileInfo getFileInfo() {
		return fileInfo;
	}

}
