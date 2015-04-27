package edu.utdallas.seers.bre.javabre.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.MaxMin;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.InFixExprVisitor;

public class IfElseIfExtractor implements RuleExtractor {

	private Set<Term> businessTerms;
	private Set<Term> sysTerms;
	final String TEMPLATE = "A {0} is by definition one of the following: {1}";
	private static HashMap<String, LinkedList<String>> values = new HashMap<String, LinkedList<String>>();
	private static HashMap<String, Integer> lineNo = new HashMap<String, Integer>();

	public IfElseIfExtractor(Set<Term> businessTerms, Set<Term> sysTerms) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
	}

	IfStatement ifStmt;
	JavaFileInfo info;
	static boolean processed = false;
	@SuppressWarnings({ "rawtypes" })
	public List<BusinessRule> extract(JavaFileInfo info) {
		this.info = info;
		List<BusinessRule> rules = new ArrayList<BusinessRule>();

		List<IfStatement> ifStmts = info.getIfStmts();

		for (IfStatement ifStmt : ifStmts) {
			this.ifStmt = ifStmt;
			Expression expression = ifStmt.getExpression();
			parseExpression(expression);
		}
		if(!processed){
			for(String key : values.keySet()){
				
				String val2 = "[";
				LinkedList<String> ll = values.get(key);
				for(int i = 0; i < ll.size(); i++){
					if((i+1) < ll.size()){
						val2 += ll.get(i) + ", ";
					}else{
						val2 += ll.get(i) + "]";
					}
				}
				
				String brText = Utils.replaceTemplate(TEMPLATE, new String[] {
						Utils.bracketizeStr(key), val2 });
				BusinessRule rule = new BusinessRule(brText,
						BusinessRule.RuleType.CATEG_ENUMERATION);
				rule.addLocation(info.getFile(), lineNo.get(key));
				rules.add(rule);
			}
			processed = true;
		}
		return rules;
	}

	private void parseExpression(Expression expression) {

		//Case 1
		if(expression instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) expression;
			String name = "";
			try{
				name = mi.getExpression().toString();
				if (Utils.isInValidIdent(name, businessTerms, this.sysTerms)) {
					return;
				}
			}catch(NullPointerException e){
				return;
			}
			List<Expression> args = mi.arguments();
			for(Expression arg : args){
				if(arg instanceof NumberLiteral ||
						arg instanceof BooleanLiteral ||
						arg instanceof CharacterLiteral ||
						arg instanceof StringLiteral){
					if(!values.containsKey(name)){
						LinkedList<String> ll = new LinkedList<String>();
						ll.add(arg.toString());
						values.put(name, ll);
						lineNo.put(name, info.getCompilUnit()
			.getLineNumber(ifStmt.getStartPosition()));
					}else if(!values.get(name).contains(arg.toString())){
						LinkedList<String> ll = values.get(name);
						ll.add(arg.toString());
						values.put(name, ll);
					}
				}
			}
		}else
		//Case 2
		if(expression instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) expression;

			if(ie.getLeftOperand() instanceof SimpleName
					&& (ie.getRightOperand() instanceof NumberLiteral ||
							ie.getRightOperand() instanceof BooleanLiteral ||
							ie.getRightOperand() instanceof CharacterLiteral ||
							ie.getRightOperand() instanceof StringLiteral)){
				String name = ie.getLeftOperand().toString();
				if (Utils.isInValidIdent(name, businessTerms, this.sysTerms)) {
					return;
				}
				if(!values.containsKey(name)){
					LinkedList<String> ll = new LinkedList<String>();
					ll.add(ie.getRightOperand().toString());
					values.put(name, ll);
					lineNo.put(name, info.getCompilUnit()
							.getLineNumber(ifStmt.getStartPosition()));
				}else if(!values.get(name).contains(ie.getRightOperand().toString())){
					LinkedList<String> ll = values.get(name);
					ll.add(ie.getRightOperand().toString());
					values.put(name, ll);
				}
			}else if(ie.getRightOperand() instanceof SimpleName
					&& (ie.getLeftOperand() instanceof NumberLiteral ||
							ie.getLeftOperand() instanceof BooleanLiteral ||
							ie.getLeftOperand() instanceof CharacterLiteral ||
							ie.getLeftOperand() instanceof StringLiteral)){
				String name = ie.getRightOperand().toString();
				if (Utils.isInValidIdent(name, businessTerms, this.sysTerms)) {
					return;
				}
				if(!values.containsKey(name)){
					LinkedList<String> ll = new LinkedList<String>();
					ll.add(ie.getRightOperand().toString());
					values.put(name, ll);
					lineNo.put(name, info.getCompilUnit()
							.getLineNumber(ifStmt.getStartPosition()));
				}else if(!values.get(name).contains(ie.getLeftOperand().toString())){
					LinkedList<String> ll = values.get(name);
					ll.add(ie.getRightOperand().toString());
					values.put(name, ll);
				}
			}else if(ie.getRightOperand() instanceof MethodInvocation ||
					ie.getRightOperand() instanceof InfixExpression){
				parseExpression(ie.getRightOperand());
			}else if(ie.getLeftOperand() instanceof MethodInvocation ||
					ie.getLeftOperand() instanceof InfixExpression){
				parseExpression(ie.getLeftOperand());
			}
			
		}
	}
}
