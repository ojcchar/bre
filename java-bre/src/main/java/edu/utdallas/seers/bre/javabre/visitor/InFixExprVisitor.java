package edu.utdallas.seers.bre.javabre.visitor;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.entity.MaxMin;

public class InFixExprVisitor extends ASTVisitor {

	private HashMap<String, MaxMin> varsValues = new HashMap<String, MaxMin>();

	@Override
	public boolean visit(InfixExpression node) {

		Expression leftOp = node.getLeftOperand();
		Operator operator = node.getOperator();
		Expression rightOp = node.getRightOperand();

		if (!Operator.GREATER.equals(operator)
				&& !Operator.GREATER_EQUALS.equals(operator)
				&& !Operator.LESS.equals(operator)
				&& !Operator.LESS_EQUALS.equals(operator)) {
			return super.visit(node);
		}

		String[] varVal = null;
		boolean left = false;
		if ((leftOp instanceof SimpleName)
				&& (rightOp instanceof NumberLiteral)) {
			varVal = new String[] { leftOp.toString(), rightOp.toString() };
			left = true;
		}

		if ((rightOp instanceof SimpleName)
				&& (leftOp instanceof NumberLiteral)) {
			varVal = new String[] { rightOp.toString(), leftOp.toString() };
		}

		if (varVal == null) {
			return super.visit(node);
		}

		MaxMin maxMin = getVarsValues().get(varVal[0]);
		if (maxMin == null) {
			maxMin = new MaxMin();
			getVarsValues().put(varVal[0], maxMin);
		}

		if (Operator.GREATER.equals(operator)
				|| Operator.GREATER_EQUALS.equals(operator)) {

			if (left) {
				maxMin.min = varVal[1];
			} else {
				maxMin.max = varVal[1];
			}
		} else {

			if (left) {
				maxMin.max = varVal[1];
			} else {
				maxMin.min = varVal[1];
			}
		}

		return super.visit(node);
	}

	public HashMap<String, MaxMin> getVarsValues() {
		return varsValues;
	}

}
