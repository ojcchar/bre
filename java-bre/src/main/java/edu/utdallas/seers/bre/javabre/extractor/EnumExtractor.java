package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

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
			String term1 = constField.getName().toString();

			if (Utils.isInValidIdent(term1, businessTerms, this.sysTerms)) {
				continue;
			}

			List enumCases = constField.enumConstants();

			String[] values = getValues(term1, enumCases);
			String brText = Utils.replaceTemplate(TEMPLATE, values);

			CompilationUnit cu = info.getCompilUnit();
			BusinessRule rule = new BusinessRule(brText,
					BusinessRule.RuleType.CATEG_ENUMERATION);

			rule.addLocation(info.getFile(),
					cu.getLineNumber(constField.getStartPosition()));
			rules.add(rule);

		}

		return rules;
	}

	@SuppressWarnings("rawtypes")
	private String[] getValues(String subStr, List switchCases) {

		// ----------------------------

		String term0 = Utils.bracketizeStr(subStr);

		final String COMMA = ", ";
		StringBuffer buf = new StringBuffer();
		for (Object sw : switchCases) {

			if (sw instanceof EnumConstantDeclaration) {
				String subName = ((EnumConstantDeclaration) sw).getName()
						.toString();
				buf.append(Utils.bracketizeStr(subName));
				buf.append(COMMA);
			}
		}

		int lastIndexOf = buf.lastIndexOf(COMMA);
		if (lastIndexOf != -1) {
			buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), "");
		}

		lastIndexOf = buf.lastIndexOf(COMMA);
		if (lastIndexOf != -1) {
			buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), " or ");
		}

		return new String[] { term0, buf.toString() };
	}
}
