package com.crud.singl.eyehealthv3.service;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class AppInfo {

    private String packageName;
    private boolean userApp;

    public AppInfo(String packageName, boolean userApp) {
        this.packageName = packageName;
        this.userApp = userApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }
}
