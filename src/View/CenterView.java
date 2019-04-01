package View;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class CenterView implements Initializable {
	
	@SuppressWarnings("unused")
	private WarView mainApp;
	@SuppressWarnings("unused")
	private List<Listeners.WarUIEventsListener> allListeners;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allListeners = new LinkedList<Listeners.WarUIEventsListener>();

	}

	public void setMainApp(WarView mainApp) {
		this.mainApp = mainApp;
	}
	
}
