package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
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
			List fragments = constField.fragments();
			VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments
					.get(0);

			String term1 = frag.getName().toString();
			String literal1 = frag.getInitializer().toString();

			String brText = Utils.replaceTemplate(template, new String[] {
					term1, literal1 });

			CompilationUnit cu = info.getCompilUnit();
			BusinessRule rule = new BusinessRule(brText);
			
			rule.addLocation(info.getFile(),
					cu.getLineNumber(constField.getStartPosition()));
			rules.add(rule);

		}

		return rules;
	}

}
