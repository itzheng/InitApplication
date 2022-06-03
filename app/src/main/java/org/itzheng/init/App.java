package org.itzheng.init;

import android.util.Log;

import org.itzheng.appinit.IAppInit;
import org.itzheng.appinit.annotation.AppInit;

@AppInit(priority = 1)
public class App implements IAppInit {
    private static final String TAG = "App";

    @Override
    public void init() {
        Log.w(TAG, "init: tooosss");
    }
}
