package com.libra.app.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Manifest extends RealmObject {
    public int passengers;
    public double amt;
    public String date;
    public double dispatch;
    public int transload;
    public double miscellaneous;
    public int loading;
    public double amt_loading;
    public String comment;
}
