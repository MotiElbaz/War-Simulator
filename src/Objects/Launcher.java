package Objects;

import java.util.LinkedList;
import java.util.Queue;

public class Launcher extends Thread {

	private final char INITIAL = 'L';
	private static int idGenerator = 100;
	private String lId;
	private boolean isHidden;
	private boolean isActive;
	private boolean hiddenFlag;
	private Queue<Missile> waitingMissiles;
	private Missile activeMissile;

	public Launcher(String id, boolean isHidden) {
		setLId(id);
		idGenerator++;
		setHidden(isHidden);
		this.isActive = true;
		this.hiddenFlag = isHidden;
		this.activeMissile = null;
		this.waitingMissiles = new LinkedList<Missile>();
	}

	public Launcher(boolean isHidden) {
		setLId(null);
		setHidden(isHidden);
		this.isActive = true;
		this.hiddenFlag = isHidden;
		this.activeMissile = null;
		this.waitingMissiles = new LinkedList<Missile>();
	}

	public boolean addMissile(Missile m) {
		if (this.waitingMissiles.contains(m)) {
			return false;
		}
		return this.waitingMissiles.add(m);
	}

	public Missile pollMissile() {
		return this.waitingMissiles.poll();
	}

	public String getLId() {
		return lId;
	}

	public void setLId(String id) {
		if (id == null)
			this.lId = INITIAL + Integer.toString(idGenerator++);
		else
			this.lId = id;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Launcher ID : ");
		builder.append(this.getLId());
		builder.append(" , Is Hidden : ");
		builder.append(this.isHidden());
		builder.append(" , All Missiles : \n");
		for (Missile m : this.waitingMissiles) {
			builder.append(m.toString());
		}
		return builder.toString();
	}

	public void stopThread() {
		this.isActive = false;
	}

	@Override
	public void run() {
		while (this.isActive()) {
			if (this.activeMissile != null) {
				if (!this.activeMissile.isAlive()) {
					if (this.hiddenFlag) {
						this.setHidden(true);
					}
					if (!waitingMissiles.isEmpty()) {
						this.activeMissile = this.pollMissile();
						if (this.hiddenFlag) {
							this.setHidden(false);
						}
						this.activeMissile.start();
					}
				}
			} else {
				// For some reason with out a line in here the thread of the missile isnt
				// starting ...
				System.out.print("");
				if (!this.waitingMissiles.isEmpty()) {
					this.activeMissile = this.pollMissile();
					if (this.hiddenFlag) {
						this.setHidden(false);
					}
					this.activeMissile.start();
				}
			}
		}
	}

}
