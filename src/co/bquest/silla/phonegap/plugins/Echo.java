package co.bquest.silla.phonegap.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * This class echoes a string called from JavaScript.
 */
public class Echo extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0); 
            this.echo(message,callbackContext);
            Twitter twitter = TwitterFactory.getSingleton();
       	 	try {
       	 		twitter.toString();
				//twitter.updateStatus(message);
			} catch (Exception e) {
				callbackContext.error("Expected one non-empty string argument.");
				return false;
			}
       	 
            
            return true;
        }
        return false;
    }
    
    

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) { 
        	//System.out.println("Successfully updated the status to [" + status.getText() + "].");
            callbackContext.success(message + " Worldis!!");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
