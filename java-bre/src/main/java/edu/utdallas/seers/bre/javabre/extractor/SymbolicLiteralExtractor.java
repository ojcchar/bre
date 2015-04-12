package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class SymbolicLiteralExtractor implements RuleExtractor {

	@SuppressWarnings({ "rawtypes" })
	public List<BusinessRule> extract(JavaFileInfo info) {

		final String template = "A {0} is by definition {1}";

		List<FieldDeclaration> constFields = info.getConstFields();
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		for (FieldDeclaration constField : constFields) {
			try {
				List fragments = constField.fragments();
				VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments
						.get(0);

				String term1 = frag.getName().toString();

				Expression initializer = frag.getInitializer();
				if (initializer == null) {
					continue;
				}
				
				String simpleName = initializer.getClass().getSimpleName();
				
				if (!simpleName.toLowerCase().contains("literal")) {
					continue;
				}

				String literal1 = initializer.toString();

				String brText = Utils.replaceTemplate(template, new String[] {
						term1, literal1 });

				CompilationUnit cu = info.getCompilUnit();
				BusinessRule rule = new BusinessRule(brText,BusinessRule.RuleType.SYMBOL_LITERAL);

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

}
