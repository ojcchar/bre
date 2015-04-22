package org.asdfa.sub;

import org.asdfa.OtherClass;
import org.asdfa.SuperClass;
import org.asdfa.SuperInterface;

public class SubClass1 extends SuperClass implements SuperInterface {

	private static final int _4000 = 4000;
	private static final int _100 = 100;
	private String attrOne;
	private Integer attrTwo;
	public static final Double CONSTANT_VAL = 2.3;
	private Day day;
	private static final String[] colNames = { "documentno", "businesspartner",
			"description", "duedate", "amount", "rowkey" };// ArrayInitializer
	public static final String CONSTANT_VAL2 = new String("3");//classinstancecreation
	private static final long milisecDayConv = (1000 * 60 * 60 * 24); //Infix expression

	public enum Day {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
	}

	public void methodOne() {

		int variableOne = 0;

		if (variableOne > 100 && variableOne < 4000) {
			System.out.println("good!");
			boolean var2 = true;
			if (var2) {
				System.out.println("true");
			}
		} else {
			return;
		}

		int variable4 = 0;
		int variable3 = 0;
		if (variable3 > SubClass21._100 && variable4 < _4000) {
			System.out.println();
		}

		if (attrOne == null) {
			System.out.println("attrOne is null");
		}

		int[] array = new int[100];

		for (int i = variableOne; i < variableOne + 100; i++) {
			System.out.println(array[i]);
		}
	}

	@Override
	public void test() {
		OtherClass.staticMethod(0.2);
	}

}