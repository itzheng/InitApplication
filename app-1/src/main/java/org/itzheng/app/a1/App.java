package org.itzheng.app.a1;

import android.util.Log;

import org.itzheng.appinit.annotation.AppInit;
import org.itzheng.appinit.IAppInit;


@AppInit(priority = 3)
public class App implements IAppInit {
    private static final String TAG = "App";

    @Override
    public void init() {
        Log.w(TAG, "init: aaaaa111");
    }
}
