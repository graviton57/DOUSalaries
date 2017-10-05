package com.havrylyuk.dou.utils;

/**
 * Created by Igor Havrylyuk on 22.09.2017.
 */

public class NativeLibrariesManager {

    // Used to load the 'native-lib' library on application startup.
    static {
        try {
            System.loadLibrary("sqliteX"); // loads the custom sqlite library
            //System.loadLibrary("sqlitefunctions"); // loads the extensions functions library
        } catch (NullPointerException | UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public static void loadNativeLibraries() {
        // no-op
    }
}
