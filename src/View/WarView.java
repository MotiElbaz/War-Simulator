package View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Controllers.WarController;
import Model.WarModel;
import Objects.DestructedLauncher;
import Objects.DestructedMissile;
import Objects.Launcher;
import Objects.LauncherDestructor;
import Objects.Location;
import Objects.Missile;
import Objects.MissileDestructor;
import Objects.Statistics;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WarView extends Application {

	private WarController c;
	private WarModel model = new WarModel();
	private AnchorPane centerPane;
	private CenterView centerView;
	private LeftView leftView;
	private BottomView bottomView;
	private double launcherX = 475;
	private double launcherY = 450;
	private double missDestructorX = 600;
	private double missDestructorY = 500;
	private double launcherDestructorX = 300;
	private double launcherDestructorY = 300;
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ArrayList<Launcher> launchers = new ArrayList<>();
	private ArrayList<MissileDestructor> missileDestructors = new ArrayList<>();
	private ArrayList<LauncherDestructor> launcherDestructors = new ArrayList<>();
	private ArrayList<Missile> missiles = new ArrayList<>();
	private HashMap<Missile, ImageView> missilesView = new HashMap<>();
	private HashMap<String, Location> cities = new HashMap<>();
	private HashMap<Launcher, Location> launcherLocation = new HashMap<>();
	private HashMap<Launcher, Label> launcherIcons = new HashMap<>();
	private ArrayList<Label> restLocations = new ArrayList<>();

	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarView.class.getResource("Root.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<LauncherDestructor> getLauncherDestructors() {
		return this.launcherDestructors;
	}

	public ArrayList<MissileDestructor> getMissileDestructors() {
		return this.missileDestructors;
	}

	public ArrayList<Launcher> getLaunchers() {
		return this.launchers;
	}

	public void interceptLauncher(DestructedLauncher dl) {
		Launcher launcher = null;
		for (int i = 0; i < launchers.size(); i++) {
			if (i > 0) {
				if (launchers.get(i).getLId().equals(launchers.get(i - 1).getLId())) {
					launchers.remove(i);
					i--;
				}
			}
		}
		List<Launcher> newLauchers = new CopyOnWriteArrayList<Launcher>();
		newLauchers.addAll(launchers);
		for (Launcher l : newLauchers) {
			if (l.getLId().equals(dl.getTargetId())) {
				launcher = l;
				newLauchers.remove(l);
			}
		}
		final Launcher finalLauncher = launcher;
		if (!launcher.isHidden()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Image missImg = new Image("missile.png");
					ImageView missile = new ImageView(missImg);
					// missile.setRotate(270);
					missile.setX(300);
					missile.setY(300);
					missile.setFitWidth(40);
					missile.setFitHeight(80);
					centerPane.getChildren().add(missile);
					final long startNanoTime = System.nanoTime();
					int duration = dl.getDestructTime();
					AnimationTimer timer = new AnimationTimer() {
						double startX = missile.getX();
						double startY = missile.getY();
						double finalX = 500;
						double finalY = 50;
						double middleY = -200;
						double xChange = ((finalX - startX) / (duration));
						double yChangeUp = ((startY - middleY) / (duration / 2));
						double yChangeDown = -((finalY - middleY) / (duration / 2));
						double yChange = yChangeUp;
						double second = 1;

						@Override
						public void handle(long now) {
							// System.out.println(xChange);
							double t = (now - startNanoTime) / 1000000000.0;
							if (second - t <= 0) {
								second++;
								if (t >= duration / 2) {
									yChange = yChangeDown;
									missile.setRotate(180);
								} else {
								}
								missile.setX(missile.getX() + xChange);
								missile.setY(missile.getY() - yChange);
							}
							if (missile.getY() > 500) {
								// this.stop();
							}
							if (t > duration) {
								centerPane.getChildren().remove(missile);
								centerPane.getChildren().remove(launcherIcons.get(finalLauncher));
								this.stop();
							}
						}
					};
					timer.start();
				}
			});
		}
		bottomView.setLaunchers(newLauchers);
		leftView.removeLauncherToTable(launcher);
		bottomView.setData();
	}

	public void addLauncherDestructor(LauncherDestructor ld) {
		launcherDestructors.add(ld);
		Label label = new Label(ld.getId());
		Image locationIcon = new Image("location.png");
		ImageView locationView = new ImageView(locationIcon);
		locationView.setFitWidth(24);
		locationView.setFitHeight(24);
		locationView.setX(launcherDestructorX);
		label.setTranslateX(launcherDestructorX);
		label.setTranslateY(launcherDestructorY);
		locationView.setY(launcherDestructorY);
		label.setGraphic(locationView);
		launcherDestructorX += 24;
		launcherDestructorY -= 24;
		restLocations.add(label);
		centerPane.getChildren().add(restLocations.get(restLocations.size() - 1));
		leftView.addLauncherDestructor(ld);
	}

	public void addMissileDestructor(MissileDestructor md) {
		missileDestructors.add(md);
		Label label = new Label(md.getId());
		Image location = new Image("location.png");
		ImageView locationView = new ImageView(location);
		locationView.setFitWidth(24);
		locationView.setFitHeight(24);
		locationView.setX(missDestructorX);
		locationView.setY(missDestructorY);
		label.setTranslateX(missDestructorX);
		label.setTranslateY(missDestructorY);
		label.setGraphic(locationView);
		missDestructorX += 24;
		missDestructorY -= 24;
		restLocations.add(label);
		centerPane.getChildren().add(restLocations.get(restLocations.size() - 1));
		leftView.addMissDestructor(md);
	}

	public void addLauncher(Launcher l) {
		launchers.add(l);
		Label label = new Label(l.getLId());
		bottomView.setLaunchers(launchers);
		if (l.isHidden()) {
			leftView.addLauncherToTable(l);
			return;
		}
		Image locationIcon = new Image("location.png");
		ImageView locationView = new ImageView(locationIcon);
		locationView.setFitWidth(24);
		locationView.setFitHeight(24);
		locationView.setX(launcherX);
		locationView.setY(launcherY);
		label.setTranslateX(launcherX);
		label.setTranslateY(launcherY);
		label.setGraphic(locationView);
		Location locationP = new Location(launcherX, launcherY);
		launcherLocation.put(l, locationP);
		launcherX += 24;
		launcherY -= 24;
		launcherIcons.put(l, label);
		centerPane.getChildren().add(label);
		leftView.addLauncherToTable(l);
	}

	public void interceptMissile(DestructedMissile dm) {
		for (Missile m : missilesView.keySet()) {
			if (m.getMId().equals(dm.getTargetId())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						centerPane.getChildren().remove(missilesView.get(m));
					}
				});
				missiles.remove(m);
				bottomView.setMissiles(missiles);
				missilesView.remove(m);
			}
		}
	}

	public void launchMissile(Missile m) {
		missiles.add(m);
		bottomView.setMissiles(missiles);
		Image missImg = new Image("missile.png");
		ImageView missile = new ImageView(missImg);
		// missile.setRotate(270);
		missile.setX(475);
		missile.setY(450);
		missile.setFitWidth(40);
		missile.setFitHeight(80);
		missilesView.put(m, missile);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				centerPane.getChildren().add(missile);
				final long startNanoTime = System.nanoTime();
				int duration = m.getFlyTime();
				AnimationTimer timer = new AnimationTimer() {
					double startX = missile.getX();
					double startY = missile.getY();
					double finalX = cities.get(m.getDestination()).getX();
					double finalY = cities.get(m.getDestination()).getY();
					double middleY = -200;
					double xChange = ((finalX - startX) / (duration));
					double yChangeUp = ((startY - middleY) / (duration / 2));
					double yChangeDown = -((finalY - middleY) / (duration / 2));
					double yChange = yChangeUp;
					double second = 1;

					@Override
					public void handle(long now) {
						// System.out.println(xChange);
						double t = (now - startNanoTime) / 1000000000.0;
						if (second - t <= 0) {
							second++;
							if (t >= duration / 2) {
								yChange = yChangeDown;
								missile.setRotate(180);
							} else {
							}
							missile.setX(missile.getX() + xChange);
							missile.setY(missile.getY() - yChange);
						}
						if (missile.getY() > 500) {
							// this.stop();
						}
						if (t > duration) {
							missilesView.remove(m);
							missiles.remove(m);
							bottomView.setMissiles(missiles);
							centerPane.getChildren().remove(missile);
							this.stop();

						}
					}
				};
				timer.start();
			}
		});
		bottomView.setData();
	}

	public void showCenter() {
		try {
			// Load main overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarView.class.getResource("Center.fxml"));
			this.centerPane = (AnchorPane) loader.load();
			Image image = new Image("map.jpg");
			ImageView img = new ImageView(image);
			img.fitWidthProperty().bind(this.centerPane.widthProperty());
			img.fitHeightProperty().bind(this.centerPane.heightProperty());
			this.centerPane.getChildren().add(img);
			rootLayout.setCenter(this.centerPane);
			this.centerView = loader.getController();
			this.centerView.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showBottom() {
		try {
			// Load main overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarView.class.getResource("Bottom.fxml"));
			AnchorPane bottom = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setBottom(bottom);

			this.bottomView = loader.getController();
			this.bottomView.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showLeft() {
		try {
			// Load main overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarView.class.getResource("Left.fxml"));
			AnchorPane left = (AnchorPane) loader.load();

			rootLayout.setLeft(left);

			this.leftView = loader.getController();
			this.leftView.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		cities.put("Beer-Sheva", new Location(1100, 350));
		cities.put("Ofakim", new Location(850, 275));
		cities.put("Ashkelon", new Location(825, -100));
		cities.put("Sderot", new Location(850, 50));
		cities.put("Netivot", new Location(825, 150));
		cities.put("Keryat-Gat", new Location(1050, -50));
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("War Simulation");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				e.consume(); // to prevent the application from default closing
				CloseApplicationUtil.closeApplication(WarView.this);
			}
		});
		primaryStage.setMaximized(true);
		primaryStage.centerOnScreen();
		primaryStage.fullScreenProperty();
		initRootLayout();

		showCenter();
		showBottom();
		showLeft();
		this.c = new WarController(this.model, this.bottomView, this);
		this.bottomView.setData();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Statistics getStatistics() {
		return this.c.getStatistics();
	}

}
