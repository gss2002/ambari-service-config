package org.apache.ambari.hdp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JsonFileReader {
	  
	public static JSONObject readJsonFile(String file) {
	    File f = new File(file);
        if (f.exists()) {
            InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String jsonTxt = null;
			try {
				jsonTxt = IOUtils.toString(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(jsonTxt);
            JSONObject json = new JSONObject(jsonTxt);
            return json;
        }
		return null;
	}
}
