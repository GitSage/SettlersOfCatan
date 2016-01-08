package client.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import client.data.PlayerInfo;

/**
 * Used to give dummy data from file for testing purposes
 */
public class FileProxy implements Proxy {
	
	private String testFileDir;
	
	public FileProxy(String pathToTestFileDir) {
		testFileDir = pathToTestFileDir;
	}
	
	/**
	 * This is the method that actually does the work
	 * @param fullPath
	 * @return
	 * @throws IOException
	 */
	private String fakeSend(String fullPath) throws IOException {
		
		File file = new File(fullPath);
		
		BufferedReader reader;

		reader = new BufferedReader(new FileReader (file));

	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    while( (line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    reader.close();
	    String responseString = stringBuilder.toString();
	    if(responseString.equals("")) {
	    	responseString = "Success";
	    }
	    	
	    return responseString;
	}
	
	private String fakePost(String path) throws IOException {
		
		//remove get variables
		String correctedPath = path.split("\\?")[0];
		
		String fullPath = testFileDir + "post" + correctedPath + ".json";
		return fakeSend(fullPath);
	}	

	private String fakeGet(String path) throws IOException {
		
		//remove get variables
		String correctedPath = path.split("\\?")[0];
		
		String fullPath = testFileDir + "get" + correctedPath + ".json";
		return fakeSend(fullPath);
	}
	
	/**
     * returns a PlayerInfo object
     * @return
     */
    @Override
    public PlayerInfo getPlayerCookie() {
    	
    	PlayerInfo fake = new PlayerInfo();
    	fake.setId(0);
    	fake.setName("Test");
    	return fake;
    }
	
	@Override
	public String get(String path) {
		try {
			return fakeGet(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String post(String path, Object requestObject) {
		try {
			return fakePost(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteGameCookie() {
		
	}
}

