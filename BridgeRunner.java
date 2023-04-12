/**
 * Runs all threads
 */

public class BridgeRunner {

	public static void main(String[] args) {

		// check command line inputs
        if (args.length != 2) {
            System.out.println("Usage: javac BridgeRunner <bridge limit> <num cars>");
            return;
        }
        
        // parse the bridge limit and number of cars from command line arguments
        int bridgeLimit, numCars;
        try {
            bridgeLimit = Integer.parseInt(args[0]);
            numCars = Integer.parseInt(args[1]);
        } 
		catch (NumberFormatException e) {
            System.out.println("Error: Invalid arguments, please provide two integers");
            return;
        }
        
        // check if bridge limit or number of cars are negative
        if (bridgeLimit <= 0 || numCars <= 0) {
            System.out.println("Error: bridge limit and/or num cars must be positive.");
            return;
        }
        
        // instantiate the bridge
        OneLaneBridge bridge = new OneLaneBridge(bridgeLimit);
        
        // allocate space for threads
        Thread[] carThreads = new Thread[numCars];
		// start then join the threads
        for (int i = 0; i < numCars; i++) {
            Car car = new Car(i, bridge);
            carThreads[i] = new Thread(car);
            carThreads[i].start();
        }
        
        // wait for all car threads to complete
        for (int i = 0; i < numCars; i++) {
            try {
                carThreads[i].join();
            } 
			catch (InterruptedException e) {
                System.out.println("Error: Interrupted while waiting for car threads to complete");
            }
        }
        
		System.out.println("All cars have crossed!!");
	}

}