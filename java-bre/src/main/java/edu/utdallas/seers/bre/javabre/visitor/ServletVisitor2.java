package edu.utdallas.seers.bre.javabre.visitor;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;

public class ServletVisitor2 extends ASTVisitor {

	private JavaFileInfo fileInfo;
	private HashMap<String, List<String>> clsMethods;

	public ServletVisitor2(HashMap<String, List<String>> clsMethods) {
		this.clsMethods = clsMethods;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		String qualifiedName = node.resolveBinding().getQualifiedName();
		if (clsMethods.containsKey(qualifiedName)) {
			fileInfo = new JavaFileInfo();
			return true;
		}
		return false;

	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (fileInfo != null) {
			fileInfo.getMethodInvoc().add(node);
		}
		return super.visit(node);
	}

	public JavaFileInfo getFileInfo() {
		return fileInfo;
	}

}
