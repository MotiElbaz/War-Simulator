package Model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


public class WarReader extends Thread implements FinalValuesInterface{

	private JSONParser parser;
	private  WarModel war;


	public WarReader(JSONParser parser, WarModel  war) {
		this.parser = parser;
		this.war = war;
	}

	@Override
	public void run() {
		try {
			readFromJsonFile(parser, war);
		} catch (IOException | ParseException | InterruptedException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}

	/** To read the whole details of the war from the JSON file
	 * @throws org.json.simple.parser.ParseException **/
	private void readFromJsonFile(JSONParser parser, WarModel war) throws FileNotFoundException, IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {	
		Object obj = parser.parse(new FileReader("myWarJSON.json"));
		JSONObject jsonObject =  (JSONObject) obj;
		JSONObject theWarFromJSON = (JSONObject) jsonObject.get("war");
		//Read war's tools
		readLaunchersFromJSON(theWarFromJSON, war);
		readMissileDestructorsFromJSON(theWarFromJSON, war);
		readLauncherDestructorsJSON(theWarFromJSON, war);
	}




	/**Read the launcher details from JSON file in addition ,reading and launching missiles 
	 * @throws IOException 
	 * @throws SecurityException **/
	private void readLaunchersFromJSON(JSONObject theWar, WarModel war) throws InterruptedException, SecurityException, IOException {
		JSONObject missileLaunchers = (JSONObject) theWar.get("missileLaunchers");
		JSONArray launchers = (JSONArray) missileLaunchers.get("launcher");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = launchers.iterator();
		//Read values of launchers
		while (iterator.hasNext()) {
			JSONObject launcher = iterator.next();
			String idLauncher = (String) launcher.get("id");
			boolean isHidden = Boolean.valueOf((String) launcher.get("isHidden"));
			//Add the launcher to the collation of the war
			war.addLauncher(idLauncher,isHidden);
			readMissilesToLaunchFromJSON(idLauncher,launcher, war);
		}
	}
	
	
	
	/**Read the missile from JSON file in addition and launch it**/
	private void readMissilesToLaunchFromJSON(String idLauncher, JSONObject launcher, WarModel war) throws InterruptedException {
		JSONArray missiles = (JSONArray) launcher.get("missile");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> itr = missiles.iterator();
		//Start launching missiles with current launcher
		while (itr.hasNext()){
			JSONObject missile = itr.next();
			launchMissile(war,missile,idLauncher);
		}
	}
	

	private void launchMissile(WarModel war, JSONObject missile, String idLauncher) {
		String id = (String) missile.get("id");
		String destination = (String) missile.get("destination");
		int launchTime = Integer.parseInt((String) missile.get("launchTime"));
		int flyTime = Integer.parseInt((String)missile.get("flyTime")) ;
		int damage = Integer.parseInt((String)missile.get("damage"));
		war.addMissile(idLauncher,id, destination, launchTime, flyTime, damage);
	}



	/**Read the missile destructor details from JSON file and also read and destroy missiles 
	 * @throws IOException 
	 * @throws SecurityException **/
	private void readMissileDestructorsFromJSON(JSONObject theWar, WarModel war) throws InterruptedException, SecurityException, IOException {
		JSONObject missileDestructors = (JSONObject) theWar.get("missileDestructors");
		JSONArray destructors = (JSONArray) missileDestructors.get("destructor");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = destructors.iterator();
		//Read missile destructor from JSON file
		while (iterator.hasNext()) {
			JSONObject destructor = iterator.next();
			String id = (String) destructor.get("id");
			war.addMissileDestructor(id);
			readMissilesToDestruct(destructor, war);
		}
	}


	private void readLauncherDestructorsJSON(JSONObject theWar, WarModel war) throws InterruptedException, SecurityException, IOException {	

		JSONObject missileLauncherDestructors = (JSONObject) theWar.get("missileLauncherDestructors");
		JSONArray destructors = (JSONArray) missileLauncherDestructors.get("destructor");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = destructors.iterator();
		while (iterator.hasNext()) {
			JSONObject destructor = iterator.next();
			String type = (String) destructor.get("type");
			war.addLauncherDestructor(type);
			readLaunchersToDestruct(destructor, war);
		}	
	}


	private void readMissilesToDestruct(JSONObject destructor, WarModel war) throws InterruptedException {
		//If JSON Object
		if(destructor.get("destructdMissile") instanceof JSONObject ) {
			JSONObject destructdMissile = (JSONObject ) destructor.get("destructdMissile");
			String idMissile = (String) destructdMissile.get("id");
			int destructAfterLaunch = Integer.parseInt((String)destructdMissile.get("destructAfterLaunch"));
			war.interceptMissile(idMissile, destructAfterLaunch);
			//else JSON Array
		}else {
			JSONArray destructdMissile = (JSONArray) destructor.get("destructdMissile");
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> itr = destructdMissile.iterator();
			while (itr.hasNext()){
				JSONObject missile = itr.next();
				String idMissile = (String) missile.get("id");
				int destructAfterLaunch = Integer.parseInt((String)missile.get("destructAfterLaunch"));
				war.interceptMissile(idMissile, destructAfterLaunch);
			}
		}	
	}



	private void readLaunchersToDestruct(JSONObject destructor, WarModel war ) throws InterruptedException {
		//If JSON Object 
		if(destructor.get("destructedLanucher") instanceof JSONObject ) {
			JSONObject destructedLanucher = (JSONObject ) destructor.get("destructedLanucher");
			String id = (String) destructedLanucher.get("id");
			int destructTime = Integer.parseInt((String)destructedLanucher.get("destructTime"));
			war.interceptLauncher(id,destructTime);
			//Else JSON Array
		}else {
			JSONArray destructedLanucher = (JSONArray) destructor.get("destructedLanucher");
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> itr = destructedLanucher.iterator();
			while (itr.hasNext()){
				JSONObject launcher = itr.next();
				String id = (String) launcher.get("id");
				int destructTime = Integer.parseInt((String)launcher.get("destructTime"));
				war.interceptLauncher(id,destructTime);
			}
		}
	}
}
