# init() loads the braincloud.cfg reader and the app's generated BrainCloudTuning by name.
-keep class com.bitheads.braincloud.support.NativeConfig { public *; }
-keep interface com.bitheads.braincloud.support.NativeConfig$Callback { *; }
-keep interface com.bitheads.braincloud.support.NativeConfig$AppsCallback { *; }
-keep class braincloud.BrainCloudTuning { public static void fill(byte[]); }
