package com.fung.agorithym;


/*
 * 0123456789 --componet   
 * 0123456789 --value     值代表其父元素。。例如第5个元素值 = 3 代表 其父元素是 第3个 元素。。所以3 和 5 是关联的。
 * 0123436789 -- 5连接 3   
 *     
 *     
 *     
 */
public class WeightedQuickUnionUF {
	private int[] id; // parent link (site indexed)
	private int[] sz; // size of component for roots (site indexed)
	private int count; // number of components

	public WeightedQuickUnionUF(int N) {
		count = N;
		id = new int[N];
		for (int i = 0; i < N; i++)
			id[i] = i;
		sz = new int[N];
		for (int i = 0; i < N; i++)
			sz[i] = 1;
	}

	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);       //判断两个节点是否关联，就是寻找他们的父节点是否一样。。
	}

	private int find(int p) { // Follow links to find a root.  
		while (p != id[p])
			p = id[p];
		return p;
	}

	public void union(int p, int q) { //比较两个树的大小，将 小的连去大的。。避免树变太深。。
		int i = find(p);
		int j = find(q);
		if (i == j)
			return;
		// Make smaller root point to larger one.
		if (sz[i] < sz[j]) {
			id[i] = j;
			sz[j] += sz[i];
		} else {
			id[j] = i;
			sz[i] += sz[j];
		}
		count--;
	}
	
	/*
	public void union(int p, int q)
	{ // Give p and q the same root.
	int pRoot = find(p);
	int qRoot = find(q);
	if (pRoot == qRoot) return;
	id[pRoot] = qRoot;
	count--;
	}
	*/
}
