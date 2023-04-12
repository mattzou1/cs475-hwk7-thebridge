import java.util.ArrayList;

public class OneLaneBridge extends Bridge {

    // maximum number of cars that can be on the bridge at once
    private int bridgeLimit;

    // condition variables
    private Object enter = new Object();
    private Object leave = new Object();

    public OneLaneBridge(int bridgeLimit) {
        super();
        this.bridgeLimit = bridgeLimit;
    }

    public void arrive(Car car) throws InterruptedException {
        synchronized(enter) {
            // wait until it's the car can cross
            while (!canCross(car)) {
                enter.wait();
            }
            car.setEntryTime(currentTime);
            //critical section
            synchronized(this){
                bridge.add(car);
                currentTime++;
                System.out.print("Bridge (dir=" + direction + "): ");
                System.out.println(bridge);
            }
        }
    }

    public void exit(Car car) throws InterruptedException {
        synchronized(leave) {
            //check if car is at head of arraylist
            while (!car.equals(bridge.get(0))) {
                leave.wait();
            }
            //critical section 
            synchronized(this){
                bridge.remove(car);
                System.out.print("Bridge (dir=" + direction + "): ");
                System.out.println(bridge); 
            }
            // notify waiting cars that they might be able to enter
            leave.notifyAll();
        }
        synchronized (enter) {
            // notify leaving cars that they might be able to leave
            enter.notifyAll();
        }
    }

    /*
     * Returns true if a car can cross the bridge, false otherwise
     */
    private boolean canCross(Car car) {
        // if the bridge is empty, the car can cross
        if (bridge.size() == 0) {
            direction = car.getDirection(); // set the direction to the car's direction
            return true;
        }

        // if the car is going in the opposite direction of the current traffic flow, it can't cross
        if (direction != car.getDirection()) {
            return false;
        }

        // if the car is going in the same direction as the current traffic flow and there's room on the bridge, it can cross
        if (bridge.size() < bridgeLimit) {
            return true;
        }

        // otherwise, the car can't cross
        return false;
    }
}
