package com.jframe;

import java.util.ArrayList;
import java.util.List;

public class StreamTest {

	public static void main(String args[]) {
		int i = 0;
		List<Integer> intList = new ArrayList<>();
		while (i < 1000) {
			i++;
			intList.add(i);
		}

		List<Integer> intList2 = new ArrayList<>();
		intList.parallelStream().filter(n -> n % 2 == 0).forEach(e -> intList2.add(e));
	}

}
