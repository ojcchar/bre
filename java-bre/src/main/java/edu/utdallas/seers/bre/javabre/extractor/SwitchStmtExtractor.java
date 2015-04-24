package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
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

public class SwitchStmtExtractor implements RuleExtractor {

	private Set<Term> businessTerms;
	private Set<Term> sysTerms;
	final String TEMPLATE = "A {0} is by definition one of the following: {1}";

	public SwitchStmtExtractor(Set<Term> businessTerms, Set<Term> sysTerms) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<BusinessRule> extract(JavaFileInfo info) {
		
		List<SwitchStatement> constFields = info.getSwitchStmts();
		List<BusinessRule> rules = new ArrayList<BusinessRule>();


		for (SwitchStatement constField : constFields) {
			try {
				String term1 = constFields.get(0).getExpression().toString();

				if ("serialVersionUID".equalsIgnoreCase(term1)) {
					continue;
				}

				if (Utils.isInValidIdent(term1, businessTerms, this.sysTerms)) {
					continue;
				}
				
				//parse case statements
				String cases = "[";
				List switchCases = constFields.get(0).statements();
				for(int i = 0; i < switchCases.size(); i++){
					SwitchCase vdf;
					try{
						vdf = (SwitchCase)switchCases.get(i);
					}catch(Exception e){
						continue;
					}
					String ss = "";
					try{
						ss = vdf.getExpression().toString();
					}catch(Exception e){
						ss = "default";
					}
					
					if((i+1) < switchCases.size()){
						cases += ss + ", ";
					}else{
						cases += ss + "]";
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
