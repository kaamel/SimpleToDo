package com.codepath.simpletodo;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by kaamel on 8/3/17.
 */

public class SimpleTodoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        //FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }
}
