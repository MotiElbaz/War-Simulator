package Model;

public interface FinalValuesInterface {

	
	int ADD_LAUNCHER_DISTRACTOR = 1;
	int ADD_MISSILE_DSTRACTOR = 2;
	int ADD_LAUNCHER = 3;
	int LAUNCH_MISSILE = 4;
	int DESTROY_MISSILES = 5;
	int DESTROY_LAUNCHERS = 6;
	int SHOW_DETAILS = 7;
	int EXIT = 0;
	int MAX_MISSILES = 10;
	int VALUE_TO_SECONDES_CONVERTER = 1000;
	String WELCOME_STR = "Welcome to THE WAR";
	String OPTIONS_STR = "Please choose one of the above:\n"
			+ "Add launcher destractor                1\n"
			+ "Add missile destractor                 2\n"
			+ "Add launcher                           3\n"
			+ "Missile launch                         4\n"
			+ "Destroy a missile                      5\n"
			+ "Destroy a launcher	               6\n"
			+ "Show details                           7\n"
			+ "Exit                                   0\n";	
	
	
	
}
