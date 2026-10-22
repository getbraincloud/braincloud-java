# brainCloud Java Library

Thanks for downloading the brainCloud Java client library! Here are a few notes to get you started. Further information about the brainCloud API, including example Tutorials can be found here:

http://getbraincloud.com/apidocs/

If you haven't signed up or you want to log into the brainCloud portal, you can do that here:

https://portal.braincloudservers.com/

For Android development:

https://github.com/getbraincloud/braincloud-java-android

For previous versions of brainCloud (4.13.0 and earlier):

https://github.com/getbraincloud/braincloud-java-deprecated

## Releases

Now on Maven Central!

The brainCloud Java client library is published under the com.bitheads namespace on Maven Central: 

https://central.sonatype.com/artifact/com.bitheads/braincloud-java/

### Maven Dependency

Add the following to a pom.xml, replacing the version with the desired value:

```
<dependency>
    <groupId>com.bitheads</groupId>
    <artifactId>braincloud-java</artifactId>
    <version>x.x.x</version>
</dependency>
```

### Gradle Dependency
Add the following to a build.gradle:

```
implementation 'com.bitheads:braincloud-java:x.x.x'
```

Note that for Android projects there is a ```braincloud-java-android``` artifact published on Maven Central containing a separate Android version of the BrainCloudWrapper.java:

https://central.sonatype.com/artifact/com.bitheads/braincloud-java-android/

## Examples

Java/Android examples here: https://github.com/getbraincloud/examples-java

## Troubleshooting

Here are a few common errors that you may see on your first attempt to connect to brainCloud.

- **No braincloud.cfg found**: Pick an app in the brainCloud tool window and check `src/main/resources/braincloud.cfg` exists.
- **App id not set**: If you initialize with the app ID and secret, check they're set correctly in `initialize()`.
- **Platform not enabled**: Verify you've enabled your platform on the portal.

If you're still having issues, log into the portal and give us a shout through the help system (bottom right icon with the question mark and chat bubble).

## brainCloud Summary

brainCloud is a ready-made back-end platform for the development of feature-rich games, apps and things. brainCloud provides the features you need – along with comprehensive tools to support your team during development, testing and user support.

brainCloud consists of:
- Cloud Service – an advanced, Software-as-a-Service (SaaS) back-end
- Client Libraries – local client libraries (SDKs)
- Design Portal – a portal that allows you to design and debug your apps
- brainCloud Architecture

![architecture](/Screenshots/bc-architecture.png?raw=true)

## What's the difference between the brainCloud Wrapper and the brainCloud Client?
The wrapper contains quality of life improvement around the brainCloud Client. It may contain device specific code, such as serializing the user's login id on an Android or iOS device.
It is recommended to use the wrapper by default.

![wrapper](/Screenshots/bc-wrapper.png?raw=true)

## How do I initialize brainCloud?

### With the brainCloud Java plugin (recommended)
Keeps the app secret out of your code.

1. Add the `braincloud-java` dependency. IntelliJ IDEA offers the **brainCloud Java** plugin, or install it from **Settings → Plugins → Marketplace** ([plugin page](https://plugins.jetbrains.com/plugin/34823-braincloud-java)).
2. Open the **brainCloud** tool window, log in, and pick your team and app (or create one).
3. The plugin writes `src/main/resources/braincloud.cfg` and gitignores it.
4. Initialize:
```java
_bc = new BrainCloudWrapper(); // optionally pass in a _wrapperName
if (!_bc.init()) {
    // no braincloud.cfg found
}
```

To switch apps, pick the app again or use **Tools → brainCloud → Refresh Config**.

**Parent apps:** add the child apps your client switches to in the plugin. `init()` loads them too, and `getChildAppIdList()` returns their ids (in plugin order) for `getIdentityService().switchToChildProfile`. For CI, keep `braincloud.cfg` as a secret file and copy it into `src/main/resources/` before building.

If you minify with ProGuard/R8, keep the config reader:
```
-keep class com.bitheads.braincloud.support.NativeConfig { public *; }
-keep interface com.bitheads.braincloud.support.NativeConfig$Callback { *; }
```

### With the app ID and secret
```java
_bc = new BrainCloudWrapper();
_bc.initialize(_appId, _secret, _appVersion, _serverUrl);
```
Your app ID and secret are in the portal under **Design | Core App Info > Application IDs**.

![wrapper](/Screenshots/bc-ids.png?raw=true)

_wrapperName prefixes saved operations that the wrapper will make. Use a _wrapperName if you plan on having multiple instances of brainCloud running.


----------------

#### Newly upgraded?
If your app is already live, you should **NOT** specify the _wrapperName - otherwise the library will look in the wrong location for your user's stored anonymousID and profileID information. Only add a name if you intend to alter the save data.

---------------


_appVersion is the current version of our app. Having an _appVersion less than your minimum app version on brainCloud will prevent the user from accessing the service until they update their app to the lastest version you have provided them.

![wrapper](/Screenshots/bc-minVersions.png?raw=true)

## How do I keep the brainCloud SDK updating?
In your project's update loop, you're going to want to update brainCloud client so it can check for responses.

To do this, you need to call runCallbacks();

```java
_bc.runCallbacks();
```

## How do I authenticate a user with brainCloud?
The simplest form of authenticating with brainCloud Wrapper is an Anonymous Authentication.
```java
_bc.authenticateAnonymous(_callback);
```
This method will create an account, and continue to use a locally saved anonymous id.

Your _callback will inherit from IServerCallback and contain the functions needed to react to the brainCloud Server response.


To login with a specfic anonymous id, use the brainCloud client.
```java
_bc.getClient().getAuthenticationService().setAnonymousId(_anonymousId); // re-use an Anon id
_bc.getClient().getAuthenticationService().setAnonymousId(_bc.getClient().getAuthenticationService().generateAnonymousId()); // or use brainCloud to generate one
_bc.getClient().GetAuthenticationService().AuthenticateAnonymous(_forceCreate, _callback);
```
Setting _forceCreate to false will ensure the user will only login to an existing account. Setting it to true, will allow the user to register a new account

## How do I attach an email to a user's brainCloud profile?
After having the user create an anonymous with brainCloud, they are probably going to want to attach an email or username, so their account can be accessed via another platform, or when their local data is discarded.
Attaching email authenticate would look like this.
```java
_wrapper.getIdentityService().attachEmailIdentity(_email, _password, _callback);
```
There are many authentication types. You can also merge profiles and detach idenities. See the brainCloud documentation for more information:
http://getbraincloud.com/apidocs/apiref/?java#capi-auth

## TimeUtils
Most of our APIs suggest using UTC time, so we have added utility functions for better handling local and UTC time.
```
long UTCDateTimeToUTCMillis(Date utcDate) //returns the UTC time in milliseconds as an Int64.
Date UTCMillisToUTCDateTime(long utcMillis) //returns a Date in UTC based on the milliseconds passed in
Date LocalTimeToUTCTime(Date localDate) //Converts a Local time to UTC time
Date UTCTimeToLocalTime (Date utcDate) //Converts a UTC time to Local time
```
examples of use:
```
        Date date = new Date();
        date.setTime(date.getTime() + 120 * 1000);
        long dateAsLong = TimeUtil.UTCDateTimeToUTCMillis(date); //convert date into UTC milliseconds

        _wrapper.getScriptService().scheduleRunScriptMillisUTC(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                dateAsLong, //use it in our UTC calls
                tr);
```
