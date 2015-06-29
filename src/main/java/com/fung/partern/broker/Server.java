package com.fung.partern.broker;

public class Server {

	private int firstNum;
	private int secondNum;
	private int serverID;
	private int rusult;

	// 每次新增server，都要给他一个ID
	public Server(int id) {
		this.serverID = id;
	}

	// server获得request，即获取传入的两个整数
	public void GetRequest(int num1, int num2) {
		this.firstNum = num1;
		this.secondNum = num2;
	}

	// 根据ID号调用对应的服务计算结果，得到response
	public int SetResponse() {
		if (serverID == 0) {
			rusult = firstNum + secondNum;
			return rusult;
		} else if (serverID == 1) {
			rusult = firstNum - secondNum;
			return rusult;
		} else if (serverID == 2) {
			rusult = firstNum * secondNum;
			return rusult;
		} else {
			rusult = firstNum / secondNum;
			return rusult;
		}
	}

}
