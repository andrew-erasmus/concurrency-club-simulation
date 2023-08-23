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

        synchronized (Clubgoer.pause) {
            while (Clubgoer.pause.get()) {
                try {
                    Clubgoer.pause.wait();
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

    private void serve(Boolean reverse) throws InterruptedException {
        // MAKE SO THAT GOES ONLY BACK AND FORTH
        int x_mv;
        if (reverse == false) {
            x_mv = 1;
        }else{
            x_mv= -1;
        }

        int y_mv = 0;
        currentBlock = club.moveAndre(currentBlock, x_mv, y_mv, myLocation);
        sleep(movingSpeed);

    }

    public void run() {
        try {
            startSim();
            checkPause();
            myLocation.setArrived();
            checkPause(); // check whether have been asked to pause
            // inRoom = true;
            enterClub();
            checkPause();
            int counter = 0;
            int length = club.getMaxX();
            Boolean reverse = false;

            while (inRoom) {
                // TODO: MAKE WANDER BACK AND FORTH TO SERVE DRINKS
                if (counter == length) {
                    reverse = !reverse;
                    counter = 0;
                }
                serve(reverse);
                checkPause();
                counter++;

            }

        } catch (InterruptedException e1) { // do nothing
        }
    }
    // TODO: FIX ANDRE - MAKE HIM PAUSE, SERVE DRINKINGS AND MOVE UP AND DOWN

}
