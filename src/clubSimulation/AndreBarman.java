package clubSimulation;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

//Class for Andre the barman
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

        synchronized (Clubgoer.pause) { //lock on the clubgoer pause
            while (Clubgoer.pause.get()) { //when the user pressed pause
                try {
                    Clubgoer.pause.wait(); //wait until the user presses resume
                } catch (InterruptedException e) {
                }
            }
        }

    }

    private void startSim() { 
        try {
            ClubSimulation.latch.await(); //wait until the latch open
        } catch (InterruptedException e) {
        }

    }

    public void enterClub() throws InterruptedException {
        currentBlock = club.enterAndre(myLocation); // Start at starting point
        inRoom = true;
    }

    private void serve(Boolean reverse) throws InterruptedException { //serve patrons drinks
        
        int x_mv;
        if (reverse == false) {
            x_mv = 1; //make the barman move right
        }else{
            x_mv= -1; //make the barman move left
        }

        int y_mv = 0; //make the barman stay at the same y level
        currentBlock = club.moveAndre(currentBlock, x_mv, y_mv, myLocation); //make the thread move
        sleep(movingSpeed);

        synchronized(Clubgoer.served){ //lock on the served atomic boolean
            GridBlock patronBlock = Clubgoer.club.whichBlock(currentBlock.getX(),currentBlock.getY()-1); //gets the block above andre at the bar
            if(patronBlock.occupied()){ //checks if there is a patron waiting to be served
                Clubgoer.served.notify(); //tell the thread that they have been served by Andre
                Clubgoer.served.set(true); //set the served boolean to true
                sleep(movingSpeed);
            }
        }

    }

    public void run() {
        try {
            startSim();
            checkPause();
            myLocation.setArrived();
            checkPause(); // check whether have been asked to pause
            enterClub();
            checkPause();

            int counter = 0; //counts how many times andre has moved in the grid
            int length = club.getMaxX(); //the length of the club's width
            Boolean reverse = false; //tells andre whether to move backwards or forwards

            while (inRoom) {
                
                if (counter == length) { //if andre is at the edge of the grid
                    reverse = !reverse; //reverse his movement direction
                    counter = 0;
                }
                serve(reverse); //serve the patron
                sleep(movingSpeed/5);
                checkPause();
                counter++;

            }

        } catch (InterruptedException e1) { // do nothing
        }
    }

}
