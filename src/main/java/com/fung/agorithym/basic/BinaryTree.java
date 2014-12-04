package com.fung.agorithym.basic;

public class BinaryTree {
	private int value;
	private BinaryTree left;
	private BinaryTree right;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public BinaryTree getLeft() {
		return left;
	}
	public void setLeft(BinaryTree left) {
		this.left = left;
	}
	public BinaryTree getRight() {
		return right;
	}
	public void setRight(BinaryTree right) {
		this.right = right;
	}
	
	public int sumAll()
	{
		int _total=value;
		
		if(getLeft()!=null)
		{
			_total+=getLeft().sumAll();
		}
		if(getRight()!=null)
		{
			_total+=getRight().sumAll();
		}
		
		return _total;
	}
	

}
