package com.libra.app.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Musty on 3/1/18.
 */

public class ApiReader {

    private int VALIDATION_ERROR = 400;
    private int NOT_FOUND_ERROR = 404;
    private int UNAUTHORIZED_ERROR = 401;
    private int FORBIDDEN_ERROR = 403;
    private int SYSTEM_ERROR = 501;
    private int OK_RESPONSE = 200;
    private int CREATED_RESPONSE = 201;
    private int DUPLICATE_RESPONSE = 202;
    private JSONObject jsonObject;

    private int code = 0;
    private String message = "";
    private String error = "";

    public ApiReader(JSONObject js){
        this.jsonObject = js;
        try{
            this.code = js.getInt("code");
            this.message = js.getString("message");
            this.error = js.getString("error");
        }
        catch(JSONException jsEx){
            jsEx.printStackTrace();
        }
    }

    public boolean isSuccess(){
        return code == OK_RESPONSE || code == CREATED_RESPONSE || code == DUPLICATE_RESPONSE;
    }

    public boolean isFailed(){
        return code == VALIDATION_ERROR;
    }

    public boolean isAuthorizationError(){
        return code == UNAUTHORIZED_ERROR || code == FORBIDDEN_ERROR;
    }

    public boolean isSystemError(){
        return code == SYSTEM_ERROR || code == NOT_FOUND_ERROR;
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try{
            obj = jsonObject.getJSONObject("data");
        }
        catch(JSONException jsEx){
            jsEx.printStackTrace();
        }
        return obj;
    }

    public JSONObject getData(String key){
        JSONObject obj = new JSONObject();
        try{
            obj = jsonObject.getJSONObject("data").getJSONObject(key);
        }
        catch(JSONException jsEx){
            jsEx.printStackTrace();
        }
        return obj;
    }

    public JSONArray getData(boolean isArray){
        JSONArray obj = new JSONArray();
        try{
            obj = jsonObject.getJSONArray("data");
        }
        catch(JSONException jsEx){
            jsEx.printStackTrace();
        }
        return obj;
    }

    public JSONArray getData(boolean isArray, String key){
        JSONArray obj = new JSONArray();
        try{
            obj = jsonObject.getJSONObject("data").getJSONArray(key);
        }
        catch(JSONException jsEx){
            jsEx.printStackTrace();
        }
        return obj;
    }


    public String getErrorMessage(){
        return this.error;
    }

    public String getErrorTitle(){
        return this.message;
    }

    @Override
    public String toString() {
        return "ApiReader{" +
                ", jsonObject=" + jsonObject.toString() +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
