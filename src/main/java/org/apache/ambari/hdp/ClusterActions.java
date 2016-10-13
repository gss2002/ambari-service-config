package org.apache.ambari.hdp;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClusterActions {

	
	public static String getClusterName(String json){
		JSONObject jsonObj= new JSONObject(json);
        JSONArray jsonArray = (JSONArray) jsonObj.get("items");
        String clustername = jsonArray.getJSONObject(0).getJSONObject("Clusters").getString("cluster_name");
		return clustername;
	}
	public static String getCurrentVersion(String json, String config_type){
		JSONObject jsonObj= new JSONObject(json);
        String version = jsonObj.getJSONObject("Clusters").getJSONObject("desired_configs").getJSONObject(config_type).getString("tag");
        return version;
	}
	
	public static JSONObject getCurrentProps(String json){
		JSONObject jsonObj = new JSONObject(json);
		JSONObject jsonProps = jsonObj.getJSONArray("items").getJSONObject(0).getJSONObject("properties");
		return jsonProps;
	}
	public static JSONObject updateJsonObj(JSONObject jsonObj, JSONObject jprops) {
		Iterator<?> keys = jprops.keys();
		while(keys.hasNext() ) {
		    String key = (String)keys.next();
		    if (jprops.get(key).toString().equals("AMBARI_DELETE")){
		    	if (jsonObj.has(key)){
		    		jsonObj.remove(key);
			    	System.out.println("Removing: Key="+key+" Value="+jprops.get(key));

		    	}
		    } else {
		    	if (!(jsonObj.has(key))) {
		    		jsonObj.put(key, jprops.get(key).toString());
		    		System.out.println("Adding: Key="+key+" Value="+jprops.get(key));
		    	} else {
		    		if (!(jsonObj.get(key).toString().equalsIgnoreCase(jprops.get(key).toString()))) {
			    		jsonObj.put(key, jprops.get(key).toString());
			    		System.out.println("Updating: Key="+key+" Value="+jprops.get(key));		
		    		}
		    	}
		    }  		
		}
		return jsonObj;
	}
}
