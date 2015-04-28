package org.asdfa;

public class OtherClass {
	
	public static enum DAYS_OF_THE_WEEK {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
	
	public static int staticMethod(double param1){
		
		int x = 0;
		switch(x){
		case 0: 
			break;
		case 1: x = 5;
			break;
		case 3: x = 2;
			break;
		default:
		}
		
		String val1 = "oranges";
		String val2 = "grass";
		
		if(val1 != null && val1.equals("oranges")  ){
			val1 = "action1";
		}else if(val1.equals("red")){
			val1 = "action2";
		}
		
		int y = 0;
		int z = 5;
		
		if(y < 4){
			y = 3;
		}else if (y > 5 && z == 5){
			y = 7;
		}else if(z <= 5){
			y = 9;
		}else if(y >= 4 && z != 5){
			y = 10;
		}
		
		return 0;
	}

}
