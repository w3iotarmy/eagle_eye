package org.droidplanner.android.socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Borhan Uddin on 6/29/2018.
 */
public class SocketCommand {
    public static String getJsonArm(){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("u", SocketConstant.USER);
            jsonObj.put("action", SocketConstant.ARM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj.toString();
    }
    public static String getJsonStart(){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("u", SocketConstant.USER);
            jsonObj.put("action", SocketConstant.START);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj.toString();
    }
    public static String getJsonMode(String mode){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("u", SocketConstant.USER);
            jsonObj.put("action", SocketConstant.MODE);
            jsonObj.put("data", mode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj.toString();
    }

}
