package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class CategorizationEnumExtractor {

	final String[] templates = { "A {0} is by definition either {1} or {2}",
			"A {0} is by definition one of the following: {1}",
			"The {0} of a {1} is by definition one of the following: {2}." };

	public List<BusinessRule> extract(
			HashMap<TypeDcl, List<TypeDeclaration>> subclasses) {
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		Set<Entry<TypeDcl, List<TypeDeclaration>>> entrySet = subclasses
				.entrySet();

		for (Entry<TypeDcl, List<TypeDeclaration>> entry : entrySet) {
			TypeDcl superClass = entry.getKey();

			if (superClass.getType().equals(TypeDcl.TypeDclType.INTERFACE)) {
				continue;
			}

			List<TypeDeclaration> subClasses = entry.getValue();

			if (subclasses.size() == 1) {
				continue;
			}

			int i = 1;
			if (subClasses.size() == 2) {
				i = 0;
			}
			String[] values = getValues(superClass, subClasses);
			String tmpl = templates[i];

			String brText = Utils.replaceTemplate(tmpl, values);

			JavaFileInfo info = superClass.getFileInfo();
			// CompilationUnit cu = info.getCompilUnit();
			BusinessRule rule = new BusinessRule(brText, info.getFile(), 1);
			rules.add(rule);

		}

		return rules;
	}

	private String[] getValues(TypeDcl superClass,
			List<TypeDeclaration> subClasses) {

		if (subClasses.size() == 2) {
			return new String[] { superClass.getName(),
					subClasses.get(0).getName().toString(),
					subClasses.get(1).getName().toString() };
		}

		final String COMMA = ", ";
		StringBuffer buf = new StringBuffer();
		for (TypeDeclaration tDcl : subClasses) {
			buf.append(tDcl.getName().toString());
			buf.append(COMMA);
		}

		int lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), "");

		lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), " or ");

		return new String[] { superClass.getName(), buf.toString() };
	}
}
