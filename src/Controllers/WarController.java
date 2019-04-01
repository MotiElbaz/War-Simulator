package Controllers;

import Listeners.WarModelEventsListener;
import Listeners.WarUIEventsListener;
import Model.WarModel;
import Objects.DestructedLauncher;
import Objects.DestructedMissile;
import Objects.Launcher;
import Objects.LauncherDestructor;
import Objects.Missile;
import Objects.MissileDestructor;
import Objects.Statistics;
import View.BottomView;
import View.WarView;

public class WarController implements WarModelEventsListener, WarUIEventsListener {

	private WarModel model;
	private BottomView bottomView;
	private WarView warView;

	public WarController(WarModel model, BottomView bottomView, WarView warView) {
		this.model = model;
		this.warView = warView;
		this.bottomView = bottomView;

		this.model.registerListener(this);
		this.bottomView.registerListener(this);
	}

	@Override
	public void addedLauncherToModelEvent(Launcher l) {
		warView.addLauncher(l);
	}

	@Override
	public void addedLauncherDestructorToModelEvent(LauncherDestructor ld) {
		warView.addLauncherDestructor(ld);
	}

	@Override
	public void addedMissileDestructorToModelEvent(MissileDestructor md) {
		warView.addMissileDestructor(md);
	}

	@Override
	public void addedLaunchedMissileToModelEvent(Missile m) {
		warView.launchMissile(m);

	}

	@Override
	public void addedLauncherInterceptToModelEvent(DestructedLauncher dl) {
		warView.interceptLauncher(dl);
	}

	@Override
	public void addedMissileInterceptToModelEvent(DestructedMissile dm) {
		warView.interceptMissile(dm);
	}

	@Override
	public void addMissileToUI(String destination, int flyTime, int damage) {
		model.addMissile(null, null, destination, 0, flyTime, damage);
	}

	@Override
	public void addLauncherToUI() {
		model.addLauncher(null, true);
	}

	@Override
	public void addMissileDestructorToUI() {
		model.addMissileDestructor(null);
	}

	@Override
	public void addMissileInterceptToUI(String targetId) {
		model.interceptMissile(targetId, 0);
	}

	@Override
	public void addLauncherDestructorToUI(String type) {
		model.addLauncherDestructor(type);
	}

	@Override
	public void addLauncherInterceptToUI(String targetId) {
		model.interceptLauncher(targetId, 0);
	}

	public Statistics getStatistics() {
		return this.model.getStatistics();
	}

}
