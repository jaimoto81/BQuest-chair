/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package co.bquest.silla.messages.main.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.users.model.QBUser;

public class BQuest  extends Activity implements QBCallback
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	
        // ================= QuickBlox ===== Step 1 =================
        // Initialize QuickBlox application with credentials.
        // Getting app credentials -- http://quickblox.com/developers/Getting_application_credentials
        QBSettings.getInstance().fastConfigInit("1025", "9uWqM55p5MmYO7D", "RbQq8L2vGvnd2N2");

        // ================= QuickBlox ===== Step 2 =================
        // Authorize application with device & user.
        // You can create user on admin.quickblox.com, Users module or through QBUsers.signUp method
        QBUser qbUser = new QBUser();
        qbUser.setLogin("moto");
        qbUser.setPassword("motomoto");

       
        
        //
        // Create session with additional parameters
        QBAuth.createSession(qbUser, this);
         
    }
    
    
    @Override
    public void onComplete(Result result) {
        

        if (result.isSuccess()) {
            // Show Messages activity
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
            finish();
        } else {

            // Show errors
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Error(s) occurred. Look into DDMS log for details, " +
                    "please. Errors: " + result.getErrors()).create().show();
        }

    }

    @Override
    public void onComplete(Result result, Object context) {
    }
    
   
}

