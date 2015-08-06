package com.fung.partern.memento;

import javax.swing.JTextArea;

public class TextState extends JTextArea {  
    private int pageline = 10;  
    private int curline = 0;  
  
    public int getPageline() {  
        return pageline;  
    }  
  
    public void setPageline(int pageline) {  
        this.pageline = pageline;  
    }  
  
    public int getCurline() {  
        return curline;  
    }  
  
    public void setCurline(int curline) {  
        this.curline = curline;  
    }  
  
    public TextState(String cmd, int curline) {  
        this.setText(cmd);  
        this.curline = curline;  
    }  
}
