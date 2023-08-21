package clubSimulation;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class AndreBarman extends Thread {
    public static ClubGrid club; //shared club

	GridBlock currentBlock;
	private Random rand;
	private int movingSpeed;
	
	private PeopleLocation myLocation;
	private boolean inRoom;
	private boolean thirsty;
	private boolean wantToLeave;
	public static AtomicBoolean pause = new AtomicBoolean(false);
	
	private int ID; //thread ID 
    //TODO: MAKE HIM START IN BAR AND MOVE BACK AND FORTH
	
	AndreBarman( int ID,  PeopleLocation loc,  int speed) {
		this.ID=ID;
		movingSpeed=speed; //range of speeds for customers
		this.myLocation = loc; //for easy lookups
		inRoom=false; //not in room yet
		thirsty=true; //thirsty when arrive
		wantToLeave=false;	 //want to stay when arrive
		rand=new Random();
		
	}
	
	//getter
	public  boolean inRoom() {
		return inRoom;
	}
	
	//getter
	public   int getX() { return currentBlock.getX();}	
	
	//getter
	public   int getY() {	return currentBlock.getY();	}
	
	//getter
	public   int getSpeed() { return movingSpeed; }

	//setter

	//check to see if user pressed pause button
	private void checkPause() {
		 	
		synchronized(pause){
			while(pause.get()){
				try {
					pause.wait();
				} catch (InterruptedException e) {}
			}
		}
        
    }
	private void startSim() { 	
		try{
			ClubSimulation.latch.await();
		}catch(InterruptedException e){}
			
    }
	
	//get drink at bar
		private void getDrink() throws InterruptedException {
			//FIX SO BARMAN GIVES THE DRINK AND IT IS NOT AUTOMATIC
			thirsty=false;
			System.out.println("Thread "+this.ID + " got drink at bar position: " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep(movingSpeed*5);  //wait a bit
		}
		
	
	public void enterClub() throws InterruptedException {
		currentBlock = club.enterClub(myLocation);  //enter through entrance
		inRoom=true;
		System.out.println("Thread "+this.ID + " entered club at position: " + currentBlock.getX()  + " " +currentBlock.getY() );
		sleep(movingSpeed/2);  //wait a bit at door
	}
	
	public void run() {
		try {
			startSim(); 
			checkPause();
            enterClub(); // ENSURE HIS STARTING POSITION
			checkPause();
			checkPause(); //check whether have been asked to pause
		
			while (inRoom) {
                //TODO: MAKE WANDER BACK AND FORTH TO SERVE DRINKS   
            }
				

		} catch (InterruptedException e1) {  //do nothing
		}
	}
    


}
