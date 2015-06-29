package com.fung.partern.broker;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {
	/** * */
	private static final long serialVersionUID = 1L;
	private int firstNum;
	private int secondNum;
	private int serverID;
	private int rusult;
	private Broker broker;
	private JButton submitBtn;
	private JButton clearBtn;
	private JTextField firtInput;
	private JTextField secInput;
	private JTextField displayResult;
	// 提供加、减、乘、除四种服务供用户选择
	private String[] item = { "addition", "subtraction", "multiplication",
			"division" };
	private JComboBox<String> combobox = new JComboBox<String>(item);

	public Client(Broker b) {
		this.setTitle("Broker Pattern");
		broker = b;
		// 添加各种组件并注册相应的监听器
		setLayout(new GridLayout(5, 1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 2));
		JLabel label1 = new JLabel("First Number");
		firtInput = new JTextField();
		JLabel label2 = new JLabel("Second Number");
		secInput = new JTextField();

		panel.add(label1);
		panel.add(firtInput);
		panel2.add(label2);
		panel2.add(secInput);
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1, 2));
		JLabel label3 = new JLabel("Server");
		panel3.add(label3);
		panel3.add(combobox);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayout(1, 2));
		submitBtn = new JButton("Sunmit");
		clearBtn = new JButton("Clear");
		submitBtn.addActionListener(this);
		clearBtn.addActionListener(this);
		panel4.add(submitBtn);
		panel4.add(clearBtn);
		JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayout(1, 2));
		JLabel label5 = new JLabel("Result:");
		displayResult = new JTextField();
		displayResult.setEditable(false);
		panel5.add(label5);
		panel5.add(displayResult);
		add(panel);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);
	}

	// 实现监听事件处理程序

	public void actionPerformed(ActionEvent e)

	{
		if (e.getActionCommand() == "Sunmit") {
			try {
				int a = Integer.parseInt(firtInput.getText().trim());
				int b = Integer.parseInt(secInput.getText().trim());
				// 发送请求，包含两个整数以及服务ID
				SetRequest(a, b, combobox.getSelectedIndex());
				// 获得由broker转发的response并显示到客户端
				int out = GetResponse();

				displayResult.setText(Integer.toString(out));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Your input should be a type of integer!");
			}
		} else if (e.getActionCommand() == "Clear") {
			// 清空输入框
			firtInput.setText("");
			secInput.setText("");
			displayResult.setText("");
		}
	}

	// 添加request，发送两个整数以及server的ID号
	public void SetRequest(int num1, int num2, int id) {
		this.firstNum = num1;
		this.secondNum = num2;
		this.serverID = id;
	}

	// 获取broker转发的response，记录计算结果
	public int GetResponse() {
		rusult = broker.GetResponseByRequestId(firstNum, secondNum, serverID);
		return rusult;

	}

	public static void main(String[] args) {
		Broker b = new Broker();
		final Client client = new Client(b);
		// totalServer记录提供的服务总数
		int totalServer = client.item.length;
		// 为每个server注册broker，ID号依次为0到maxID
		for (int i = 0; i < totalServer; i++) {
			Server server = new Server(i);
			b.Register(server);
		}
		client.setVisible(true);
		client.setSize(300, 200);
		// client.pack();
		// 自动调节窗口大小
		client.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}