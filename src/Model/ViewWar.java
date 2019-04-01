package Model;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

public class ViewWar implements FinalValuesInterface{

	private WarModel theWar = null;
	private Scanner in =new Scanner(System.in);
	
	public ViewWar(WarModel theWar) {
		this.theWar = theWar;
		printTheMenu();
		chooseOptions();
	}

	public ViewWar(JSONParser parser, WarModel theWar) {
		WarReader detailsFromJSON =  new WarReader(parser, theWar);
		detailsFromJSON.start();
		chooseOptions(); 
	}
	
	
	public void printTheMenu() {
		System.out.println(WELCOME_STR);
		System.out.println(OPTIONS_STR);			
	}
	
	public  void chooseOptions( ) throws SecurityException{
		int option ;
		boolean ifContinue = true;

		while(ifContinue ) {
			printTheMenu();
			option = in.nextInt();
			switch(option) {

			case ADD_LAUNCHER_DISTRACTOR:
				addLauncherDestructorAction();
				break;
			case ADD_MISSILE_DSTRACTOR:
				addMissileDestructorAction();
				break;
			case ADD_LAUNCHER:
				addLauncherAction();
				break;
			case LAUNCH_MISSILE:
				missileLaunchAction();
				break;
			case DESTROY_MISSILES:
				destroyMissileAction();
				break;
			case DESTROY_LAUNCHERS:
				destroyLauncherAction();
				break;
			case SHOW_DETAILS:
				showDetails();
				break;
			case EXIT:
				System.out.println("Bye bye");
				ifContinue  = false;
				in.close();
				break;
			}
		}

	}

	private void showDetails() {
		System.out.println(WarModel.statistics);
	}

	private void destroyLauncherAction() {
		System.out.println("Enter launcher ID:");
		String idTargetLauncher = in.nextLine();
		System.out.println("Enter the destruct time:");
		int destructTime =   in.nextInt();
		theWar.interceptLauncher(idTargetLauncher,destructTime );
	}

	private void destroyMissileAction() {
		System.out.println("Enter missile ID:");
		String idMissile = in.nextLine();
		System.out.println("Enter time to destruct after launching");
		int destructAfterLaunch =   in.nextInt();
		theWar.interceptMissile(idMissile,destructAfterLaunch);
	}

	private void missileLaunchAction() {
		System.out.println("Enter destination:");
		String destination =  in.nextLine();
		System.out.println("Enter fly time:");
		int flyTime = in.nextInt();
		System.out.println("Enter potential damage:");
		int damage = in.nextInt();
		theWar.addMissile(null,null,destination,0,flyTime, damage );
	}

	private void addLauncherAction() {
		theWar.addLauncher(null,false);
		System.out.println("Launcher added successfully\n");
	}

	private void addMissileDestructorAction() {
		System.out.println("Enter ID:");
		String id = in.nextLine();
		theWar.addMissileDestructor(id);
		System.out.println("Missile destractor added successfully!\n");
	}

	private void addLauncherDestructorAction() {	
		System.out.println("Please enter type of destructor (PLANE/SHIP)");
		String launcherDestructorType = in.nextLine();
		theWar.addLauncherDestructor(launcherDestructorType);
		System.out.println("Launcher destractor added successfully!\n");	
	}
	

}
