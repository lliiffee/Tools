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
	
	
	/** Number of iterations to run the program. */
    public int maxIterations()
    {
        return mMaxIterations;
    }

    /** Number of iterations to run the program. */
    public int maxTurns()
    {
        return mMaxTurns;
    }




    /**
     * Which synchronization to use, e.g., "SEMA" vs. "COND".
     * Defaults to "SEMA".
     */
    public String syncMechanism()
    {
        return mSyncMechanism;
    }
    
    /**
     * Parse command-line arguments and set the appropriate values.
     */
    
	public boolean parseArgs(String[] argv) {
		 for(int argc=0;argc<argv.length;argc+=2)
		 {
			 if("-i".equals(argv[argc]))
			 {
				 mMaxIterations = Integer.parseInt(argv[argc + 1]);
			 
			 }else if (argv[argc].equals("-s"))
	             mSyncMechanism = argv[argc + 1];
	         else if (argv[argc].equals("-t"))
	             mMaxTurns = Integer.parseInt(argv[argc + 1]);
	         else
	             {
	                 printUsage();
	                 return false;
	             }
		 }
	     return true;
		
	}

	


	/** Print out usage and default values. */
	/** Print out usage and default values. */
    public void printUsage()
    {
        PlatformStrategy platform = PlatformStrategy.instance();
        platform.errorLog("Options", "\nHelp Invoked on ");
        platform.errorLog("Options", "[-hist] ");
        platform.errorLog("", "");
        platform.errorLog("", "");

        platform.errorLog("Options", "Usage: ");
        platform.errorLog("Options", "-h: invoke help ");
        platform.errorLog("Options", "-i max-number-of-iterations ");
        platform.errorLog("Options", "-s sync-mechanism (e.g., \"SEMA\", \"COND\", or \"MONOBJ\"");
        platform.errorLog("Options", "-t max-number-of-turns");
    }

    /**
     * Make the constructor private for a singleton.
     */
    private Options()
    {
    }
}