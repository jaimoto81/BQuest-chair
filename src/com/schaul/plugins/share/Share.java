/**
 * 
 * Phonegap share plugin for Android
 * Kevin Schaul 2011
 *
 */

package com.schaul.plugins.share;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

public class Share extends CordovaPlugin {

	@Override
	//public PluginResult execute(String action, JSONArray args, String callbackId) {
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		try {
			JSONObject jo = args.getJSONObject(0);
			doSendIntent(jo.getString("subject"), jo.getString("text")); 
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	
	private void doSendIntent(String subject, String text) {
		Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		this.cordova.startActivityForResult(this, sendIntent, 0);
	}

}
