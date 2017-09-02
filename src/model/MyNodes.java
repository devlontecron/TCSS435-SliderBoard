/**
 * Devin Durham
 * TCSS435
 * 06/09/2017
 * 
 */


package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Class is used for holding and storing board configurations and additional information about them once placed in the tree
 * @author Devin
 *
 */
public class MyNodes {
	List<Character> gameBoard;
	LinkedList<MyNodes> children;
	int cost;
	MyNodes parent;
	
	public MyNodes(List<Character> board, int c, MyNodes par){
		this.gameBoard = board;
		this.cost = c;
		this.parent = par;
		this.children = new LinkedList<MyNodes>();
		
	}
	
	
	
	public MyNodes getParent() {
		return parent;
	}


	public void setParent(MyNodes parent) {
		this.parent = parent;
	}
	
	
	public void addChild(MyNodes node){
		children.add(node);
	}



	public List<Character> getGameBoard() {
		return gameBoard;
	}



	public void setGameBoard(List<Character> gameBoard) {
		this.gameBoard = gameBoard;
	}



	public LinkedList<MyNodes> getChildren() {
		return children;
	}



	public void setChildren(LinkedList<MyNodes> children) {
		this.children = children;
	}



	public int getCost() {
		return cost;
	}



	public void setCost(int deapth) {
		this.cost = deapth;
	}

}
