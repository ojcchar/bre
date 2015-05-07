package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule.RuleType;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class CategEnumConstExtractor implements RuleExtractor {

	private static final String TEMPLATE = "A {0} is by definition one of the following: {1}.";
	private Set<Term> sysTerms;
	private Set<Term> businessTerms;
	private List<FieldDeclaration> constsRules = new ArrayList<FieldDeclaration>();

	public CategEnumConstExtractor(Set<Term> businessTerms, Set<Term> sysTerms) {
		this.sysTerms = sysTerms;
		this.businessTerms = businessTerms;
	}

	@Override
	public List<BusinessRule> extract(JavaFileInfo info) {

		List<FieldDeclaration> constFields = info.getConstFields();
		CompilationUnit unit = info.getCompilUnit();

		// HEURISTIC
		HashMap<String, List<FieldDeclaration>> constGroups = getConstantGroups(
				constFields, unit);
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		Set<String> keySet = constGroups.keySet();
		for (String key : keySet) {
			List<FieldDeclaration> consts = constGroups.get(key);

			boolean b = isInvalidGroup(consts);
			if (b) {
				continue;
			}

			HashMap<String, Integer> freq = new HashMap<String, Integer>();
			for (int i = 0; i < consts.size(); i++) {
				for (int j = i + 1; j < consts.size(); j++) {

					String text1 = ((VariableDeclarationFragment) consts.get(i)
							.fragments().get(0)).getName().toString();
					String text2 = ((VariableDeclarationFragment) consts.get(j)
							.fragments().get(0)).getName().toString();
					String lcs = getLongestCommonSubstring(text1, text2);

					Integer f = freq.get(lcs);
					if (f == null) {
						f = 0;
					}
					freq.put(lcs, ++f);

				}
			}

			String subStr = getCategory(freq, consts);

			if (subStr != null && subStr.trim().length() > 2) {

				this.constsRules.addAll(consts);

				String[] values = getValues(subStr, consts);

				if (values == null) {
					continue;
				}

				String brText = Utils.replaceTemplate(TEMPLATE, values);

				BusinessRule rule = new BusinessRule(brText,
						RuleType.CATEG_ENUMERATION);
				rule.addLocation(info.getFile(),
						unit.getLineNumber(consts.get(0).getStartPosition()));
				rules.add(rule);
			}

			// System.out.println(freq);

		}

		// System.out.println(rules);

		return rules;
	}

	private boolean isInvalidGroup(List<FieldDeclaration> consts) {
		int numInv = 0;

		for (FieldDeclaration cnt : consts) {
			String nameCnt = ((VariableDeclarationFragment) cnt.fragments()
					.get(0)).getName().toString();
			if (Utils.isInValidIdent(nameCnt, businessTerms, sysTerms)) {
				numInv++;
			}
		}
		// HEURISTIC
		boolean isInv = (numInv / consts.size()) > 0.8;
		return isInv;
	}

	private String getCategory(HashMap<String, Integer> freq,
			List<FieldDeclaration> consts) {
		Entry<String, Integer> maxEntry = null;

		for (Entry<String, Integer> entry : freq.entrySet()) {
			if (maxEntry == null
					|| entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		// HEURISTIC
		if ((2 * maxEntry.getValue())
				/ (Math.pow(consts.size(), 2) - consts.size()) > 0.8) {
			return maxEntry.getKey();
		}
		// if (maxEntry.getValue() / consts.size() > 0.8) {
		// return maxEntry.getKey();
		// }

		return null;
	}

	// public LinkedHashMap<String, Integer> sortHashMapByValuesD(
	// HashMap<String, Integer> passedMap) {
	// List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
	// List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
	// Collections.sort(mapValues);
	// Collections.sort(mapKeys);
	//
	// LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String,
	// Integer>();
	//
	// Iterator<Integer> valueIt = mapValues.iterator();
	// while (valueIt.hasNext()) {
	// Integer val = valueIt.next();
	// Iterator<String> keyIt = mapKeys.iterator();
	//
	// while (keyIt.hasNext()) {
	// String key = keyIt.next();
	// String comp1 = passedMap.get(key).toString();
	// String comp2 = val.toString();
	//
	// if (comp1.equals(comp2)) {
	// mapKeys.remove(key);
	// sortedMap.put(key, val);
	// break;
	// }
	//
	// }
	//
	// }
	// return sortedMap;
	// }

	private String[] getValues(String categStr, List<FieldDeclaration> consts) {

		String term0 = Utils.bracketizeStr(categStr);

		if (Utils.isInValidIdent(term0, businessTerms, sysTerms)) {
			return null;
		}

		final String COMMA = ", ";
		StringBuffer buf = new StringBuffer();
		for (FieldDeclaration tDcl : consts) {
			SimpleName clName = ((VariableDeclarationFragment) tDcl.fragments()
					.get(0)).getName();

			String subName = clName.toString();
			if (subName.contains(categStr)) {
				subName = subName.replace(categStr, "");
				if (!subName.isEmpty()) {
					buf.append(Utils.bracketizeStr(subName));
					buf.append(COMMA);
				}
			}
		}

		int lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), "");

		lastIndexOf = buf.lastIndexOf(COMMA);
		buf.replace(lastIndexOf, lastIndexOf + COMMA.length(), " or ");

		return new String[] { term0, buf.toString() };
	}

	private String getLongestCommonSubstring(String text1, String text2) {
		// read in two string from two files
		int N1 = text1.length();
		// int N2 = text2.length();

		// concatenate two string with intervening '\1'
		String text = text1 + '\1' + text2;
		int N = text.length();

		// compute suffix array of concatenated text
		SuffixArray suffix = new SuffixArray(text);

		// search for longest common substring
		String lcs = "";
		for (int i = 1; i < N; i++) {

			// adjacent suffixes both from first text string
			if (suffix.index(i) < N1 && suffix.index(i - 1) < N1)
				continue;

			// adjacent suffixes both from secondt text string
			if (suffix.index(i) > N1 && suffix.index(i - 1) > N1)
				continue;

			// check if adjacent suffixes longer common substring
			int length = suffix.lcp(i);
			if (length > lcs.length()) {
				lcs = text.substring(suffix.index(i), suffix.index(i) + length);
			}
		}

		return lcs;
	}

	private HashMap<String, List<FieldDeclaration>> getConstantGroups(
			List<FieldDeclaration> constFields, CompilationUnit unit) {
		HashMap<String, List<FieldDeclaration>> constGroups = new HashMap<String, List<FieldDeclaration>>();

		int numConsts = constFields.size();
		for (int i = 0; i < numConsts;) {

			FieldDeclaration currConst = constFields.get(i);
			int currLine = unit.getLineNumber(currConst.getStartPosition());
			int endLine = unit.getLineNumber(currConst.getStartPosition()
					+ currConst.getLength());
			int prevLine = currLine;

			// System.out.println("curr: " + currConst);
			int numGroup = 1;
			int idx = -1;

			// analyze the next fields
			for (int j = i + 1; j < numConsts; j++) {

				FieldDeclaration nextConst = constFields.get(j);
				int nextStartLine = unit.getLineNumber(nextConst
						.getStartPosition());

				int stmtLength = endLine - prevLine + 1;

				// detect if there is a group
				if ((prevLine + stmtLength) == nextStartLine) {
					prevLine = nextStartLine;
					endLine = unit.getLineNumber(nextConst.getStartPosition()
							+ nextConst.getLength());
					numGroup++;
				} else {
					idx = j;
					break;
				}
			}

			// get the group
			if (currLine < prevLine) {
				List<FieldDeclaration> subList = constFields.subList(i, i
						+ numGroup);
				constGroups.put(String.valueOf(i), subList);
			}

			// update the i index
			if (idx == i) {
				i++;
			} else {
				if (idx == -1 && (numGroup + i) == numConsts) {
					i = numGroup + i;
				} else if (idx == -1) {
					i++;
				} else {
					i = idx;
				}
			}
		}
		return constGroups;
	}

	public List<FieldDeclaration> getConstsRules() {
		return constsRules;
	}

}
