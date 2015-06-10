package com.fung.pingpong;

public abstract class PlatformStrategy {

	/** The singleton @a PlatformStrategy instance. */
    private static PlatformStrategy mUniqueInstance = null;
    
    
    /** 
     * Method that sets a new PlatformStrategy singleton and returns
     * the one and only singleton instance.
     */
	public static PlatformStrategy instance(PlatformStrategy platform) {
		 return mUniqueInstance = platform;
		
	}

	

	public static PlatformStrategy instance() {
		// TODO Auto-generated method stub
		return mUniqueInstance;
	}
	
	/** Do any initialization needed to start a new game. */
    public abstract void begin();

    /** Print the outputString to the display. */
    public abstract void print(String outputString);

    /** Indicate that a game thread has finished running. */
    public abstract void done();

    /** Barrier that waits for all the game threads to finish. */
    public abstract void awaitDone();

    /** 
     * Returns the name of the platform in a string. e.g., Android or
     * a JVM.
     */
    public abstract String platformName();

    /**
     * Error log formats the message and displays it for the debugging
     * purposes.
     */
    public abstract void errorLog(String javaFile, String errorMessage);

    /**
     * Make the constructor protected for a singleton.
     */
    protected PlatformStrategy() {}

}
