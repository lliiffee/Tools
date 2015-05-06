package com.feng.easymock;

public class ClassUnderTest {
	private Collaborator listener;
    // ...
    public void setListener(Collaborator listener) {
        this.listener = listener;
    }
    public void addDocument(String title, byte[] document) { 
    	listener.documentAdded("test");
    	System.out.println("addDocument....title"); 
    }
    public boolean removeDocument(String title) {
    	listener.documentRemoved("test");
        System.out.println("removeDocment....title"); 
        return true;
    }
    public boolean removeDocuments(String[] titles) {
    	 listener.documentRemoved("test");
    	 System.out.println("removeDocment...String[] titles"); 
         return true;
    }
}
