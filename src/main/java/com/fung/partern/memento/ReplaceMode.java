package com.fung.partern.memento;

import javax.swing.JOptionPane;

public class ReplaceMode implements StateDependedAction {  
    private String replace(int begin, int end, String[] cmd, boolean askflag) {  
        String context = "";  
        if (begin < 1) {  
            begin = 1;  
        }  
        if (end > cmd.length) {  
            end = cmd.length;  
        }  
        for (int i = 0; i < begin - 1; i++) {  
            context += cmd[i];  
            context += "\n";  
        }  
        for (int i = begin - 1; i < end; i++) {  
            boolean replaceflag = true;  
            if (askflag) {  
                if (JOptionPane.showConfirmDialog(null,  
                        "Do you want to replace the first string with second in line"  
                                + (i + 1) + "?", "Confirm",  
                        JOptionPane.YES_NO_OPTION) != 0) {  
                    replaceflag = false;  
                }  
            }  
            if (replaceflag) {  
                String temp[] = cmd[i].split(" ", 3);  
                if ((temp.length > 1) && (!temp[1].equals(""))) {  
                    temp[0] = temp[1];  
                    String templine = "";  
                    if (temp.length > 2) {  
                        System.out.println(temp[2]);  
                        templine = temp[0] + " " + temp[1] + " " + temp[2];  
                    } else {  
                        templine = temp[0] + " " + temp[1];  
                    }  
  
                    context += templine;  
                    context += "\n";  
                }  
  
            } else {  
                context += cmd[i];  
                context += "\n";  
            }  
        }  
        for (int i = end; i < cmd.length; i++) {  
            context += cmd[i];  
            context += "\n";  
        }  
        return context;  
    }  
  
    @Override  
    public void action(TextState text, String cmd) {  
        String context = "";  
        String[] lineString = text.getText().split("\n");  
        boolean askflag = false;  
        String numline[];  
        if (cmd.contains("?")) {  
            askflag = true;  
        }  
        if (cmd.matches("(\\d{1,}r.*|[\\d]{1,}\\?r.*)")) {  
            if (askflag) {  
                numline = cmd.split("\\?", 2);  
            } else {  
                numline = cmd.split("r", 2);  
            }  
            int beginline = Integer.parseInt(numline[0]);  
            int endline = text.getLineCount();  
            text.setText(this.replace(beginline, endline, lineString, askflag));  
        } else if (cmd  
                .matches("([\\d]{1,},[\\d]{1,}r.*|[\\d]{1,},[\\d]{1,}[\\?]r.*)")) {  
            if (askflag) {  
                numline = cmd.split("\\?", 2);  
            } else {  
                numline = cmd.split("r", 2);  
            }  
            String[] numline2 = numline[0].split(",", 2);  
            int beginline = Integer.parseInt(numline2[0]);  
            int endline = Integer.parseInt(numline2[1]);  
            text.setText(this.replace(beginline, endline, lineString, askflag));  
        } else if (cmd.matches("(,[\\d]{1,}r.*|,[\\d]{1,}[\\?]r.*)")) {  
            int beginline = text.getCurline();  
            int endline = text.getLineCount();  
            text.setText(this.replace(beginline, endline, lineString, askflag));  
        } else {  
            JOptionPane.showMessageDialog(text, "FORMAT:[#][,#][?]r$,$");  
        }  
  
    }  
  
}  
