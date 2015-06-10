package com.fung.pingpong;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 /** 
         * Initializes the Platform singleton with the appropriate
         * PlatformStrategy, which in this case will be the
         * ConsolePlatform.
         */
		PlatformStrategy.instance(new PlatformStrategyFactory(System.out,null).makePlatformStrategy());
		
		 /** Initializes the Options singleton. */
        Options.instance().parseArgs(args);
        
        /**
         * Create a PlayPingPong object to run the designated number of
         * iterations.
         */
        PlayPingPong pingPong=new PlayPingPong(PlatformStrategy.instance(),
        		Options.instance().maxIterations(),
        		Options.instance().maxTurns(),
        		Options.instance().syncMechanism());
        
        /**
         * Start a thread to play ping-pong.
         */
        new Thread (pingPong).start();
        System.out.println("main done...");
        
	}

}
