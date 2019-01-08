package com.libra.app.model;

import io.realm.RealmObject;

public class Routes extends RealmObject{
    public String route;
    public double dispatch;
    public boolean transload;
    public double total_balance;
    public double miscellaneous;
}
