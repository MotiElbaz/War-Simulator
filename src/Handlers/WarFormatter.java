package Handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class WarFormatter extends Formatter {

	public static final String FORMART = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String format(LogRecord arg0) {
		DateFormat dateFormat = new SimpleDateFormat(FORMART);
		Date date = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append(dateFormat.format(date));
		sb.append("\t-->\t");
		sb.append(formatMessage(arg0));
		sb.append("\n");
		return sb.toString();
	}

}
