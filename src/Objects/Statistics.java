package Objects;

public class Statistics {

	private int missilesFired;
	private int missilesIntercepted;
	private int launchersIntercepted;
	private int sumDamage;

	public Statistics() {
		this.missilesFired = 0;
		this.missilesIntercepted = 0;
		this.launchersIntercepted = 0;
		this.sumDamage = 0;
	}

	public Statistics getThis() {
		return this;
	}

	public int getMissilesFired() {
		return missilesFired;
	}

	public void addMissileFired() {
		this.missilesFired++;
	}

	public int getMissilesIntercepted() {
		return missilesIntercepted;
	}

	public void addMissileIntercepted() {
		this.missilesIntercepted++;
	}

	public int getLaunchersIntercepted() {
		return launchersIntercepted;
	}

	public void addLauncherIntercepted() {
		this.launchersIntercepted++;
	}

	public int getSumDamage() {
		return sumDamage;
	}

	public void addSumDamage(int sumDamage) {
		this.sumDamage += sumDamage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Missiles Fired : ");
		builder.append(missilesFired);
		builder.append(" , Missiles Intercepted : ");
		builder.append(missilesIntercepted);
		builder.append(" , Launchers Intercepted ");
		builder.append(launchersIntercepted);
		builder.append(" , Sum of Damage : ");
		builder.append(sumDamage);
		builder.append(" . ");
		return builder.toString();
	}

}
