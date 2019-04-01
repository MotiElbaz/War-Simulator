package Listeners;

public interface WarUIEventsListener {

	void addMissileToUI(String destination, int flyTime, int damage);

	void addLauncherToUI();

	void addMissileDestructorToUI();

	void addMissileInterceptToUI(String targetId);

	void addLauncherDestructorToUI(String type);

	void addLauncherInterceptToUI(String targetId);

}
