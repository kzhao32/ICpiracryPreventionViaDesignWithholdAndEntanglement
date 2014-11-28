import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		String stringWalker = "OUTPUT(22)";
//		String stringWalkerLC = stringWalker.toLowerCase();
//		if (stringWalkerLC.contains("output(")) {
//			int o = Integer.parseInt(stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")));	// o is the input
//				System.out.println(stringWalker.substring(0, stringWalker.indexOf("(") + 1) + (o) + stringWalker.substring(stringWalker.indexOf(")")));
//				
//				
//		}
//		String a = "abcde";
//		System.out.println(a.substring(0, a.length() - 2));
//		String b = "10 = NAND(1a, 3a)";
//		System.out.println(b.substring(0, b.indexOf("(") + 1));
		
		
		
//		String stringWalker = "10 = NAND(25, 27)";
//		String[] gateInputsStringArray = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")).split(", ");
//		String gateInputsString = "";
//		for (int m = 0; m < gateInputsStringArray.length; m++) {
//			int n = Integer.parseInt(gateInputsStringArray[m]);  	// n is the input(s)
//			gateInputsString += n + ", ";
//		}
//		System.out.println(gateInputsString);
//		String gateAfterOutputString = stringWalker.substring(stringWalker.indexOf(" = "), stringWalker.indexOf("(") + 1);
//		System.out.println(gateAfterOutputString);
		
		
		
//		Scanner testPatternFile;
//		try {
//			testPatternFile = new Scanner(new File("qwerty.txt"));
//			while (testPatternFile.hasNextLine()) {
//				System.out.println("test");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		int [] a = new int[0];
//		if (5==(int) (3*Math.random()*2)) {
//			a = new int[5];
//		}
//		for (int i = 0; i < a.length; i++) {
//			System.out.println("test");
//		}
		
		
//		String a = "";
//		System.out.println(a);
		
		try{
			 
    		File file = new File("c17.10.10.10.00.flt");
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
	}

}
