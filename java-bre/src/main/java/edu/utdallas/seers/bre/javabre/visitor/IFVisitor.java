package edu.utdallas.seers.bre.javabre.visitor;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;

import edu.utdallas.seers.bre.javabre.entity.IFstmt;

public class IFVisitor extends ASTVisitor {

	private CompilationUnit cu;
	private File file;

	private Set<String> bVars;

	// file -> [ var -> if_statement ]
	private HashMap<String, Set<IFstmt>> ifStmts;

	@Override
	public boolean visit(IfStatement node) {

		for (String bVar : bVars) {
			IFHelperVisitor vis = new IFHelperVisitor(bVar);
			node.accept(vis);
			boolean b = vis.useVar();
			if (b) {

				Set<IFstmt> ifs = ifStmts.get(bVar);
				if (ifs == null) {
					ifs = new HashSet<IFstmt>();
					ifStmts.put(bVar, ifs);
				}

				IFstmt ifSt = new IFstmt(node.getExpression().toString()
						.replace("\n", "\\n"), node.getThenStatement()
						.toString().replace("\n", "\\n"),
						cu.getLineNumber(node.getStartPosition()));
				ifs.add(ifSt);
			}
		}

		return super.visit(node);
	}

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Set<String> getbVars() {
		return bVars;
	}

	public void setbVars(Set<String> bVars) {
		this.bVars = bVars;
		ifStmts = new HashMap<String, Set<IFstmt>>();
	}

	public HashMap<String, Set<IFstmt>> getIfStmts() {
		return ifStmts;
	}

	public void setIfStmts(HashMap<String, Set<IFstmt>> ifStmts) {
		this.ifStmts = ifStmts;
	}

}
