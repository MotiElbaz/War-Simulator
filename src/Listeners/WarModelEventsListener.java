package Listeners;

import Objects.DestructedLauncher;
import Objects.DestructedMissile;
import Objects.Launcher;
import Objects.LauncherDestructor;
import Objects.Missile;
import Objects.MissileDestructor;

public interface WarModelEventsListener {

	void addedLauncherToModelEvent(Launcher l);

	void addedLauncherDestructorToModelEvent(LauncherDestructor ld);

	void addedMissileDestructorToModelEvent(MissileDestructor md);

	void addedLaunchedMissileToModelEvent(Missile m);

	void addedLauncherInterceptToModelEvent(DestructedLauncher dl);

	void addedMissileInterceptToModelEvent(DestructedMissile dm);

}
