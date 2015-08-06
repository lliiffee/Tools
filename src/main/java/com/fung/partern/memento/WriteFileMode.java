package com.fung.partern.memento;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

public class WriteFileMode  implements StateDependedAction {  
    public void action(TextState text, String cmd) {  
        String filename[] = cmd.split("w", 2);  
        FileOutputStream fos = null;  
        BufferedWriter bw = null;  
        try {  
            if (!filename[1].equals("")) {  
  
                File file = new File(filename[1]);  
                fos = new FileOutputStream(file);  
  
                bw = new BufferedWriter(new OutputStreamWriter(fos));  
  
                String context = "";  
                String numline[] = cmd.split("w", 2);  
                int savelines;  
  
                if (numline[0].equals("")) {  
                    savelines = text.getLineCount();  
                } else {  
                    savelines = Integer.parseInt(numline[0]);  
                    if (savelines < 0 || savelines > text.getLineCount()) {  
                        JOptionPane.showMessageDialog(text, "Bad Parameter"  
                                + savelines);  
                        return;  
                    }  
                }  
  
                String[] lineString = text.getText().split("\n");  
  
                for (int i = 0; i < savelines; i++) {  
                    context += lineString[i];  
                    context += "\n";  
                }  
  
                bw.write(context);  
                JOptionPane.showMessageDialog(text, "FILE SAVED");  
            } else {  
                JOptionPane.showMessageDialog(text, "FORMAT:[#]wFilename");  
            }  
  
        } catch (FileNotFoundException fnfe) {  
            fnfe.printStackTrace();  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            try {  
                if (bw != null)  
                    bw.close();  
                if (fos != null)  
                    fos.close();  
            } catch (IOException ie) {  
            }  
        }  
  
    }  
  
}  
