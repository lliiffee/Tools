package com.fung.partern.memento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class EditorApp extends JFrame { 
 
  private JPanel contentPane; 
 
  private JTextField textField_1; 
  private TextState textPane; 
  private CmdHandler handler = new CmdHandler(); 
 
  /** 
   * Launch the application. 
   */ 
  public static void main(String[] args) { 
    EventQueue.invokeLater(new Runnable() { 
      public void run() { 
        try { 
          EditorApp frame = new EditorApp(); 
          frame.setVisible(true); 
        } catch (Exception e) { 
          e.printStackTrace(); 
        } 
      } 
    }); 
  } 
 
  public JTextArea getContext() { 
    return textPane; 
  } 
 
  /** 
   * Create the frame. 
   * 
   * @throws BadLocationException 
   */ 
  public EditorApp() throws BadLocationException { 
    setFont(new Font("Aharoni", Font.BOLD, 12)); 
    setTitle("LIKER'S EDLIN VERSION 0.9"); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    this.setExtendedState(EditorApp.MAXIMIZED_BOTH); 
    contentPane = new JPanel(); 
    setContentPane(contentPane); 
    contentPane.setLayout(new GridLayout(0, 1, 0, 0)); 
 
    JPanel panel = new JPanel(); 
    contentPane.add(panel); 
    panel.setLayout(new BorderLayout(0, 0)); 
 
    textField_1 = new JTextField(); 
 
    textField_1.setForeground(Color.YELLOW); 
    textField_1.requestFocus(); 
 
    textField_1.setBackground(Color.BLACK); 
    panel.add(textField_1, BorderLayout.SOUTH); 
    textField_1.setColumns(10); 
 
    textPane = new TextState("Initial Material For Testing.#1\r\n" 
        + "TYPE \"i\" TO EDIT. #2\r\nThis is a line #3\r\n" 
        + "This is a line #4\r\nThis is a line #5\r\n" 
        + "This is a line #6\r\nThis is a line #7\r\n" 
        + "This is a line #8\r\nThis is a line #9\r\n" 
        + "This is a line #10\r\nThis is a line #11\r\n" 
        + "This is a line #12\r\nThis is a line #13\r\n" 
        + "This is a line #14\r\nThis is a line #15\r\n" 
        + "This is a line #16\r\nThis is a line #17\r\n" 
        + "This is a line #18\r\nThis is a line #19\r\nEND#20", 0); 
    textPane.setEditable(false); 
    panel.add(textPane, BorderLayout.CENTER); 
 
    textPane.addKeyListener(new KeyAdapter() { 
      public void keyReleased(KeyEvent arg0) { 
        if (arg0.getKeyChar() == KeyEvent.VK_ESCAPE) { 
          textPane.setEditable(false); 
          textField_1.requestFocus(); 
        } 
      } 
    }); 
    textField_1.addKeyListener(new KeyAdapter() { 
      public void keyReleased(KeyEvent arg0) { 
        if (arg0.getKeyChar() == KeyEvent.VK_ENTER) { 
          handler.handler(textField_1, textPane); 
          textField_1.setText("Type commands here."); 
          setTitle("Curren.Line.#" + (textPane.getCurline() + 1)); 
        } 
      } 
    }); 
    textField_1.addMouseListener(new MouseAdapter() { 
      public void mouseClicked(MouseEvent arg0) { 
        textField_1.setText(""); 
      } 
    }); 
 
  } 
}
