package com.fung.partern.memento;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CmdHandler {
	 public StateDependedAction state;  
	    private CareTaker caretaker = new CareTaker();  
	    public TextMemento justModified;  
	  
	    public void handler(JTextField textField_1, TextState text) {  
	        String cmd = textField_1.getText();  
	        if (textField_1.getText().equals("q")) {  
	            System.exit(0);  
	        } else if (textField_1.getText().equals("U")) {  
	            justModified = new TextMemento(new TextState(text.getText(),  
	                    text.getCurline()));  
	            text.setText(caretaker.getMemento().getText().getText());  
	        } else if (textField_1.getText().equals(".")) {  
	            text.setEditable(false);  
	        } else if (textField_1.getText().equals("Y")) {  
	            if (justModified != null) {  
	                text.setText(justModified.getText().getText());  
	            }  
	        } else {  
	            caretaker.saveMemento(new TextMemento(new TextState(text.getText(), text  
	                    .getCurline())));  
	            if (cmd.matches("[\\d]{0,}i")) {  
	                state = new InsertMode();  
	                state.action(text, cmd);  
	            } else if (cmd  
	                    .matches("(l|[\\d]{1,}l|[\\d]{1,},[\\d]{1,}l|,[\\d]{1,}l)")) {  
	                state = new ListMode();  
	                state.action(text, cmd);  
	            } else if (cmd.contains("r$,$")) {  
	                state = new ReplaceMode();  
	                state.action(text, cmd);  
	            } else if (cmd.matches("[\\d]{0,}w(.*)")) {  
	                state = new WriteFileMode();  
	                state.action(text, cmd);  
	            } else {  
	                JOptionPane  
	                        .showMessageDialog(text,  
	                                "Standard Commands:\n[#]i\n[#][,#]l\n[#][,#][?]r$,$\nq\n[#]wFilename\nU\nY");  
	            }  
	        }  
	  
	    }  
}
