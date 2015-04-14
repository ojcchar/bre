package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.MaxMin;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.InFixExprVisitor;

public class ValidValExtractor implements RuleExtractor {

	private static final String[] templates = {
			"A {0} is by definition at least {1} and at most {2}",
			"A {0} is by definition at least {1}",
			"A {0} is by definition at most {1}" };

	public List<BusinessRule> extract(JavaFileInfo info) {
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		List<IfStatement> ifStmts = info.getIfStmts();

		for (IfStatement ifStmt : ifStmts) {

			Expression expression = ifStmt.getExpression();

			InFixExprVisitor inFixExprVisitor = new InFixExprVisitor();
			expression.accept(inFixExprVisitor);

			HashMap<String, MaxMin> varsValues = inFixExprVisitor
					.getVarsValues();

			Set<Entry<String, MaxMin>> entrySet = varsValues.entrySet();
			for (Entry<String, MaxMin> entry : entrySet) {

				int i = 0;
				MaxMin value = entry.getValue();
				String[] values = null;
				String term0 = entry.getKey();
				String nlTerm = Utils.bracketizeStr(term0);
				if (value.min == null || value.max == null) {
					if (value.max == null) {
						i = 1;
						values = new String[] { nlTerm, value.min };
					} else {
						i = 2;
						values = new String[] { nlTerm, value.max };
					}
				} else {
					values = new String[] { nlTerm, value.min, value.max };
				}

				String brText = Utils.replaceTemplate(templates[i], values);
				BusinessRule rule = new BusinessRule(brText,
						BusinessRule.RuleType.VALID_VALUE);
				rule.addLocation(info.getFile(), info.getCompilUnit()
						.getLineNumber(ifStmt.getStartPosition()));
				rules.add(rule);
			}

		}

		return rules;
	}

}
