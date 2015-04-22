package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class CategorizationEnumExtractor {

	final String[] templates = { "A {0} is by definition either {1} or {2}",
			"A {0} is by definition one of the following: {1}",
			"The {0} of a {1} is by definition one of the following: {2}." };
	private Set<Term> businessTerms;
	private Set<Term> sysTerms;

	public CategorizationEnumExtractor(Set<Term> businessTerms,
			Set<Term> sysTerms) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
	}

	public List<BusinessRule> extract(
			HashMap<TypeDcl, List<TypeDeclaration>> classHierarchy,
			HashMap<String, JavaFileInfo> classesInfo) {
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		Set<Entry<TypeDcl, List<TypeDeclaration>>> entrySet = classHierarchy
				.entrySet();

		for (Entry<TypeDcl, List<TypeDeclaration>> entry : entrySet) {
			TypeDcl superClass = entry.getKey();

			// if (superClass.getType().equals(TypeDcl.TypeDclType.INTERFACE)) {
			// continue;
			// }

			if (Utils.isInValidIdent(superClass.getName(), businessTerms,
					this.sysTerms)) {
				continue;
			}
			
			List<TypeDeclaration> subClasses = entry.getValue();
			if (subClasses.size() == 1) {
				continue;
			}

			int i = 1;
			if (subClasses.size() == 2) {
				i = 0;
			}
			String[] values = getValues(superClass, subClasses);
			String tmpl = templates[i];

			String brText = Utils.replaceTemplate(tmpl, values);

			BusinessRule rule = new BusinessRule(brText,
					BusinessRule.RuleType.CATEG_ENUMERATION);
			for (TypeDeclaration subClass : subClasses) {
				JavaFileInfo info = classesInfo.get(subClass.resolveBinding()
						.getQualifiedName());
				rule.addLocation(info.getFile(), info.getCompilUnit()
						.getLineNumber(subClass.getStartPosition()));
			}

			rules.add(rule);

		}

		return rules;
	}

	private String[] getValues(TypeDcl superClass,
			List<TypeDeclaration> subClasses) {

		String term0 = Utils.bracketizeStr(superClass.getName());

		if (subClasses.size() == 2) {
			return new String[] {
					term0,
					Utils.bracketizeStr(subClasses.get(0).getName().toString()),
					Utils.bracketizeStr(subClasses.get(1).getName().toString()) };
		}

		final String COMMA = ", ";
		StringBuffer buf = new StringBuffer();
		for (TypeDeclaration tDcl : subClasses) {
			SimpleName clName = tDcl.getName();

			String subName = clName.toString();
			buf.append(Utils.bracketizeStr(subName));
			buf.append(COMMA);
		}

		int lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), "");

		lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), " or ");

		return new String[] { term0, buf.toString() };
	}
}
