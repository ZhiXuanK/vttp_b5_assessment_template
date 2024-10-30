package vttp.batch5.sdf.task02;

import java.io.*;
import java.util.*;

public class Main {

	static String[][] gameboard = { { ".", ".", "." },
			{ ".", ".", "." },
			{ ".", ".", "." } };

	static List<int[]> legalPositions = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		// System.out.printf("hello, world\n");
		// args[0] is the ttt file in the format TTT/board0.txt
		File file = new File(args[0]);
		if (!file.exists()) {
			System.err.println("Please input a TTT configuration file");
			System.exit(-1);
		}

		// read the file, character by character
		try (Reader reader = new FileReader(file);
				BufferedReader br = new BufferedReader(reader);) {
			String line = "";
			int outside = 0;

			while ((line = br.readLine()) != null) {
				for (int i = 0; i < 3; i++) {
					char c = line.charAt(i);
					if (c == '.') {
						int[] pos = new int[2];
						pos[0] = outside;
						pos[1] = i;
						legalPositions.add(pos);
					}
					gameboard[outside][i] = String.valueOf(c);

				}
				outside += 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ^^^^^^ code above take in the TTT file, puts it into gameboard and stores

		// store utility of each position using map
		Map<int[], Integer> utility = new HashMap<>();

		// loop through each position to get utility
		for (int[] p : legalPositions) {
			// start with util = 0, then check for 2O,1. to account for O winning in the
			// next step, then 3X to account for X alr win
			int util = 0;
			// x position p[0], y position p[1]
			gameboard[p[0]][p[1]] = "X";
			// check 2 O 1".""
			// rows and columns
			for (int i = 0; i < 3; i++) {
				int rowZero = 0;
				int rowSpace = 0;
				int colZero = 0;
				int colSpace = 0;
				for (int j = 0; j < 3; j++) {
					if (gameboard[i][j].equals("O")) {
						rowZero += 1;
					} else if (gameboard[i][j].equals(".")) {
						rowSpace += 1;
					}
					if (gameboard[j][i].equals("O")) {
						colZero += 1;
					} else if (gameboard[j][i].equals(".")) {
						colSpace += 1;
					}
				}

				if (rowSpace == 1 && rowZero == 2) {
					util = -1;
				} else if (colSpace == 1 && colZero == 2) {
					util = -1;
				}
			}
			// diagonal
			List<String> diagonalLeft = new ArrayList<>();
			diagonalLeft.add(gameboard[0][0]);
			diagonalLeft.add(gameboard[1][1]);
			diagonalLeft.add(gameboard[2][2]);
			List<String> diagonalRight = new ArrayList<>();
			diagonalRight.add(gameboard[0][2]);
			diagonalRight.add(gameboard[1][1]);
			diagonalRight.add(gameboard[2][0]);

			int leftZero = 0;
			int leftSpace = 0;
			for (int i = 0; i < 3; i++) {
				if (diagonalLeft.get(i).equals("O")) {
					leftZero += 1;
				} else if (diagonalLeft.get(i).equals(".")) {
					leftSpace += 1;
				}
			}

			if (leftSpace == 1 && leftZero == 2) {
				util = -1;
			}

			int rightZero = 0;
			int rightSpace = 0;
			for (int i = 0; i < 3; i++) {
				if (diagonalRight.get(i).equals("O")) {
					rightZero += 1;
				} else if (diagonalRight.get(i).equals(".")) {
					rightSpace += 1;
				}
			}
			if (rightSpace == 1 && rightZero == 2) {
				util = -1;
			}

			// check 3X
			// diagonal
			if ((gameboard[0][0].equals("X") && gameboard[1][1].equals("X") && gameboard[2][2].equals("X"))
					|| (gameboard[0][2].equals("X") && gameboard[1][1].equals("X") && gameboard[2][0].equals("X"))) {
				util = 1;
			}
			// row
			for (int i = 0; i < 3; i++) {
				if (gameboard[i][0].equals("X") && gameboard[i][1].equals("X") && gameboard[i][2].equals("X")) {
					util = 1;
				}
			}
			// column
			for (int i = 0; i < 3; i++) {
				if (gameboard[0][i].equals("X") && gameboard[1][i].equals("X") && gameboard[2][i].equals("X")) {
					util = 1;
				}
			}
			gameboard[p[0]][p[1]] = ".";
			utility.put(p, util);
		}

		System.out.println("Processing: " + args[0]);
		System.out.println();
		System.out.println("Board:");
		for (int i = 0; i < 3; i++) {
			System.out.printf("%s%s%s\n", gameboard[i][0], gameboard[i][1], gameboard[i][2]);
		}
		System.out.println("-----------------------------");
		for (var entry : utility.entrySet()) {
			System.out.printf("y=%d, x=%d, utility=%d\n", entry.getKey()[0], entry.getKey()[1], entry.getValue());
		}

	}

}
