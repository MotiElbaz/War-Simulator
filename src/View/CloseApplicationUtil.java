package View;

import javafx.application.Platform;

public class CloseApplicationUtil {

	public static void closeApplication(WarView theApplication) {
		/*
		 * DialogResponse response =
		 * Dialogs.showConfirmDialog(theApplication.getPrimaryStage(),
		 * "Are you sure you want to exit?", "Confirm Dialog", "Goodbye");
		 * 
		 * if (response == DialogResponse.YES) {
		 */
		if (true) {
			Platform.exit();
		}
	}

}
