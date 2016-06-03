package io.appium.java_client.YouiEngine;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.YouiEngine.internal.JsonToYouiEngineElementConverter;
import java.net.URL;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Response;


public class YouiEngineDriver<T extends WebElement> extends AppiumDriver<T> {

    public String appPlatform;

    // Platform constants
    final static public String IOS = "ios";
    final static public String ANDROID = "android";

    // internal constants
    final static public String APP_PLATFORM_CAPABILITIES = "youiAppPlatform";

    // error messages
    private String unsupportedMethodForPlatform;
    private String unknownPlatformSpecified;

    /** Constructor takes in the Appium Server URL and the capabilities you want to use for this test execution. **/
    public YouiEngineDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities, JsonToYouiEngineElementConverter.class);

        if (desiredCapabilities.getCapability(APP_PLATFORM_CAPABILITIES) != null) {
            appPlatform = desiredCapabilities.getCapability(APP_PLATFORM_CAPABILITIES).toString().toLowerCase();
            System.out.println("\nPlatform: " + appPlatform);
        } else {
            // TODO what do we do if the appPlatform is null? I know Simon does this sometimes to allow connection to an existing app running...
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

    public void lockFor(int seconds) throws InterruptedException, NoSuchMethodException {
        if (appPlatform.equals(IOS)) {
            super.execute("lock", getCommandImmutableMap("secs", seconds));
        } else if (appPlatform.equals(ANDROID)) {
            this.lock();
            // TODO need a better way to do this...
            Thread.sleep((long)seconds*1000); // convert to milliseconds
            this.unlock();
        } else {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
    }

    // TODO: need to decide on how to communicate to the test author when something is not implemented for that platform
    /*
        The issue here seems to be that the capabilities of Android's native automation framework differs from iOS's
        native automation framework. They both expose things differently or some things only on one framework.
     */

    // --------- iOS ONLY --------------------------------------------------------------------------------------------
    public void mobileShake() throws NoSuchMethodException {
        if (!appPlatform.equals(IOS)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("mobileShake");
    }


    // --------- Android ONLY ----------------------------------------------------------------------------------------
    public void toggleLocationServices() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID))
        {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleLocationServices");
    }

    public void toggleFlightMode() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID))
        {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleFlightMode");
    }

    public void toggleWiFi() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID))
        {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleWiFi");
    }

    public void toggleData() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID))
        {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("toggleData");
    }

    public void lock() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("lock");
    }

    public void unlock() throws NoSuchMethodException {
        if (!appPlatform.equals(ANDROID)) {
            throw new NoSuchMethodException(unsupportedMethodForPlatform);
        }
        super.execute("unlock");
    }

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

