package org.apache.ambari.hdp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import org.json.JSONObject;

public class AmbariHttpClient {
	String basicAuth;

	public AmbariHttpClient(String user, char[] passwd){
		basicAuth = "Basic " + Base64.getEncoder().encodeToString((user+":"+String.valueOf(passwd)).getBytes());

	}
	
	public String getJSON(String curl) {
		URL cUrl = null;
		try {
			cUrl = new URL(curl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection c;
		try {
			c = (HttpURLConnection) cUrl.openConnection();
			c.setRequestProperty ("Authorization", basicAuth);
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-Type", "application/json");
			c.setRequestProperty("X-Requested-By", "ambari");
			c.setUseCaches(false);
			c.setDoInput(true);
			c.setDoOutput(true);
			c.connect();
			int status = c.getResponseCode();
			if (status == 200 || status == 201 || status == 202) {
	              BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public void putJson(String curl, JSONObject jsonObj) {
		String jsonout = jsonObj.toString();
		URL cUrl = null;
		try {
			cUrl = new URL(curl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection c;
		try {
			c = (HttpURLConnection) cUrl.openConnection();
			c.setRequestMethod("PUT");
			c.setRequestProperty ("Authorization", basicAuth);
			c.setRequestProperty("X-Requested-By", "ambari");
			c.setUseCaches(false);
			c.setDoInput(true);
			c.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(c.getOutputStream());      
			wr.write(jsonout.getBytes());
			wr.flush();      
			wr.close();
			c.getInputStream();
			System.out.println("Status: "+c.getResponseCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
