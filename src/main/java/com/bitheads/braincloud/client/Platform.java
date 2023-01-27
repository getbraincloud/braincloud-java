package com.bitheads.braincloud.client;

public class Platform {
    private String _value;

    public static final Platform AppleTVOS = new Platform("APPLE_TV_OS");
    public static final Platform BlackBerry = new Platform("BB");
    public static final Platform Facebook = new Platform("FB");
    public static final Platform Oculus = new Platform("Oculus");
    public static final Platform GooglePlayAndroid = new Platform("ANG");
    public static final Platform AmazonAndroid = new Platform(("Amazon")); //needs to have specific capitalizations.
    public static final Platform iOS = new Platform("IOS");
    public static final Platform Linux = new Platform("LINUX");
    public static final Platform Mac = new Platform("MAC");
    public static final Platform Web = new Platform("WEB");
    public static final Platform WindowsPhone = new Platform("WINP");
    public static final Platform Windows = new Platform("WINDOWS");
    public static final Platform Xbox360 = new Platform("XBOX_360");
    public static final Platform XboxOne = new Platform("XBOX_ONE");
    public static final Platform PS3 = new Platform("PS3");
    public static final Platform PS4 = new Platform("PS4");
    public static final Platform PSVita = new Platform("PS_VITA");
    public static final Platform WatchOS = new Platform("WATCH_OS");
    public static final Platform Wii = new Platform("WII");
    public static final Platform Tizen = new Platform("TIZEN");
    public static final Platform Roku = new Platform("ROKU");
    public static final Platform Unknown = new Platform("UNKNOWN");

    private Platform(String value) {
        this._value = value;
    }

    @Override
    public String toString() {
        return _value;
    }

    // this will need to be updated as we come across new platforms that are actually running java. This detects a generic platform based on 
    // the platform code that is returned by javas platform detection.
    // Research will need to be done to identify the code for each platform being detected.  
    public static Platform detectGenericPlatform(String s)
    {
        //Google android
        if (s.contains("ANG")) {
            return GooglePlayAndroid;
        }
        //amazon android
        if (s.contains("Amazon")) {
            return AmazonAndroid;
        }
        //linux os
        if (s.contains("nix")) {
            return Linux;
        }
        //mac os 
        if (s.contains("mac")) {
            return Mac;
        }
        //web app
        if (s.contains("web")) {
            return Web;
        }
        //windows os
        if (s.contains("win")) {
            return Windows;
        }
        return Unknown;
    }

    public static Platform fromString(String s) {
        if (s.equals(AppleTVOS.toString())) {
            return AppleTVOS;
        }
        if (s.equals(BlackBerry.toString())) {
            return BlackBerry;
        }
        if (s.equals(Facebook.toString())) {
            return Facebook;
        }
        if (s.equals(Oculus.toString())) {
            return Oculus;
        }
        if (s.equals(GooglePlayAndroid.toString())) {
            return GooglePlayAndroid;
        }
        if (s.equals(AmazonAndroid.toString())) {
            return AmazonAndroid;
        }
        if (s.equals(iOS.toString())) {
            return iOS;
        }
        if (s.equals(Linux.toString())) {
            return Linux;
        }
        if (s.equals(Mac.toString())) {
            return Mac;
        }
        if (s.equals(Web.toString())) {
            return Web;
        }
        if (s.equals(WindowsPhone.toString())) {
            return WindowsPhone;
        }
        if (s.equals(Windows.toString())) {
            return Windows;
        }
        if (s.equals(Xbox360.toString())) {
            return Xbox360;
        }
        if (s.equals(XboxOne.toString())) {
            return XboxOne;
        }
        if (s.equals(PS3.toString())) {
            return PS3;
        }
        if (s.equals(PS4.toString())) {
            return PS4;
        }
        if (s.equals(PSVita.toString())) {
            return PSVita;
        }
        if (s.equals(WatchOS.toString())) {
            return WatchOS;
        }
        if (s.equals(Wii.toString())) {
            return Wii;
        }
        if (s.equals(Tizen.toString())) {
            return Tizen;
        }
        if (s.equals(Roku.toString())) {
            return Roku;
        }

        return Unknown;
    }
}
