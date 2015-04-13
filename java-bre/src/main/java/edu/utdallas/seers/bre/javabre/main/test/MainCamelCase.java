package edu.utdallas.seers.bre.javabre.main.test;

import com.google.common.base.CaseFormat;

public class MainCamelCase {

	public static void main(String[] args) {

		String s1 = "PDFLoader";
		String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s1);

		System.out.println(s);

		String[] tests = {
				"lowercase", // [lowercase]
				"Class", // [Class]
				"MyClass", // [My Class]
				"HTML", // [HTML]
				"PDFLoader", // [PDF Loader]
				"AString", // [A String]
				"SimpleXMLParser", // [Simple XML Parser]
				"GL11Version", // [GL 11 Version]
				"99Bottles", // [99 Bottles]
				"May5", // [May 5]
				"BFG9000", // [BFG 9000]
				"helloTest", "hello test",
				"PDFLoaderSimpleXMLParserGL11VersionBFG9000" };
		for (String test : tests) {
			System.out.println("[" + splitCamelCase(test) + "]");
		}
	}

	static String splitCamelCase(String s) {
		String regex = String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])");
		return s.replaceAll(regex, " ");
	}

}
