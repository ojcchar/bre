package edu.utdallas.seers.bre.javabre.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;

public class ServletVisitor extends ASTVisitor {

	private JavaFileInfo fileInfo;

	public ServletVisitor() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(TypeDeclaration node) {

		Type superclType = node.getSuperclassType();

		//FIXME: CHANGE TO FALSE, TRUE ONLY FOR TESTING PURPOSES
		boolean isServlet = false;

		// super class
		if (superclType != null) {
			String name = superclType.resolveBinding().getName();
			if (name.toLowerCase().contains("servlet")) {
				isServlet = true;
			}
		}
		if (!isServlet) {
			List<Type> superintTypes = node.superInterfaceTypes();
			// super interfaces
			if (!superintTypes.isEmpty()) {
				for (Type type : superintTypes) {
					String name = type.resolveBinding().getName();
					if (name.toLowerCase().contains("servlet")) {
						isServlet = true;
						break;
					}
				}
			}

		}

		if (isServlet) {
			fileInfo = new JavaFileInfo();
			fileInfo.getClasses().add(node);

			return super.visit(node);
		} else {
			return false;
		}

	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (fileInfo != null) {
			fileInfo.getMethodInvoc().add(node);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {
		fileInfo.getIfStmts().add(node);
		return super.visit(node);
	}

	public JavaFileInfo getFileInfo() {
		return fileInfo;
	}

}
