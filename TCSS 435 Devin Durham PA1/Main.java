/**
 * Devin Durham
 * TCSS435
 * 06/09/2017
 * 
 */

package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 * a 4x4 slider puzzle solving AI with 5 different tree search methods. 
 * @author Devin
 *
 */
public class Main {

	//Setting up the target or goal states
	static List<Character> target = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', ' ');
	static List<Character> target2 = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'F',
			'E', ' ');
	char searchType;
	static List<Character> userBoard = new ArrayList<Character>();
	static List<MyNodes> fringe = new ArrayList<MyNodes>();
	static int f = 0;
	static int depth;
	static int numCreated;
	static int numExpanded;
	static int maxFringe;
	static Hashtable<Integer, ArrayList<List<Character>>> visited = new Hashtable<Integer, ArrayList<List<Character>>>();

	
	public static void main(String[] args) {
		System.out.println("Welcome!");
		input();

	}

	/**
	 * inform user of format
	 * receives input from user
	 * parses input into board configuration, requested search, and any additional information
	 */
	private static void input() {
		char searchType = 0;
		boolean flag = true;
		String option = null;

		System.out.println(
				"Enter the \"Initial state\", the search method (BFS, DFS, DLS, GBFS, AStar), and options (DeapthLimit, h1, h2)");

		Scanner input = new Scanner(System.in);
		String userIn = input.nextLine();

		char[] userInA = userIn.toCharArray();

		input.close();

		for (int i = 0; userIn.length() > i; i++) {

			if (userInA[i] == '"') {
				i++;

				//loops to make initial board
				for (int x = 0; userInA[i] != '"'; x++) {
					userBoard.add(userInA[i]);
					i++;
				}

				for (int z = 0; userBoard.size() > z; z++) {
					System.out.print(userBoard.get(z));
				}
				System.out.println("");

				//identifies requested search type by getting the first letter of search for later use in switch statement
			} else if (userInA[i] == ' ' && flag) {
				i++;
				searchType = userInA[i];
				if (searchType == 'D') {
					searchType = userInA[i + 1];

				}
				System.out.println("Search Type: " + searchType);
				flag = false;

				//gathers additional options if needed
			} else if (userInA[i] == ' ' && flag == false) {
				if (searchType == 'G' || searchType == 'A') {
					i += 2;
					option = Character.toString(userInA[i]);
					
				} else if (searchType == 'L') {
					i++;
					 option = Character.toString(userInA[i]);
					 i++;
					while(userIn.length()>i){
					option = option + Character.toString(userInA[i]);
					i++;
					}
				}
				System.out.println("Options: " + option);
			}
		}
		
		//switch case to call the appropriate search tree method
		int optionInt = 0;
		if(option != null){
		optionInt = Integer.parseInt(option);
		}
		searchType = Character.toUpperCase(searchType);
		printBoard(userBoard);
		switch (searchType) {
		case 'B':
			BFS(userBoard);
			break;
		case 'F':
			DFS(userBoard, optionInt);
			break;
		case 'L':
			DFS(userBoard, optionInt);
			break;
		case 'G':
			GBFS(userBoard, optionInt);
			break;
		case 'A':
			AStar(userBoard, optionInt);
			break;
		case 'T':
			test(userBoard);
			break;
		default:
			System.out.println("Error: No search type found");
		}

	}
	
	
	private static void test(List<Character> inputArr){
		printBoard(inputArr);
		List<Character> newBoard = moveU(inputArr, getBlank(inputArr));
		printBoard(newBoard);
	}

	/**
	 * BFS search Tree
	 * @param inputArr List<Character> of board configuration
	 */
	private static void BFS(List<Character> inputArr) {
		boolean foundGoal = false;
		MyNodes root = new MyNodes(inputArr, 0, null);
		fringe.add(root);

		for (int i = 0; i < fringe.size() && !foundGoal; i++) {
			//checks and updates fringe max size
			if((fringe.size())>maxFringe){
				maxFringe=fringe.size();
			}
			
			//pulls next node from list
			MyNodes currParent = fringe.remove(0);
			List<Character> currBoard = currParent.getGameBoard();
			
			//printBoard(currBoard);

			//checks if goal state is reached
			if (checkGoal(currBoard) ==0) {
				depth = getDepth(currParent);
				foundGoal = true;
				printGoal();
			} else {
				numExpanded++;

				int mBlank = getBlank(currBoard);

				//performs every possible move and adds to list
				if (moveR(currBoard, mBlank) != null) {
					List<Character> newBoard = moveR(currBoard, mBlank);
					MyNodes newNode = new MyNodes(newBoard, 0, currParent);
					currParent.addChild(newNode);
					fringe.add(newNode);
					numCreated++;
				}
				if (moveD(currBoard, mBlank) != null) {
					List<Character> newBoard = moveD(currBoard, mBlank);
					MyNodes newNode = new MyNodes(newBoard, 0, currParent);
					currParent.addChild(newNode);
					fringe.add(newNode);
					numCreated++;
				}
				if (moveL(currBoard, mBlank) != null) {
					List<Character> newBoard = moveL(currBoard, mBlank);
					MyNodes newNode = new MyNodes(newBoard, 0, currParent);
					currParent.addChild(newNode);
					fringe.add(newNode);
					numCreated++;
				}
				if (moveU(currBoard, mBlank) != null) {
					List<Character> newBoard = moveU(currBoard, mBlank);
					MyNodes newNode = new MyNodes(newBoard, 0, currParent);
					currParent.addChild(newNode);
					fringe.add(newNode);
					numCreated++;
				}
			}
		}
	}




	
	/**
	 * DFS & DLS
	 * this worked with simple solutions. i can only assume this would work for complex boards if i let it sit long enough.	
	 * @param inputArr board configuration 
	 * @param option used if DLS is called
	 */
	private static void DFS(List<Character> inputArr, int option) {
		MyNodes root = new MyNodes(inputArr, 0, null);
		
		hasVisited(inputArr, getBlank(inputArr));
		int depthLimit= 0;
		boolean foundGoal = false;
		fringe.add(root);

		/**
		 * checks if DLS or DFS mode by checking if the options were ever set
		 */
		if(option == 0){
			depthLimit = -1;
		}else{
			depthLimit = option;
		}
		numCreated++;
		while (!fringe.isEmpty() && !foundGoal) {
			if(fringe.size()>maxFringe){
				maxFringe=fringe.size();
			}
			MyNodes currParent = fringe.remove(fringe.size() - 1);
			if(getDepth(currParent)!=depthLimit){
				
			List<Character> currBoard = currParent.getGameBoard();
			int mBlank = getBlank(currBoard);
			
			printBoard(currBoard);
			if (checkGoal(currBoard) == 0) {
				depth = getDepth(currParent);
				foundGoal = true;
				System.out.println("Goal state reached");
				printGoal();
			} else {
				numExpanded++;

				if (moveU(currBoard, mBlank) != null) {
					List<Character> newBoard = moveU(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-4)) {
						MyNodes newNode = new MyNodes(newBoard, 0, currParent);
						currParent.addChild(newNode);
						fringe.add(newNode);
						numCreated++;
					}
				}
				if (moveL(currBoard, mBlank) != null) {
					List<Character> newBoard = moveL(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-1)) {
						MyNodes newNode = new MyNodes(newBoard, 0, currParent);
						currParent.addChild(newNode);
						fringe.add(newNode);
						numCreated++;
					}
				}
				if (moveD(currBoard, mBlank) != null) {
					List<Character> newBoard = moveD(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+4)) {
						MyNodes newNode = new MyNodes(newBoard, 0, currParent);
						currParent.addChild(newNode);
						fringe.add(newNode);
						numCreated++;
					}
				}
				if (moveR(currBoard, mBlank) != null) {
					List<Character> newBoard = moveR(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+1)) {
						MyNodes newNode = new MyNodes(newBoard, 0, currParent);
						currParent.addChild(newNode);
						fringe.add(newNode);
						numCreated++;
					}
				}
			
			}
			}
			
		}
		if(!foundGoal){
		System.out.println("Deapth limit reached. all nodes have been expanded");
		depth = -1;
		maxFringe = 0;
		numExpanded = 0;
		numCreated = 0;
		printGoal();
		}
	}
	
	/**
	 * Greedy best first tree.
	 * calculates the cost from root by hx options
	 * @param inputArr game board configuration 
	 * @param hx options for GBFS heuristics
	 */
	private static void GBFS(List<Character> inputArr, int hx) {
		int option = hx;
		MyNodes root = new MyNodes(inputArr, whichH(option, inputArr) , null);
		boolean foundGoal = false;
		fringe.add(root);

		while (!fringe.isEmpty() && !foundGoal) {
			if (fringe.size() > maxFringe) {
				maxFringe = fringe.size();
			}
			MyNodes currParent = fringe.remove(fringe.size() - 1);
			List<Character> currBoard = currParent.getGameBoard();
			int mBlank = getBlank(currBoard);
			hasVisited(currBoard, mBlank);
			//printBoard(currBoard);
			if (checkGoal(currBoard) == 0) {
				depth = getDepth(currParent);
				foundGoal = true;
				printGoal();
			} else {
				numExpanded++;

				if (moveU(currBoard, mBlank) != null) {
					List<Character> newBoard = moveU(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-4)) {
						MyNodes newNode = new MyNodes(newBoard, whichH(option, newBoard), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveL(currBoard, mBlank) != null) {
					List<Character> newBoard = moveL(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-1)) {
						MyNodes newNode = new MyNodes(newBoard, whichH(option, newBoard), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveD(currBoard, mBlank) != null) {
					List<Character> newBoard = moveD(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+4)) {
						MyNodes newNode = new MyNodes(newBoard, whichH(option, newBoard), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveR(currBoard, mBlank) != null) {
					List<Character> newBoard = moveR(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+1)) {
						MyNodes newNode = new MyNodes(newBoard, whichH(option, newBoard), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
			}
		}

	}

	/**
	 * AStar search tree by calculating cost with option heuristic and greedy(cost from root to current node)
 	 * @param inputArr board configuration
	 * @param option heuristics
	 */
	private static void AStar(List<Character> inputArr, int option) {
		MyNodes root = new MyNodes(inputArr, whichH(option, inputArr) , null);
		boolean foundGoal = false;
		fringe.add(root);

		while (!fringe.isEmpty() && !foundGoal) {
			if (fringe.size() > maxFringe) {
				maxFringe = fringe.size();
			}
			MyNodes currParent = fringe.remove(fringe.size() - 1);
			List<Character> currBoard = currParent.getGameBoard();
			int mBlank = getBlank(currBoard);
			hasVisited(currBoard, mBlank);
			int costFromNode = getDepth(currParent);
			//printBoard(currBoard);
			if (checkGoal(currBoard) == 0) {
				depth = getDepth(currParent);
				foundGoal = true;
				printGoal();
			} else {
				numExpanded++;

				if (moveU(currBoard, mBlank) != null) {
					List<Character> newBoard = moveU(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-4)) {
						MyNodes newNode = new MyNodes(newBoard, (whichH(option, newBoard) + costFromNode) , currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveL(currBoard, mBlank) != null) {
					List<Character> newBoard = moveL(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank-1)) {
						MyNodes newNode = new MyNodes(newBoard,(whichH(option, newBoard) + costFromNode), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveD(currBoard, mBlank) != null) {
					List<Character> newBoard = moveD(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+4)) {
						MyNodes newNode = new MyNodes(newBoard, (whichH(option, newBoard) + costFromNode), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
				if (moveR(currBoard, mBlank) != null) {
					List<Character> newBoard = moveR(currBoard, mBlank);
					if (!hasVisited(newBoard, mBlank+1)) {
						MyNodes newNode = new MyNodes(newBoard, (whichH(option, newBoard) + costFromNode), currParent);
						currParent.addChild(newNode);
						addByCost(newNode);
						numCreated++;
					}
				}
			}
		}

	}

	
	/**
	 * H1 heuristic that calculates the out of place tiles
	 * @param arr board configuration
	 * @return cost
	 */
	private static int h1(List<Character> arr){
		int offTarget = 0;
		for (int a = 0; a < arr.size(); a++) {
			if (arr.get(a) == target.get(a) || arr.get(a) == target2.get(a)) {

			} else {
				offTarget++;
			}
		}
		return offTarget;
	}
	
	/**
	 * H2 heuristics calculates the sum of the Manhattan distance for each tile to get to its target place.
	 * does this by parsing out the current tiles location, finding the (x,y), compares to target, and finds difference 
	 * @param arr
	 * @return cost
	 */
	private static int h2(List<Character> arr){
		int offTarget = 0;
		for (int a = 0; a < arr.size(); a++) {
			if (arr.get(a) == target.get(a) || arr.get(a) == target2.get(a)) {
			}else{
				char holder = arr.get(a);
				int col = a%4;
				int row = a/4;
				boolean thingyFound = false;
				for(int i = 0; i<target.size() && !thingyFound; i++){
					if(target.get(i) == holder){
						thingyFound = true;
						int Tc = i%4;
						int Tr = i/4;
						
						int cDif = Math.abs(Tc-col);
						int rDif = Math.abs(Tr-row);
						offTarget = offTarget + cDif+rDif;
					}
				}
			}
		}
		return offTarget;
	}

	/**
	 * a minor helper method to allow for easy incorperation to decide which heuristic to use 
	 * defaults to h2
	 * @param option
	 * @param arr current board configuration
	 * @return
	 */
	private static int whichH(int option, List<Character> arr){
		int cost = 0;
		if (option == 1){
			cost = h1(arr);
		}else{ 
			cost = h2(arr);
		}
		return cost;
	}
	
	/**
	 * places new nodes into the fringe by comparing cost and sorting accordingly. low cost go to end of list (where they will be pulled next)
	 * @param arr current node to be added to fringe
	 */
	private static void addByCost(MyNodes arr){
		boolean isInserted = false;
		for(int i = 0; i<fringe.size() && !isInserted; i++){
			if(fringe.get(i).getCost()<arr.cost){
				fringe.add(i, arr);
				isInserted = true;
			}
		}
		if(isInserted==false){
			fringe.add(arr);
		}
	}
	
	/**
	 * used for the DFS tree to ensure no looping and already visited nodes using a hash table <where the blank spot is, all possible boards we have seen with that blank spot>
	 * @param currBoard List<Character> of board configuration
	 * @param mBlank where the blank spot is currently
	 * @return
	 */
	private static boolean hasVisited(List<Character> currBoard, int mBlank) {
		boolean isSame = false;

		/**
		 * checks if the blank spot even exists as a key
		 */
		if (!visited.containsKey(mBlank)) {
			ArrayList<List<Character>> boardss = new ArrayList<List<Character>>();
			boardss.add(currBoard);
			visited.put(mBlank, boardss);
			isSame = false;
			
			/**
			 * iterates through the list from the hash of all possible board combinations looking for duplicates
			 */
		} else {
			ArrayList<List<Character>> list = visited.get(mBlank);
			for (int d = 0; d < list.size() && !isSame; d++) {
				List<Character> dummyBoard = list.get(d);
				int k = 0;
				boolean isDif= false;
				while (!isDif && !isSame && k<dummyBoard.size()) {
					char db = dummyBoard.get(k);
					char cb = currBoard.get(k);
					if (db == cb) {
						k++;
					}else{
						isDif = true;
					}
				}
				if(k==dummyBoard.size()){
					isSame=true;
				}
			}
			/**
			 * if all the boards have been checks, then this must be a new board and needs to be added to the list
			 */
			if(!isSame){
				ArrayList<List<Character>> boardss = visited.get(mBlank);
				boardss.add(currBoard);
				visited.put(mBlank, boardss);
				isSame = false;
			}
		}
	return isSame;
	}

	
	
	/**
	 * identifies the depth of the current node by looking up to parent and counting till it reaches the root
	 * @param currParent
	 * @return depth of node
	 */
	private static int getDepth(MyNodes currParent) {
		int nodeDepth = 1;
		MyNodes current = currParent;

		while (current.getParent() != null) {
			nodeDepth++;
			current = current.getParent();
		}
		return nodeDepth;
	}

	/**
	 * given the board, method will return the index of where the blank tile is
	 * @param array game board configuration
	 * @return index of blank
	 */
	private static int getBlank(List<Character> array) {
		int root = -1;
		for (int y = 0; array.size() > y; y++) {
			if (array.get(y) == ' ') {
				root = y;
				return root;
			}
		}
		return root;
	}

	/**
	 * compares the given board to the targets and checks for goal state by returning a 0 for goal state and an integer for h1 cost from goal
	 * @param array
	 * @return cost from goal (0 if goal)
	 */
	private static int checkGoal(List<Character> array) {
		int offTarget = 0;
		for (int a = 0; a < array.size(); a++) {
			//
			if (array.get(a) == target.get(a) || array.get(a) == target2.get(a)  ) {
			}else{
				offTarget++;
			}
		}
		return offTarget;
	}

	/**
	 * outputs a new List<Character> after performing a move 
	 * @param Tarray of current game board
	 * @param blank where the blank tile is
	 * @return newGameboard configuration
	 */
	private static List<Character> moveR(List<Character> Tarray, int blank) {
		List<Character> array = new ArrayList<Character>(Tarray);
		if (blank == 3 || blank == 7 || blank == 11 || blank == 15) {
			return null;
		} else {

			char val = array.get(blank + 1);
			array.set(blank + 1, ' ');
			array.set(blank, val);
			return array;
		}
	}

	/**
	 * outputs a new List<Character> after performing a move 
	 * @param Tarray of current game board
	 * @param blank where the blank tile is
	 * @return newGameboard configuration
	 */
	private static List<Character> moveD(List<Character> Tarray, int blank) {
		List<Character> array = new ArrayList<Character>(Tarray);
		try {
			char val = array.get(blank + 4);
			array.set(blank, val);
			array.set(blank + 4, ' ');
			return array;
		} catch (IndexOutOfBoundsException exception) {
			return null;
		}
	}

	/**
	 * outputs a new List<Character> after performing a move 
	 * @param Tarray of current game board
	 * @param blank where the blank tile is
	 * @return newGameboard configuration
	 */
	private static List<Character> moveL(List<Character> Tarray, int blank) {
		List<Character> array = new ArrayList<Character>(Tarray);
		if (blank == 4 || blank == 8 || blank == 12 || blank == 0) {
			return null;
		} else {
			char val = array.get(blank - 1);
			array.set(blank, val);
			array.set(blank - 1, ' ');
			return array;
		}

	}

	/**
	 * outputs a new List<Character> after performing a move 
	 * @param Tarray of current game board
	 * @param blank where the blank tile is
	 * @return newGameboard configuration
	 */
	private static List<Character> moveU(List<Character> Tarray, int blank) {
		List<Character> array = new ArrayList<Character>(Tarray);
		try {
			char val = array.get(blank - 4);
			array.set(blank, val);
			array.set(blank - 4, ' ');
			return array;
		} catch (IndexOutOfBoundsException exception) {
			return null;
		}
	}

	/**
	 * formats and prints the game board to console
	 * @param array game board
	 */
	public static void printBoard(List<Character> array) {
		for (int b = 0; b < array.size(); b++) {
			if (b % 4 == 0) {
				System.out.println("|");
			}
			System.out.print("|" + array.get(b));
		}
		System.out.println("|");
	}

	/**
	 * prints the goal information of goal nodes depth, number of nodes created, number of nodes expanded, max size of the fringe
	 */
	public static void printGoal() {
		System.out.println("\n" + depth + ", " + numCreated + ", " + numExpanded + ", " + maxFringe);
	}

}
