package Model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import Listeners.OnHitListener;
import Listeners.OnInterceptListener;
import Listeners.OnLaunchingListener;
import Listeners.WarModelEventsListener;
import Objects.DestructedLauncher;
import Objects.DestructedMissile;
import Objects.Launcher;
import Objects.LauncherDestructor;
import Objects.Missile;
import Objects.MissileDestructor;
import Objects.Statistics;

public class WarModel implements OnHitListener, OnLaunchingListener, OnInterceptListener {

	private final int MAX = 4;
	private final int MIN = 1;
	private ArrayList<Launcher> activeLaunchers;
	private ArrayList<Missile> activeMissiles;
	private ArrayList<LauncherDestructor> launchersDestructors;
	private ArrayList<MissileDestructor> missileDestructors;
	private Vector<WarModelEventsListener> listeners;
	public static Statistics statistics;

	public WarModel() {
		this.activeLaunchers = new ArrayList<Launcher>();
		this.activeMissiles = new ArrayList<Missile>();
		this.launchersDestructors = new ArrayList<LauncherDestructor>();
		this.missileDestructors = new ArrayList<MissileDestructor>();
		this.listeners = new Vector<WarModelEventsListener>();
		WarModel.statistics = new Statistics();
	}

	public Launcher addLauncher(String id, boolean isHidden) {
		Launcher newLauncher = null;
		Random random = new Random();
		if (id == null)
			newLauncher = new Launcher(random.nextBoolean());
		else
			newLauncher = new Launcher(id, isHidden);
		newLauncher.start();
		activeLaunchers.add(newLauncher);
		fireAddLauncherEvent(newLauncher);
		return newLauncher;
	}

	public Missile addMissile(String idLauncher, String idMissile, String destination, int launchTime, int flyTime,
			int damage) {
		Missile newMissile = null;
		Random random = new Random();
		int randomLauncher = random.nextInt(activeLaunchers.size()) - MIN;
		if (randomLauncher < 0) {
			randomLauncher = 0;
		}
		// If the missile details come from the user then random the launch time value
		if (launchTime == 0) {
			newMissile = new Missile(destination, random.nextInt(MAX) + MIN, flyTime, damage,
					activeLaunchers.get(randomLauncher).getLId());
		} else {
			newMissile = new Missile(idMissile, destination, launchTime, flyTime, damage, idLauncher);
		}
		newMissile.registerHitListener(this);
		newMissile.registerLaunchingListener(this);
		// This line for checking - need to be deleted
		activeLaunchers.get(randomLauncher).addMissile(newMissile);
		return newMissile;
	}

	public LauncherDestructor addLauncherDestructor(String type) {
		LauncherDestructor ld = new LauncherDestructor(type);
		ld.registerInterceptListener(this);
		Thread t = new Thread(ld);
		t.start();
		launchersDestructors.add(ld);
		fireAddLauncherDestructorEvent(ld);
		return ld;
	}

	public DestructedLauncher interceptLauncher(String targetId, int destructTime) {
		if (this.launchersDestructors.isEmpty()) {
			return null;
		}
		for (Launcher l : this.activeLaunchers) {
			if (l.getLId().equals(targetId)) {
				if (l.isHidden()) {
					return null;
				}
			}
		}
		Random random = new Random();
		int randomLD = random.nextInt(launchersDestructors.size()) - MIN;
		if (randomLD < 0) {
			randomLD = 0;
		}
		DestructedLauncher dl;
		if (destructTime == 0) {
			dl = new DestructedLauncher(targetId, random.nextInt(MAX) + MIN,
					this.launchersDestructors.get(randomLD).getId());
		} else {
			dl = new DestructedLauncher(targetId, destructTime, this.launchersDestructors.get(randomLD).getId());
		}
		// This line for checking - need to be deleted
		dl.setDestructTime(5);
		this.launchersDestructors.get(randomLD).addLauncherToDestruct(dl);
		return dl;
	}

	public MissileDestructor addMissileDestructor(String id) {
		MissileDestructor md;
		if (id != null) {
			md = new MissileDestructor(id);
		} else {
			md = new MissileDestructor();
		}
		md.registerInterceptListener(this);
		Thread t = new Thread(md);
		t.start();
		missileDestructors.add(md);
		fireAddMissileDestructorEvent(md);
		return md;
	}

	public DestructedMissile interceptMissile(String targetId, int destructAfterLaunch) {
		if (this.missileDestructors.isEmpty()) {
			return null;
		}
		Random random = new Random();
		int randomMD = random.nextInt(missileDestructors.size()) - MIN;
		if (randomMD < 0) {
			randomMD = 0;
		}
		DestructedMissile dm;
		if (destructAfterLaunch == 0) {
			dm = new DestructedMissile(targetId, random.nextInt(MAX) + MIN,
					this.missileDestructors.get(randomMD).getId());
		} else {
			dm = new DestructedMissile(targetId, destructAfterLaunch, this.missileDestructors.get(randomMD).getId());
		}
		this.missileDestructors.get(randomMD).addMissileToDestruct(dm);
		return dm;
	}

	private void fireAddLauncherEvent(Launcher launcher) {
		for (WarModelEventsListener l : listeners) {
			l.addedLauncherToModelEvent(launcher);
		}
	}

	private void fireLaunchedMissileEvent(Missile m) {
		for (WarModelEventsListener l : listeners) {
			l.addedLaunchedMissileToModelEvent(m);
		}
	}

	private void fireAddLauncherDestructorEvent(LauncherDestructor ld) {
		for (WarModelEventsListener l : listeners) {
			l.addedLauncherDestructorToModelEvent(ld);
		}
	}

	private void fireLauncherInterceptEvent(DestructedLauncher dl) {
		for (WarModelEventsListener l : listeners) {
			l.addedLauncherInterceptToModelEvent(dl);
		}
	}

	private void fireAddMissileDestructorEvent(MissileDestructor md) {
		for (WarModelEventsListener l : listeners) {
			l.addedMissileDestructorToModelEvent(md);
		}
	}

	private void fireMissileInterceptEvent(DestructedMissile dm) {
		for (WarModelEventsListener l : listeners) {
			l.addedMissileInterceptToModelEvent(dm);
		}
	}

	@Override
	public void onHitEvent(Missile m) {
		// When the missile hit or intercepted , removing him from the activeMissiles .
		if (this.activeMissiles.contains(m)) {
			this.activeMissiles.remove(m);
			statistics.addSumDamage(m.getDamage());
		}
	}

	@Override
	public void onLaunchingEvent(Object o) {
		if (o instanceof Missile) {
			Missile m = (Missile) o;
			if (!this.activeMissiles.contains(m)) {
				this.activeMissiles.add(m);
				statistics.addMissileFired();
			}
			fireLaunchedMissileEvent(m);
		}
	}

	@Override
	public void onInterceptEvent(Object o) {
		// When Launcher got intercepted or the missile got intercepted .
		if (o instanceof DestructedLauncher) {
			DestructedLauncher dl = (DestructedLauncher) o;
			for (Launcher l : activeLaunchers) {
				if (l.getLId().equals(dl.getTargetId())) {
					l.stopThread();
					statistics.addLauncherIntercepted();
					fireLauncherInterceptEvent(dl);
				}
			}
		} else if (o instanceof DestructedMissile) {
			DestructedMissile dm = (DestructedMissile) o;
			for (Missile m : activeMissiles) {
				if (m.getMId().equals(dm.getTargetId())) {
					m.interrupt();
					statistics.addMissileIntercepted();
					fireMissileInterceptEvent(dm);
				}
			}
		}
	}

	public void registerListener(WarModelEventsListener listener) {
		listeners.add(listener);
	}

	public Statistics getStatistics() {
		return WarModel.statistics.getThis();
	}

}
