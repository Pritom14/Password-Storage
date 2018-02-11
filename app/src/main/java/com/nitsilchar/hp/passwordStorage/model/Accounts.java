package com.nitsilchar.hp.passwordStorage.model;

public class Accounts {
    private String mAccountName;
    private String mPass;
    private String mDescription;
    private String mLink;

    public Accounts(String accountName, String pass, String description, String link) {
        mAccountName = accountName;
        mPass = pass;
        mDescription = description;
        mLink = link;
    }

    public String getmAccountName() {
        return mAccountName;
    }

    public void setmAccountName(String mAccountName) {
        this.mAccountName = mAccountName;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmPass(String mPass) {
        this.mPass = mPass;
    }


    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }
}
