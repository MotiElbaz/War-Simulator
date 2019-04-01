package Objects;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import Handlers.WarFiltrer;
import Handlers.WarFormatter;
import Listeners.OnHitListener;
import Listeners.OnLaunchingListener;
import Model.ProgramMain;

public class Missile extends Thread {

	private final static int MILLIS = 1000;
	private final char INITIAL = 'M';
	private static int idGenerator = 100;
	private String mId;
	private String destination;
	private int launchTime;
	private int flyTime;
	private int damage;
	private Vector<OnHitListener> allHitListeners;
	private Vector<OnLaunchingListener> allLaunchingListeners;
	private String launcherId;
	private FileHandler missileHandler;

	public Missile(String id, String destination, int launchTime, int flyTime, int damage, String launcherId) {
		setMId(id);
		idGenerator++;
		setDestination(destination);
		setLaunchTime(launchTime);
		setFlyTime(flyTime);
		setDamage(damage);
		setLauncherId(launcherId);
		this.allHitListeners = new Vector<OnHitListener>();
		this.allLaunchingListeners = new Vector<OnLaunchingListener>();
	}

	public Missile(String destination, int launchTime, int flyTime, int damage, String launcherId) {
		setMId(null);
		setDestination(destination);
		setLaunchTime(launchTime);
		setFlyTime(flyTime);
		setDamage(damage);
		setLauncherId(launcherId);
		this.allHitListeners = new Vector<OnHitListener>();
		this.allLaunchingListeners = new Vector<OnLaunchingListener>();
	}

	public String getLauncherId() {
		return launcherId;
	}

	public void setLauncherId(String launcherId) {
		this.launcherId = launcherId;
	}

	public String getMId() {
		return this.mId;
	}

	public void setMId(String id) {
		if (id == null)
			this.mId = INITIAL + Integer.toString(idGenerator++);
		else
			this.mId = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(int launchTime) {
		this.launchTime = launchTime;
	}

	public int getFlyTime() {
		return flyTime;
	}

	public void setFlyTime(int flyTime) {
		this.flyTime = flyTime;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Missile ID :");
		builder.append(this.getId());
		builder.append(" , Destination : ");
		builder.append(this.getDestination());
		builder.append(" , Launch Time : ");
		builder.append(this.getLaunchTime());
		builder.append(" , Fly Time : ");
		builder.append(this.getFlyTime());
		builder.append(" , Damage : ");
		builder.append(this.getDamage());
		return builder.toString();
	}

	@Override
	public void run() {
		try {
			setUpHandler();
			notifyLaunchingListeners();
			Thread.sleep(this.getLaunchTime() * MILLIS);
			Thread.sleep(this.getFlyTime() * MILLIS);
			writeToHanbderMissileDetails(false);
			notifyHitListeners();
		} catch (InterruptedException e) {
			// e.printStackTrace();
			writeToHanbderMissileDetails(true);
		} catch (SecurityException | IOException e1) {

		}
	}

	private void writeToHanbderMissileDetails(boolean ifIntercepted) {
		if (!ifIntercepted) {
			ProgramMain.warLogger.log(Level.INFO,
					"Program Destenation: " + getDestination() + "\n" + "Time launching: " + getLaunchTime() + "\n"
							+ "Time Flying: " + getLaunchTime() + "\n" + "Result: hited!" + "Damage: " + getDamage()
							+ "\n");
		} else {
			ProgramMain.warLogger.log(Level.INFO, "Program Destenation: " + getDestination() + "\n" + "Time launching: "
					+ getLaunchTime() + "\n" + "Time Flying: " + getLaunchTime() + "\n" + "Result: destructed!\n");
		}
	}

	private void setUpHandler() throws SecurityException, IOException {
		missileHandler = new FileHandler("missile.txt");
		missileHandler.setFilter(new WarFiltrer(this.getClass().toString()));
		missileHandler.setFormatter(new WarFormatter());
		ProgramMain.warLogger.addHandler(missileHandler);
	}

	private void notifyHitListeners() {
		// Notify WarModel that the missile hit or intercepted , and remove him from
		// activeMissiles .
		for (OnHitListener l : allHitListeners) {
			l.onHitEvent(this);
		}
	}

	private void notifyLaunchingListeners() {
		// Notify WarModel that the missile launched , and add him from activeMissiles .
		for (OnLaunchingListener l : allLaunchingListeners) {
			l.onLaunchingEvent(this);
		}
	}

	public void registerHitListener(OnHitListener l) {
		allHitListeners.add(l);
	}

	public void registerLaunchingListener(OnLaunchingListener l) {
		allLaunchingListeners.add(l);
	}

}
