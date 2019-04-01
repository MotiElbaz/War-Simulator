package Objects;

public class DestructedMissile extends Destructor {

	private final static String INITIAL = "DM";
	private static int idGenerator = 100;
	private String targetId;
	private int destructAfterLaunch;
	private String mdId;

	public DestructedMissile(String targetId, int destructAfterLaunch, String mdId) {
		super(INITIAL + Integer.toString(idGenerator++));
		setDestructAfterLaunch(destructAfterLaunch);
		setTargetId(targetId);
		setMdId(mdId);
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	public int getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(int destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DestructMissile ID : ");
		builder.append(this.getId());
		builder.append(" , Destruct After Launch : ");
		builder.append(this.getDestructAfterLaunch());
		builder.append("\n");
		return builder.toString();
	}

}
