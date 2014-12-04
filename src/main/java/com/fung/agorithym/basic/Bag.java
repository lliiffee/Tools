package com.fung.agorithym.basic;

import java.util.Iterator;


/*
 * A bag is a collection where removing items is not supported—its purpose is to
provide clients with the ability to collect items and then to iterate through the collected
items (the client can also test if a bag is empty and find its number of items).
 */

public class Bag<Item> implements Iterable<Item>
{
private Node first; // first node in list
private class Node
	{
		Item item;
		Node next;
	}
	public void add(Item item)
	{ // same as push() in Stack
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
	}
	public Iterator<Item> iterator()
	{ 
		return new ListIterator();
	}

	private class ListIterator implements Iterator<Item>
	{
		private Node current = first;
		public boolean hasNext()
		{ return current != null; }
		public void remove() { }
		public Item next()
		{
		Item item = current.item;
		current = current.next;
		return item;
		}
	}
}