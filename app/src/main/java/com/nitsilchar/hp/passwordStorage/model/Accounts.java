package com.nitsilchar.hp.passwordStorage.model;

/**
 * Created by Carol on 1/1/2018.
 */

public class Accounts {
    private String mAccountName;
    private String mPass;
    private String mDescription;

    public Accounts(String accountName, String pass, String description) {
        mAccountName = accountName;
        mPass = pass;
        mDescription = description;
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

}
