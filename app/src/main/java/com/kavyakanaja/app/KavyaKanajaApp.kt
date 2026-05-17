package com.kavyakanaja.app

import android.app.Application

/**
 * Application class — ensures database is initialized correctly.
 * Register in AndroidManifest.xml with android:name=".KavyaKanajaApp"
 */
class KavyaKanajaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Any global app initialization goes here
        // e.g., crash reporting, analytics, etc.
    }
}