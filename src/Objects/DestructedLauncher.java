package Objects;

public class DestructedLauncher extends Destructor {

	private final static String INITIAL = "DL";
	private static int idGenerator = 100;
	private String targetId;
	private int destructTime;
	private String ldId;

	public DestructedLauncher(String targetId, int destructTime, String ldId) {
		super(INITIAL + Integer.toString(idGenerator++));
		setDestructTime(destructTime);
		setTargetId(targetId);
		setLdId(ldId);
	}

	public String getTargetId() {
		return this.targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getLdId() {
		return this.ldId;
	}

	public void setLdId(String ldId) {
		this.ldId = ldId;

	}

	public int getDestructTime() {
		return destructTime;
	}

	public void setDestructTime(int destructTime) {
		this.destructTime = destructTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DestructLauncher ID : ");
		builder.append(this.getId());
		builder.append(" , Target ID : ");
		builder.append(this.getTargetId());
		builder.append(" , Destruct Time : ");
		builder.append(this.getDestructTime());
		builder.append("\n");
		return builder.toString();
	}

}
