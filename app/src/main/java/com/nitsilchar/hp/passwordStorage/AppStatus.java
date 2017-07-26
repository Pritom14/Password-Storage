package com.nitsilchar.hp.passwordStorage;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by shaloin on 15/6/17.
 */

public class AppStatus {
    Context context;

    public AppStatus(Context context){
        this.context=context;
    }

    public boolean isOnline(){
        /*Runtime runtime=Runtime.getRuntime();
        try{
            Process ipProcess=runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue=ipProcess.waitFor();
            return (exitValue==0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;*/
        ConnectivityManager connectivityManager
                =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
