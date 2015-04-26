package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class EnumExtractor implements RuleExtractor {

	private Set<Term> businessTerms;
	private Set<Term> sysTerms;
	final String TEMPLATE = "A {0} is by definition one of the following: {1}";

	public EnumExtractor(Set<Term> businessTerms, Set<Term> sysTerms) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<BusinessRule> extract(JavaFileInfo info) {
		
		List<EnumDeclaration> constFields = info.getEnumStmts();
		List<BusinessRule> rules = new ArrayList<BusinessRule>();


		for (EnumDeclaration constField : constFields) {
			try {
				String term1 = constField.getName().toString();

				if ("serialVersionUID".equalsIgnoreCase(term1)) {
					continue;
				}

				if (Utils.isInValidIdent(term1, businessTerms, this.sysTerms)) {
					continue;
				}
				
				//parse case statements
				String cases = "[";
				List enumCases = constField.enumConstants();
				for(int i = 0; i < enumCases.size(); i++){
					
					EnumConstantDeclaration ec = (EnumConstantDeclaration) enumCases.get(i);
					
					if((i+1) < enumCases.size()){
						cases += ec.getName().toString() + ", ";
					}else{
						cases += ec.getName().toString() + "]";
					}
				}
				
				String brText = Utils.replaceTemplate(TEMPLATE, new String[] {
						Utils.bracketizeStr(term1), cases });

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
}
