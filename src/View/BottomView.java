package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import Listeners.WarUIEventsListener;
import Model.WarModel;
import Objects.Launcher;
import Objects.Missile;
import Objects.Statistics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BottomView implements Initializable {

	private ArrayList<WarUIEventsListener> allListeners = new ArrayList<WarUIEventsListener>();
	@SuppressWarnings("unused")
	private WarView mainApp;

	@FXML
	private ChoiceBox<String> type;

	@FXML
	private ChoiceBox<String> launchers;

	@FXML
	private ChoiceBox<String> missiels;

	@FXML
	private ChoiceBox<String> destination;
	@FXML
	private TextField damage;
	@FXML
	private TextField flyTime;

	@FXML
	private TableView<Statistics> tableView;
	@FXML
	private TableColumn<Statistics, Integer> missielsFired;
	@FXML
	private TableColumn<Statistics, Integer> missielsHit;
	@FXML
	private TableColumn<Statistics, Integer> launcherHit;
	@FXML
	private TableColumn<Statistics, Integer> damageCol;

	private ObservableList<Statistics> data = FXCollections.observableArrayList();

	public void setMissiles(ArrayList<Missile> list) {
		ObservableList<String> l = FXCollections.observableArrayList();
		for (Missile m : list) {
			l.add(m.getMId());
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				missiels.setItems(l);
			}
		});
	}

	public void setLaunchers(List<Launcher> newLauchers) {
		ObservableList<String> l = FXCollections.observableArrayList();
		for (Launcher launcher : newLauchers) {
			if (!launcher.isHidden()) {
				l.add(launcher.getLId());
			}
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				launchers.setItems(l);
			}
		});
	}

	public void setMissiles(Set<String> set) {
		ObservableList<String> m = FXCollections.observableArrayList();
		m.addAll(set);
		missiels.setItems(m);
	}

	@FXML
	protected void handleMissileLaunch(ActionEvent event) {
		fireMissileLaunchPressed();
	}

	private void fireMissileLaunchPressed() {
		int intDamage = -1, intFlyTime = -1;
		String destinationMis = destination.getValue();
		if (destinationMis == null) {
			return;
		} else if (destinationMis.isEmpty()) {
			return;
		}
		String missielDamage = damage.getText();
		if (missielDamage == null) {
			return;
		} else if (missielDamage.isEmpty()) {
			return;
		}
		String missielFlyTime = flyTime.getText();
		if (missielFlyTime == null) {
			return;
		} else if (missielFlyTime.isEmpty()) {
			return;
		}
		try {
			intDamage = Integer.parseInt(missielDamage);
			intFlyTime = Integer.parseInt(missielFlyTime);
		} catch (NumberFormatException e) {
			return;
		}
		for (WarUIEventsListener l : allListeners) {
			l.addMissileToUI(destinationMis, intFlyTime, intDamage);
		}
	}

	@FXML
	protected void handleMissileIntercep(ActionEvent event) {
		fireMissileIntercepPressed();
	}

	private void fireMissileIntercepPressed() {
		String missiel = missiels.getValue();
		if (missiel == null) {
			return;
		} else if (missiel.isEmpty()) {
			return;
		}
		for (WarUIEventsListener l : allListeners) {
			l.addMissileInterceptToUI(missiel);
		}
	}

	@FXML
	protected void handleAddLauncherIntercep(ActionEvent event) {
		fireAddLauncherIntercepPressed();
	}

	private void fireAddLauncherIntercepPressed() {
		String launcher = launchers.getValue();
		if (launcher == null) {
			return;
		} else if (launcher.isEmpty()) {
			return;
		}
		for (WarUIEventsListener l : allListeners) {
			l.addLauncherInterceptToUI(launcher);
		}
	}

	@FXML
	protected void handleAddMissDest(ActionEvent event) {
		fireAddMissDestPressed();
	}

	private void fireAddMissDestPressed() {
		for (WarUIEventsListener l : allListeners) {
			l.addMissileDestructorToUI();
		}
	}

	@FXML
	protected void handleAddLauncherDest(ActionEvent event) {
		fireAddLauncherDestPressed();
	}

	private void fireAddLauncherDestPressed() {
		String destType = type.getValue();
		if (destType == null) {
			return;
		} else if (destType.isEmpty()) {
			return;
		}
		for (WarUIEventsListener l : allListeners) {
			l.addLauncherDestructorToUI(destType);
		}
	}

	@FXML
	protected void handleAddLauncher(ActionEvent event) {
		fireAddLauncherPressed();
	}

	private void fireAddLauncherPressed() {
		for (WarUIEventsListener l : allListeners) {
			l.addLauncherToUI();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setTable() {
		missielsFired.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("missilesFired"));
		missielsHit.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("missilesIntercepted"));
		launcherHit.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("launchersIntercepted"));
		damageCol.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("sumDamage"));
		if (tableView.getItems().size() > 0) {
			tableView.getItems().remove(0);
		}
		tableView.setItems(data);
	}

	public void setMainApp(WarView mainApp) {
		this.mainApp = mainApp;
		destination.setItems(FXCollections.observableArrayList("Beer-Sheva", "Ofakim", "Ashkelon", "Sderot", "Netivot",
				"Keryat-Gat"));
		type.setItems(FXCollections.observableArrayList("Plane", "Ship"));
	}

	public void setData() {
		data.add(WarModel.statistics);
		setTable();
	}

	public void registerListener(WarUIEventsListener listener) {
		allListeners.add(listener);
	}

}
