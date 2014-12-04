package com.fung.agorithym.basic;

import java.util.Iterator;

public class StackLinkedListLike<Item> implements Iterable<Item> {

	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
		}

		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

	StackLinkedListLike()// create an empty stack
	{

	}

	void push(Item item) // add an item
	{
		// Add item to top of stack.
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		N++;
	}

	Item pop() // remove the most recently added item
	{
		// Remove item from top of stack.
		Item item = first.item;
		first = first.next;
		N--;
		return item;
	}

	boolean isEmpty() // is the stack empty?
	{
		return true;
	}

	int size() // number of items in the stack
	{
		return 0;
	}

	private Node first; // top of stack (most recently added node)
	private int N; // number of items

	private class Node {
		Item item;
		Node next;

	}

}

/* 循环
 * for (Node x = first; x != null; x = x.next)
{
// Process x.item.
}
*/
