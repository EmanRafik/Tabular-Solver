package minimization;

import java.util.ArrayList;
import java.util.Vector;

public class TabularMethod {

	int n;
	Vector<Integer> Minterms = new Vector<Integer>();
	Vector<Integer> Dontcare = new Vector<Integer>();
	Vector<Vector<String>> groups;
	Vector<Vector<Boolean>> check;
	ArrayList<String> pI = new ArrayList<String>();
	String[] outputs;
	

	public TabularMethod(int n, Vector<Integer> Minterms, Vector<Integer> Dontcare) {
		this.n = n;
		this.Minterms = Minterms;
		this.Dontcare = Dontcare;
		groups = new Vector<Vector<String>>(n + 1);
		check = new Vector<Vector<Boolean>>(n + 1);
		for (int i = 0; i <= n; i++) {
			groups.add(new Vector<String>());
			check.add(new Vector<Boolean>());
		}
	}

	public void setGroups() {
		int g;
		int x;
		String s;
		for (int i = 0; i < Minterms.size(); i++) {
			x = Minterms.get(i);
			g = Integer.bitCount(x);
			s = Integer.toBinaryString(x);
			groups.get(g).add(s);
			check.get(g).add(false);
		}
		for (int i = 0; i < Dontcare.size(); i++) {
			x = Dontcare.get(i);
			g = Integer.bitCount(x);
			s = Integer.toBinaryString(x);
			groups.get(g).add(s);
			check.get(g).add(false);
		}
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j < groups.get(i).size(); j++) {
				if (groups.get(i).get(j).length() < n) {
					String num = groups.get(i).get(j);
					for (int k = 0; k < (n - groups.get(i).get(j).length()); k++) {
						num = '0' + num;
					}
					groups.get(i).set(j, num);
				}
			}
		}
		System.out.println("\nGroups\n");
		for(int i = 0; i <=n; i++) {
			System.out.print("Group " + i + ": ");
			for(int j = 0; j < groups.get(i).size(); j++) {
				if(j == groups.get(i).size() - 1) {
					System.out.print(groups.get(i).get(j));
				} else {
					System.out.print(groups.get(i).get(j) + ", ");
				}
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}

	public void minimize() {
		setGroups();
		int step = 0;
		while (!checkEmpty()) {
			for (int g = 0; g < n; g++) {
				hammingDistance(g);
			}
			for (int i = 0; i <= n; i++) {
				for (int j = 0; j < groups.get(i).size(); j++) {
					if (check.get(i).get(j) && (count(groups.get(i).get(j)) <= step)) {
						groups.get(i).remove(j);
						check.get(i).remove(j);
						j--;
					}
				}
			}
			System.out.println("Step" + (step + 1) + ":\n");
			for(int i = 0; i <=n; i++) {
				System.out.print("Group " + i + ": ");
				for(int j = 0; j < groups.get(i).size(); j++) {
					if(j == groups.get(i).size() - 1) {
						System.out.print(groups.get(i).get(j));
					} else {
						System.out.print(groups.get(i).get(j) + ", ");
					}
				}
				System.out.print("\n");
			}
			System.out.println("\n");
			addPI();
			step++;
		}
		for(int i = 0; i <= n; i++) {
			if(!groups.get(i).isEmpty()) {
				for(int j = 0; j < groups.get(i).size(); j++) {
					if(!checkPI(groups.get(i).get(j))) {
						pI.add(groups.get(i).get(j));
					}
				}
			}
		}
		output();
		return;
	}

	public void hammingDistance(int g) {
		int diff = 0;
		int bitNum = 0;
		Vector<Integer> vI = new Vector<Integer>();
		Vector<Integer> vK = new Vector<Integer>();
		for (int i = 0; i < groups.get(g).size(); i++) {
			for (int j = 0; j < groups.get(g + 1).size(); j++) {
				diff = 0;
				for (int k = 0; k < n; k++) {
					if(checkDiff(groups.get(g).get(i),groups.get(g + 1).get(j))) {
						if (groups.get(g).get(i).charAt(k) != groups.get(g + 1).get(j).charAt(k)) {
							diff++;
							bitNum = k;
						}
					} else {
						break;
					}
				}
				if (diff == 1) {
					vI.add(i);
					vK.add(bitNum);
					check.get(g).set(i, true);
					check.get(g + 1).set(j, true);
				}
			}
		}
		for (int i = 0; i < vI.size(); i++) {
			String s = groups.get(g).get(vI.get(i));
			String q;
			q = s.substring(0, vK.get(i)) + "-" + s.substring(vK.get(i) + 1, s.length());
			groups.get(g).add(q);
			check.get(g).add(true);
		}
		vI.clear();
		vK.clear();
	}

	public void addPI() {
		for (int g = 0; g <= n; g++) {
			for (int j = 0; j < check.get(g).size(); j++) {
				if (!check.get(g).get(j)) {
					if(!checkPI(groups.get(g).get(j))) {
						pI.add(groups.get(g).get(j));
					}
					groups.get(g).remove(j);
					check.get(g).remove(j);
					j--;
				}
			}
		}
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j < check.get(i).size(); j++) {
				check.get(i).set(j, false);
			}
		}
	}

	public boolean checkDiff(String a, String b) {
		for(int i = 0; i < n; i++) {
			if((a.charAt(i) == '-' && b.charAt(i) != '-') || (b.charAt(i) == '-' && a.charAt(i) != '-')) {
				return false;
			}
		}
		return true;
	}
	
	public int count(String s) {
		int c = 0;
		for (int i = 0; i < n; i++) {
			if (s.charAt(i) == '-') {
				c++;
			}
		}
		return c;
	}

	public boolean checkEmpty() {
		int empty = 0;
		for (int i = 0; i <= n; i++) {
			if (groups.get(i).isEmpty()) {
				empty++;
			}
		}
		if(empty >= n) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkPI(String a) {
		for(int i = 0; i < pI.size(); i++) {
			if(a.equals(pI.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public void output() {
		String s = " ";
		outputs = new String[pI.size()];
		for (int i = 0; i < pI.size(); i++) {
			s = " ";
			for (int j = 0; j < n; j++) {
				int x = 65 + j;
				char c = (char) x;
				if (pI.get(i).charAt(j) == '1') {
					s = s + c;
				} else if (pI.get(i).charAt(j) == '0') {
					s = s + c + '\'';
				}
			}
			outputs[i] = s.substring(1, s.length());
		}
	}
}
