package Handlers;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class WarFiltrer implements Filter {

	private String str;

	public WarFiltrer(String name) {
		String[] ary = name.split(" ");
		this.str = ary[1];
	}

	@Override
	public boolean isLoggable(LogRecord log) {
		return log.getSourceClassName().equals(str);
	}

}
