package com.fung.partern.broker;

import java.util.Vector;

public class Broker {

	// 记录server的ID号，从0递增
	private int serverID;
	// 将所有注册的server保存到vector容器内
	private Vector<Server> serverVector;

	public Broker() {
		serverID = 0;
		serverVector = new Vector<Server>();
	}

	// 为server提供注册broker的功能，为每个server设置一个从0递增的ID号
	public void Register(Server s) {
		serverVector.add(serverID, s);
		serverID++;
	}

	// broker根据server的ID号获得response，以便转发给客户端显示结果
	public int GetResponseByRequestId(int x, int y, int id) {
		Server s = (Server) serverVector.get(id);
		s.GetRequest(x, y);
		return s.SetResponse();
	}

}
