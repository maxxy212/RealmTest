package com.libra.app.network;

/**
 * Created by Musty on 2/28/18.
 */

public abstract class ApiCallHandler {

    protected abstract void done();
    public void success(Object data){
        this.done();
    }
    public void failed(String title, String reason){
        this.done();
    }

}
