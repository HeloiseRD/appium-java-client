package io.appium.java_client.YouiEngine;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.YouiEngineCapabilityType;
import io.appium.java_client.YouiEngine.internal.JsonToYouiEngineElementConverter;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.WebElement;


public class YouiEngineDriver<T extends WebElement> extends AppiumDriver<T> {

    public static final String IOS = "ios";
    public static final String ANDROID = "android";

    public String appPlatform;

    private String unsupportedMethodForPlatform;
    private String unknownPlatformSpecified;

    /** Constructor takes in the Appium Server URL and the capabilities you want to use for this
     * test execution. **/
    public YouiEngineDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities, JsonToYouiEngineElementConverter.class);

        if (desiredCapabilities.getCapability(YouiEngineCapabilityType.APP_PLATFORM) != null) {
            appPlatform = desiredCapabilities.getCapability(YouiEngineCapabilityType.APP_PLATFORM)
                    .toString().toLowerCase();
        } else {
            // TODO what do we do if the appPlatform is null? I know Simon does this sometimes to
            // allow connection to an existing app running...
        }

        // potential error/assert messages
        initializeErrorStrings();
    }

    private void initializeErrorStrings() {
        unsupportedMethodForPlatform = "Unsupported method for platform: " + appPlatform;
        unknownPlatformSpecified = "Unknown app platform provided: " + appPlatform;
    }

    @Override
    public void swipe(int startx, int starty, int endx, int endy, int duration) {
        super.doSwipe(startx, starty, endx, endy, duration); // pass this down?
    }

    /** Asks the device to lock itself for the given amount of time in seconds then it will
     * request the device unlock itself. */
    public void lockFor(int seconds) throws InterruptedException, NoSuchMethodException {
        if (appPlatform.equals(IOS)) {
            super.execute("lock", getCommandImmutableMap("secs", seconds));
        } else if (appPlatform.equals(ANDROID)) {
            this.lock();
            Thread.sleep((long)seconds * 1000); // convert to milliseconds
            this.unlock();
        } else {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
    }

    // TODO: need to decide on how to communicate to the test author when something is not
    // implemented for that platform
    /*
        The issue here seems to be that the capabilities of Android's native automation framework
        differs from iOS's native automation framework. They both expose things differently or some
        things only on one framework.
     */

    // --------- iOS ONLY ------------------------------------------------------------------------
    /** Requests the device emit a shake action.
     * Only available on iOS. */
    public void mobileShake() throws NoSuchMethodException {
        if (!appPlatform.equals(IOS)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("mobileShake");
    }


    // --------- Android ONLY --------------------------------------------------------------------
    /** Requests toggling the Location Service setting.
     * Only available on Android. */
    public void toggleLocationServices() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleLocationServices");
    }

    /** Requests toggling the device's Flight Mode setting.
     * Only available on Android. */
    public void toggleFlightMode() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleFlightMode");
    }

    /** Requests toggling the device's WiFi setting.
     * Only available on Android. */
    public void toggleWiFi() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleWiFi");
    }

    /** Requests toggling the device's Data setting.
     * Only available on Android. */
    public void toggleData() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleData");
    }

    /** Requests that the device locks itself.
     * Only available on Android. */
    public void lock() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("lock");
    }

    /** Requests that the device unlocks itself.
     * Only available on Android. */
    public void unlock() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("unlock");
    }

    /** Requests device's lock state, returning True if it is locked and False if not.
     * Only available on Android. */
    public boolean isLocked() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        Response response = super.execute("isLocked");
        System.out.println(response);
        return true;
    }

    // TODO might be worth adding a private method to determine if something is in a supported list.
}

