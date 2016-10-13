package org.apache.ambari.hdp;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Console;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String clustername = "";
		String versionIn;
		String url = "";
		String username = "";
		String config_type = "";
		String reason = "Testing AmbariJavaClient";
		boolean updateAmbari = false;
		long versionTime = System.currentTimeMillis();
		JSONObject jprops = null;
		String filename = "/Users/enduser/test.json";
		
	    Console console = System.console();
	    if (console == null) {
	        System.out.println("Couldn't get Console instance");
	        System.exit(0);
	    }
	    url = "";
	    while (url.equalsIgnoreCase("")) {
		    url = console.readLine("Enter Ambari URL (ex. http://localhost:8080): ");
	    }
	    filename = "";
	    while (filename.equalsIgnoreCase("")){
	    	filename = console.readLine("Enter UpdateConfig File: ");
	    }
	    String updateAmbariStr = "";
		while (true) {

	    	updateAmbariStr = console.readLine("Update Ambari Config: true/false :");
	    	updateAmbari = Boolean.parseBoolean(updateAmbariStr.toLowerCase());
	    	if (updateAmbariStr.toLowerCase().equalsIgnoreCase("true")) {
	    		updateAmbari=true;
	    		break;
	    	}
	    	if (updateAmbariStr.toLowerCase().equalsIgnoreCase("false")) {
	    		updateAmbari=false;
	    		break;
	    	}
	    }
	    username = "";
	    while (username.equalsIgnoreCase("")) {
		    username = console.readLine("Enter UserName: ");
	    }
	    char passwordArray[] = console.readPassword("Enter Password: ");
	    
	    System.out.println(username +" "+url+" "+filename);
		
		
		AmbariHttpClient amb = new AmbariHttpClient(username, passwordArray);
		

		String curl = url+"/api/v1/clusters";
		clustername= ClusterActions.getClusterName(amb.getJSON(curl));
		JSONObject json = JsonFileReader.readJsonFile(filename);
		JSONArray jarray = json.getJSONArray("types");
		for (int i = 0; i < jarray.length(); i++) {
			config_type = jarray.getJSONObject(i).getString("type");
			reason = jarray.getJSONObject(i).getString("reason");
			jprops = jarray.getJSONObject(i).getJSONObject("properties");
			curl = url+"/api/v1/clusters/"+clustername+"?fields=Clusters/desired_configs/"+config_type;
			versionIn = ClusterActions.getCurrentVersion(amb.getJSON(curl), config_type);
			System.out.println(versionIn);
			curl = url+"/api/v1/clusters/"+clustername+"/configurations?type="+config_type+"&tag="+versionIn;
	        ClusterActions.getCurrentProps(amb.getJSON(curl));
	        JSONObject propObj = ClusterActions.updateJsonObj(ClusterActions.getCurrentProps(amb.getJSON(curl)), jprops);
	        if (updateAmbari) {
	        	JSONObject dcObj = new JSONObject();
	        	dcObj.put("type", config_type);
	        	dcObj.put("tag", "version"+Long.toString(versionTime));
	        	dcObj.put("service_config_version_note", reason);
	        	dcObj.put("user", username);
	        	dcObj.put("properties", propObj);
	        	JSONObject clusterObj = new JSONObject();
	        	clusterObj.put("desired_config", dcObj);
	        	JSONObject outObj = new JSONObject();
	        	outObj.put("Clusters", clusterObj);
	        	curl = url+"/api/v1/clusters/"+clustername;
	        	amb.putJson(curl, outObj);
	        }
		}
	}

}
