package com.fung.partern.memento;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class ListMode implements StateDependedAction {  
  
    @Override  
    public void action(TextState text, String cmd) {  
        text.setEditable(false);  
        String context = "";  
        if (cmd.matches("[\\d]{1,},[\\d]{1,}l")) {  
            String numline[] = cmd.split(",", 2);  
            int beginline = Integer.parseInt(numline[0]);  
            String numline2[] = numline[1].split("l", 2);  
            int endline = Integer.parseInt(numline2[0]);  
            if(beginline > endline){  
                JOptionPane.showMessageDialog(text, "Bad Command");  
            }else{  
                if (beginline < 1) {  
                    beginline = 1;  
                }  
                if (endline > text.getLineCount()) {  
                    endline = text.getLineCount();  
                }  
                String[] lineString = text.getText().split("\n");  
                for (int i = beginline - 1; i < endline; i++) {  
                    context += lineString[i];  
                    context += "\n";  
                }  
  
                text.setText(context);  
            }  
              
        } else if (cmd.matches("[\\d]{1,}l")) {  
            String numline[] = cmd.split("l", 2);  
            int beginline = Integer.parseInt(numline[0]);  
            if (beginline > 0 && beginline < text.getLineCount()) {  
                String[] lineString = text.getText().split("\n");  
                int endline = text.getLineCount();  
                if (text.getCurline() + text.getPageline() < text  
                        .getLineCount()) {  
                    endline = text.getCurline() + text.getPageline();  
                }  
                for (int i = text.getCurline(); i < endline-1 ; i++) {  
                    context += lineString[i];  
                    context += "\n";  
                }  
  
                text.setText(context);  
            }  
        } else if (cmd.matches("(,[\\d]{1,}l|l)")) {  
            if (text.getCurline() < 12) {  
                try {  
                    context = text.getText(text.getLineStartOffset(0),  
                            text.getLineStartOffset(text.getCurline()+1));  
                    text.setText(context);  
                } catch (BadLocationException e) {  
                    e.printStackTrace();  
                }  
            } else {  
                String[] lineString = text.getText().split("\n");  
                for (int i = (text.getCurline() - 12); i < text.getCurline(); i++) {  
                    context += lineString[i];  
                    context += "\n";  
                }  
                text.setText(context);  
            }  
        }  
    }  
  
}  
