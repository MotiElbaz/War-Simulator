package Objects;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import Handlers.WarFiltrer;
import Handlers.WarFormatter;
import Listeners.OnInterceptListener;
import Listeners.OnLaunchingListener;
import Model.ProgramMain;

public class MissileDestructor extends Destructor implements Runnable {

	private final static int MILLIS = 1000;
	private final static String INITIAL = "MD";
	private static int idGenerator = 100;
	private Queue<DestructedMissile> destructedMissiles;
	private boolean isRunning;
	private Vector<OnInterceptListener> allInterceptListeners;
	private Vector<OnLaunchingListener> allLaunchingListeners;
	private FileHandler missileDestructorHandler;

	public MissileDestructor(String id) {
		super(id);
		idGenerator++;
		this.destructedMissiles = new LinkedList<DestructedMissile>();
		this.isRunning = true;
		this.allInterceptListeners = new Vector<OnInterceptListener>();
		this.allLaunchingListeners = new Vector<OnLaunchingListener>();
	}

	public MissileDestructor() {
		super(INITIAL + Integer.toString(idGenerator++));
		this.destructedMissiles = new LinkedList<DestructedMissile>();
		this.isRunning = true;
		this.allInterceptListeners = new Vector<OnInterceptListener>();
		this.allLaunchingListeners = new Vector<OnLaunchingListener>();
	}

	public boolean addMissileToDestruct(DestructedMissile dm) {
		if (this.getDestructedMissiles().contains(dm)) {
			return false;
		}
		return this.destructedMissiles.add(dm);
	}

	public DestructedMissile removeMissileToDestruct() {
		return this.destructedMissiles.poll();
	}

	public Queue<DestructedMissile> getDestructedMissiles() {
		return destructedMissiles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MissileDestructor ID : ");
		builder.append(this.getId());
		builder.append(" , Destructed Missiles : \n");
		for (DestructedMissile dm : destructedMissiles) {
			builder.append(dm.toString());
		}
		builder.append("\n");
		return builder.toString();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void stopThread() {
		this.isRunning = false;
	}

	@Override
	public void run() {
		try {
			setUpHandler();
		} catch (SecurityException | IOException e1) {
		}
		while (this.isRunning()) {
			// For some reason with out a line in here the thread of the missile isnt
			// starting ...
			System.out.print("");
			if (!destructedMissiles.isEmpty()) {
				DestructedMissile dm = destructedMissiles.poll();
				try {
					notifyLaunchingListeners(dm);
					Thread.sleep(dm.getDestructAfterLaunch() * MILLIS);
					writeToHanbderMissileDetails(dm);
					notifyInterceptListeners(dm);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private void setUpHandler() throws SecurityException, IOException {
		missileDestructorHandler = new FileHandler("missileDestructor.txt");
		ProgramMain.warLogger.addHandler(missileDestructorHandler);
		missileDestructorHandler.setFilter(new WarFiltrer(this.getClass().toString()));
		missileDestructorHandler.setFormatter(new WarFormatter());
	}

	private void writeToHanbderMissileDetails(DestructedMissile dm) {
		ProgramMain.warLogger.log(Level.INFO,
				"Program Trying to destruct missile #" + dm.getTargetId() + "\n" + "Result: success! \n");

	}

	private void notifyLaunchingListeners(DestructedMissile dm) {
		// Notify WarModel that the interception launched , and notify the controller .
		for (OnLaunchingListener l : allLaunchingListeners) {
			l.onLaunchingEvent(dm);
		}
	}

	public void registerLaunchingListener(OnLaunchingListener l) {
		allLaunchingListeners.add(l);
	}

	private void notifyInterceptListeners(DestructedMissile dm) {
		// Notify WarModel that the missile launched , and add him from activeMissiles .
		for (OnInterceptListener l : allInterceptListeners) {
			l.onInterceptEvent(dm);
		}
	}

	public void registerInterceptListener(OnInterceptListener l) {
		allInterceptListeners.add(l);
	}

}
