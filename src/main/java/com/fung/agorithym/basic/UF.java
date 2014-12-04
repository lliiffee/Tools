package com.fung.agorithym.basic;

import com.fung.tool.tookit.StdIn;
import com.fung.tool.tookit.StdOut;

public class UF {

	private int[] id; // access to component id (site indexed)
	private int count; // number of components

	public UF(int N) { // Initialize component id array.
		count = N;
		id = new int[N];
		for (int i = 0; i < N; i++)
			id[i] = i;
	}

	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	public int find(int p) {
		return id[p];
	}
/*The find() operation is certainly quick, as it only accesses the
id[] array once in order to complete the operation. But quick-find is typically not useful
for large problems because union() needs to scan through the whole id[] array for
each input pair.*/
	
	public void union(int p, int q) { // Put p and q into the same component.
		int pID = find(p);
		int qID = find(q);
		// Nothing to do if p and q are already
		// in the same component.
		if (pID == qID)
			return;
		// Rename p’s component to q’s name.  ,,将p 和 q 关联。。需要将原来p 关联的元素的值全部 变为 q 的值。。。。
		for (int i = 0; i < id.length; i++)
			if (id[i] == pID)
				id[i] = qID;
		count--;
	}

	// See page 222 (quick-find),page 224 (quick-union) andpage 228 (weighted).
	public static void main(String[] args) { // Solve dynamic connectivity
												// problem on StdIn.
		int N = StdIn.readInt(); // Read number of sites.
		UF uf = new UF(N); // Initialize N components.
		while (!StdIn.isEmpty()) {
			int p = StdIn.readInt();
			int q = StdIn.readInt(); // Read pair to connect.
			if (uf.connected(p, q))
				continue; // Ignore if connected.
			uf.union(p, q); // Combine components
			StdOut.println(p + " " + q); // and print connection.
		}
		StdOut.println(uf.count() + " components");
	}

}
