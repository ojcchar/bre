package edu.utdallas.seers.bre.javabre.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.Token;

public class GeneralVisitor extends ASTVisitor {

	private JavaFileInfo fileInfo;
	private List<String> businessTerms;

	public GeneralVisitor(List<String> businessTerms) {
		fileInfo = new JavaFileInfo();
		this.businessTerms = businessTerms;
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
		
		String className1 = node.getName().toString();

		boolean contains = false;
		for (String term : businessTerms) {

			if (!className1.toLowerCase().contains(term.toLowerCase())) {
				List<Token> tokensCl = NLPProcessor.getInstance().processText(
						className1);

				List<Token> tokensTerm = NLPProcessor.getInstance()
						.processText(term);

				for (Token tokenCl : tokensCl) {

					for (Token tokenTerm : tokensTerm) {
						if (tokenCl.getLemma().equals(tokenTerm.getLemma())) {
							contains = true;
							break;
						}
					}

					if (contains) {
						break;
					}

				}

			} else {
				contains = true;
			}

		}

		if (!contains) {
			return false;
		}

		fileInfo.getClasses().add(node);
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
