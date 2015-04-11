import org.asdfa.OtherClass;
import org.asdfa.SuperClass;
import org.asdfa.SuperInterface;

public class SubClass1 extends SuperClass implements SuperInterface {

	private String attrOne;
	private Integer attrTwo;
	public static final Double CONSTANT_VAL = 2.3;
	private Day day;

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