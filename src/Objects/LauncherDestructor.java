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

public class LauncherDestructor extends Destructor implements Runnable {

	private final static int MILLIS = 1000;
	private final static String INITIAL = "LD";
	private static int idGenerator = 100;
	private String type;
	private Queue<DestructedLauncher> destructedLaunchers;
	private Vector<OnLaunchingListener> allLaunchingListeners;
	private Vector<OnInterceptListener> allInterceptListeners;
	private boolean isRunning;
	private FileHandler distractorLauncherHandler;

	public LauncherDestructor(String type) {
		super(INITIAL + Integer.toString(idGenerator++));
		setType(type);
		this.destructedLaunchers = new LinkedList<DestructedLauncher>();
		this.isRunning = true;
		this.allInterceptListeners = new Vector<OnInterceptListener>();
		this.allLaunchingListeners = new Vector<OnLaunchingListener>();
	}

	public boolean addLauncherToDestruct(DestructedLauncher dl) {
		if (this.destructedLaunchers.contains(dl)) {
			return false;
		}
		return this.destructedLaunchers.add(dl);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LauncherDestructor ID : ");
		builder.append(this.getId());
		builder.append(" , Type : ");
		builder.append(this.getType());
		builder.append(" , Destructed Launchers : ");
		for (DestructedLauncher dl : destructedLaunchers) {
			builder.append(dl.toString());
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
			if (!destructedLaunchers.isEmpty()) {
				DestructedLauncher dl = this.destructedLaunchers.poll();
				try {
					notifyLaunchingListeners(dl);
					Thread.sleep(dl.getDestructTime() * MILLIS);
					writeToHanbderLauncherDestructorDetails(dl);
					notifyInterceptListeners(dl);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private void setUpHandler() throws SecurityException, IOException {
		distractorLauncherHandler = new FileHandler("LaunchersDestractor.txt");
		distractorLauncherHandler.setFilter(new WarFiltrer(this.getClass().toString()));
		distractorLauncherHandler.setFormatter(new WarFormatter());
		ProgramMain.warLogger.addHandler(distractorLauncherHandler);
	}

	private void writeToHanbderLauncherDestructorDetails(DestructedLauncher dl) {
		String str = "success!";
		ProgramMain.warLogger.log(Level.INFO, "Program Launcher Destructor #" + getId() + "\n"
				+ "Trying to destruct launcher # " + dl.getTargetId() + "\n" + "Destruct result: " + str + "\n");
	}

	private void notifyLaunchingListeners(DestructedLauncher dl) {
		// Notify WarModel that the interception launched , and notify the controller .
		for (OnLaunchingListener l : allLaunchingListeners) {
			l.onLaunchingEvent(dl);
		}
	}

	public void registerLaunchingListener(OnLaunchingListener l) {
		allLaunchingListeners.add(l);
	}

	private void notifyInterceptListeners(DestructedLauncher dl) {
		// Notify WarModel that the Launcher intercepted , and remove him from
		// activeMissiles .
		for (OnInterceptListener l : allInterceptListeners) {
			l.onInterceptEvent(dl);
		}
	}

	public void registerInterceptListener(OnInterceptListener l) {
		allInterceptListeners.add(l);
	}

}
