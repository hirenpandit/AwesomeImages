package com.jframe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

	public static void main(String[] args) {
		/*
		 * Enter your code here. Read input from STDIN. Print output to STDOUT.
		 * Your class should be named Solution.
		 */
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		for (int i = 0; i < t; i++) {
			// System.out.println("=====================");
			calculateMove(sc);
			// System.out.println("=====================");
		}
	}

	private static void calculateMove(Scanner sc) {
		int n = sc.nextInt();
		int m = sc.nextInt();

		// System.out.println( String.format("T: %s N: %s M: %s",t,n,m));
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			int j = sc.nextInt();
			list.add(j);
		}
		int p = 0;
		while (p < n - 1) {
			++p;
			if (list.get(p) == 1) {
				p = p - 1;
				break;
			}
		}
		// System.out.println("first p: "+p);
		if (p + m > n) {
			System.out.println("YES");
		} else {
			int newp = getNewP(p, m, list, n);
			while (newp != 0) {
				newp = getNewP(p, m, list, n);
			}
			if (newp != 0 && newp + m > n) {
				System.out.println("YES");
			}
		}

	}

	private static int getNewP(int p, int m, List<Integer> list, int n) {
		int temp = p;
		if (list.get(p + m) == 0) {
			// System.out.println("firstnew P: "+(p+m));
			return p + m;
		} else {
			while (list.get(p + m) == 1 && list.get(p) != 0) {
				p = p - 1;
			}
			if (p == 0 || p + m == temp) {
				System.out.println("NO");
				return 0;
			}
		}
		return p + m;
	}

}
