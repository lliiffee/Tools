package com.fung.pingpong;

public class Options {

	/** The singleton @a Options instance. */
	
	private static Options mUniqueInstance=null;
	
	 /** Maximum number of iterations to run the program (defaults to 10). */
	private int mMaxIterations=10;
	
	 /** Maximum number of iterations per "turn" (defaults to 1). */
	private int mMaxTurns=1;
	
	/**
     * Which synchronization to use, e.g., "SEMA", "COND", or "MONOBJ".
     * Defaults to "SEMA".
     */
	private String mSyncMechanism="SEMA";
	/** Method to return the one and only singleton uniqueInstance. */
	public static Options instance() {
		if(mUniqueInstance==null)
			mUniqueInstance=new Options();
		
		return mUniqueInstance;
	}
	
	
	
	

}
