package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class SymbolicLiteralExtractor implements RuleExtractor {

	private Set<Term> businessTerms;
	private Set<Term> sysTerms;
	private List<FieldDeclaration> constToOmit;

	public SymbolicLiteralExtractor(Set<Term> businessTerms,
			Set<Term> sysTerms, List<FieldDeclaration> constToOmit) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
		this.constToOmit = constToOmit;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<BusinessRule> extract(JavaFileInfo info) {

		final String template = "A {0} is by definition {1}";

		List<FieldDeclaration> constFields = info.getConstFields();
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		for (FieldDeclaration constField : constFields) {
			try {

				// if (constToOmit.contains(constField)) {
				// continue;
				// }

				List fragments = constField.fragments();
				VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments
						.get(0);

				String term0 = frag.getName().toString();

				if (omitConstant(term0)) {
					continue;
				}

				if ("serialVersionUID".equalsIgnoreCase(term0)) {
					continue;
				}

				if (Utils.isInValidIdent(term0, businessTerms, this.sysTerms)) {
					continue;
				}

				Expression initializer = frag.getInitializer();
				if (initializer == null) {
					continue;
				}

				String simpleName = initializer.getClass().getSimpleName();

				if (!simpleName.toLowerCase().contains("literal")) {
					continue;
				}

				String literal1 = initializer.toString();

				if (initializer instanceof StringLiteral) {
					if (Utils.isTermContained(literal1, this.sysTerms, false)) {
						continue;
					}
				}

				List<Token> processText = NLPProcessor.getInstance()
						.processText(term0, true);
				// System.out.println(processText);
				if (Utils.isTermContained(
						literal1,
						new HashSet<Term>(Arrays.asList(new Term[] { new Term(
								processText) })), false)) {
					continue;
				}

				String brText = Utils.replaceTemplate(template, new String[] {
						Utils.bracketizeStr(term0), literal1 });

				CompilationUnit cu = info.getCompilUnit();
				BusinessRule rule = new BusinessRule(brText,
						BusinessRule.RuleType.SYMBOL_LITERAL);

				rule.addLocation(info.getFile(),
						cu.getLineNumber(constField.getStartPosition()));
				rules.add(rule);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(constField);
			}

		}

		return rules;
	}

	@SuppressWarnings("rawtypes")
	private boolean omitConstant(String nameConst) {

		for (FieldDeclaration constField : constToOmit) {
			List fragments = constField.fragments();
			VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments
					.get(0);

			String term0 = frag.getName().toString();

			if (nameConst.equals(term0)) {
				return true;
			}
		}

		return false;
	}

}
