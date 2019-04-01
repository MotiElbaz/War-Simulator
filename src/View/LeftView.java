package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Objects.Launcher;
import Objects.LauncherDestructor;
import Objects.MissileDestructor;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

public class LeftView implements Initializable {

	private WarView mainApp;
	@FXML
	private TreeTableView<String> tableView;
	@FXML
	private TreeTableColumn<String, String> col1;
	private TreeItem<String> missLaunchers = new TreeItem<>("Launchers");
	private TreeItem<String> missDest = new TreeItem<>("Missile Destructor");
	private TreeItem<String> missLaunDest = new TreeItem<>("Launcher Destructor");
	private TreeItem<String> root = new TreeItem<>("War");
	private ArrayList<TreeItem<String>> allLaunchDestItems = new ArrayList<>();
	private ArrayList<TreeItem<String>> allMissDestItems = new ArrayList<>();
	private ArrayList<TreeItem<String>> allLaunchersItems = new ArrayList<>();
	private ArrayList<Launcher> launchers;
	private ArrayList<MissileDestructor> missDestructors;
	private ArrayList<LauncherDestructor> launcherDestructos;

	public void addLauncherDestructor(LauncherDestructor ld) {
		launcherDestructos.add(ld);
		TreeItem<String> temp = new TreeItem<>(ld.getId());
		allLaunchDestItems.add(temp);
		missLaunDest.getChildren().setAll(allLaunchDestItems);
	}

	public void addMissDestructor(MissileDestructor md) {
		missDestructors.add(md);
		TreeItem<String> temp = new TreeItem<>(md.getId());
		allMissDestItems.add(temp);
		missDest.getChildren().setAll(allMissDestItems);
	}

	public void addLauncherToTable(Launcher l) {
		launchers.add(l);
		TreeItem<String> temp = new TreeItem<>(l.getLId());
		allLaunchersItems.add(temp);
		missLaunchers.getChildren().setAll(allLaunchersItems);
	}

	@SuppressWarnings("unchecked")
	public void setTable() {
		missLaunchers = new TreeItem<>("Launchers");
		missDest = new TreeItem<>("Missile Destructor");
		missLaunDest = new TreeItem<>("Launcher Destructor");
		root = new TreeItem<>("War");
		launchers = mainApp.getLaunchers();
		missDestructors = mainApp.getMissileDestructors();
		launcherDestructos = mainApp.getLauncherDestructors();

		if (launchers != null && !launchers.isEmpty()) {
			for (Launcher l : launchers) {
				TreeItem<String> temp = new TreeItem<>(l.getLId());
				allLaunchersItems.add(temp);
			}
			missLaunchers.getChildren().setAll(allLaunchersItems);
		}

		if (missDestructors != null && !missDestructors.isEmpty()) {
			for (MissileDestructor md : missDestructors) {
				TreeItem<String> temp = new TreeItem<>(md.getId());
				allMissDestItems.add(temp);
			}
			missDest.getChildren().setAll(allMissDestItems);
		}

		if (launcherDestructos != null && !launcherDestructos.isEmpty()) {
			for (LauncherDestructor ld : launcherDestructos) {
				TreeItem<String> temp = new TreeItem<>(ld.getId());
				allLaunchDestItems.add(temp);
			}
			missLaunDest.getChildren().setAll(allLaunchDestItems);
		}

		col1.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<String, String> param) {
						// TODO Auto-generated method stub
						return new SimpleStringProperty(param.getValue().getValue());
					}

				});
		root.getChildren().setAll(missLaunDest, missDest, missLaunchers);
		tableView.setRoot(root);
	}

	public void setMainApp(WarView mainApp) {
		this.mainApp = mainApp;
		setTable();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void removeLauncherToTable(Launcher launcher) {
		launchers.remove(launcher);
		for (TreeItem<String> item : allLaunchersItems) {
			if (item.getValue().equals(launcher.getLId())) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						allLaunchersItems.remove(item);
					}
				});
			}
		}
		missLaunchers.getChildren().setAll(allLaunchersItems);
	}

}
