package edu.utdallas.seers.bre.javabre.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaFileInfo {

	private File file;
	private List<TypeDeclaration> classes = new ArrayList<TypeDeclaration>();
	private List<FieldDeclaration> constFields = new ArrayList<FieldDeclaration>();
	private List<IfStatement> ifStmts = new ArrayList<IfStatement>();
	private CompilationUnit cu;
	
	private List<MethodInvocation> methodInvoc=new ArrayList<MethodInvocation>();

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<TypeDeclaration> getClasses() {
		return classes;
	}

	public void setClasses(List<TypeDeclaration> classes) {
		this.classes = classes;
	}

	public List<FieldDeclaration> getConstFields() {
		return constFields;
	}

	public void setConstFields(List<FieldDeclaration> constFields) {
		this.constFields = constFields;
	}

	@Override
	public String toString() {
		return "JavaFileInfo [file=" + file + ", classes=" + classes
				+ ", constFields=" + constFields + "]";
	}

	public void setCompilUnit(CompilationUnit cu) {
		this.cu = cu;
	}

	public CompilationUnit getCompilUnit() {
		return this.cu;
	}

	public List<IfStatement> getIfStmts() {
		return this.ifStmts;
	}

	public void setIfStmts(List<IfStatement> ifStmts) {
		this.ifStmts = ifStmts;
	}

	public List<MethodInvocation> getMethodInvoc() {
		return methodInvoc;
	}

	public void setMethodInvoc(List<MethodInvocation> methodInvoc) {
		this.methodInvoc = methodInvoc;
	}

}
