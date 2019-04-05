package minimization;

import java.util.Scanner;
import java.util.Vector;

public class MyProject {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n, x;
		Vector<Integer> inputD = new Vector<Integer>();
		Vector<Integer> inputM = new Vector<Integer>();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter number of variables: ");
		n = sc.nextInt();
		System.out.println("Enter MINTERMS, -1 to end: ");	
		while ((x = sc.nextInt()) != -1) {
			inputM.add(x);
		}
		System.out.println("Enter DON'T CARE, -1 to end: ");
		while ((x = sc.nextInt()) != -1) {
			inputD.add(x);
		}
		TabularMethod t = new TabularMethod(n, inputM, inputD);
		t.minimize();
		System.out.println("Prime Implicants:\n");
		for(int i = 0; i < t.outputs.length; i++) {
			if(i == t.outputs.length - 1) {
				System.out.print(t.outputs[i]);
			} else {
				System.out.print(t.outputs[i] + ", ");
			}
		}	
	}

}
