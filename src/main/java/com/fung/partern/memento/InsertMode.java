package com.fung.partern.memento;

import javax.swing.text.BadLocationException;

public class InsertMode implements StateDependedAction {  
  
    @Override  
    public void action(TextState text, String cmd) {  
        text.setEditable(true);  
        if (cmd.matches("[\\d]{0,}i")) {  
            String numline[] = cmd.split("i", 2);  
            int line = 0;  
            try {  
                if (!numline[0].equals("")) {  
                    if (Integer.parseInt(numline[0]) > text.getLineCount()) {  
                        line = text.getLineCount();  
                    } else if (Integer.parseInt(numline[0]) <= 0) {  
                        line = 0;  
                    } else {  
                        line = Integer.parseInt(numline[0]) - 1;  
                    }  
                }  
  
                text.setCurline(line);  
                text.requestFocus();  
                text.setCaretPosition(text.getLineStartOffset(line));  
            } catch (NumberFormatException e) {  
                e.printStackTrace();  
            } catch (BadLocationException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
}  
