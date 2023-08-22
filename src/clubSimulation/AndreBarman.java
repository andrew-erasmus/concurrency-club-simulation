package clubSimulation;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class AndreBarman extends Thread {
    public static ClubGrid club; // shared club

    GridBlock currentBlock;
    private Random rand;
    private int movingSpeed;

    private PeopleLocation myLocation;
    private boolean inRoom;
    public static AtomicBoolean pause = new AtomicBoolean(false);

    private int ID; // thread ID

    AndreBarman(int ID, PeopleLocation loc, int speed) {
        this.ID = ID;
        movingSpeed = speed;
        this.myLocation = loc; // for easy lookups
        inRoom = false; // not in room yet
        rand = new Random();

    }

    // getter
    public boolean inRoom() {
        return inRoom;
    }

    // getter
    public int getX() {
        return currentBlock.getX();
    }

    // getter
    public int getY() {
        return currentBlock.getY();
    }

    // getter
    public int getSpeed() {
        return movingSpeed;
    }

    // setter

    // check to see if user pressed pause button
    private void checkPause() {

        synchronized (pause) {
            while (pause.get()) {
                try {
                    pause.wait();
                } catch (InterruptedException e) {
                }
            }
        }

    }

    private void startSim() {
        try {
            ClubSimulation.latch.await();
        } catch (InterruptedException e) {
        }

    }


    public void enterClub() throws InterruptedException {
        currentBlock = club.enterAndre(myLocation); // Start at starting point
        inRoom = true;
    }

    private void serve() throws InterruptedException {
        // MAKE SO THAT GOES ONLY BACK AND FORTH
        int x_mv = rand.nextInt(3) - 1; // -1,0 or 1
        int y_mv = 0;
        currentBlock = club.moveAndre(currentBlock, x_mv, y_mv, myLocation);
        sleep(movingSpeed);

    }

    public void run() {
        try {
            startSim();
			checkPause();
			myLocation.setArrived();
			checkPause(); //check whether have been asked to pause
           // inRoom = true;
			enterClub();

           // while (inRoom) {
                // TODO: MAKE WANDER BACK AND FORTH TO SERVE DRINKS
             //   serve();
              //  System.out.println("mamama");
           // }

        } catch (InterruptedException e1) { // do nothing
        }
    }
    //TODO: FIX ANDRE - DISCUSS HOW TO START

}
