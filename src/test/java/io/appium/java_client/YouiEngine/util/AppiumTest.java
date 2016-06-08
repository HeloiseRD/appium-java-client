package io.appium.java_client.YouiEngine.util;

import io.appium.java_client.YouiEngine.YouiEngineDriver;
import org.apache.commons.logging.LogFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("ALL")
public class AppiumTest {

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }

    public static YouiEngineDriver driver;
    public static URL serverAddress;
    private static WebDriverWait driverWait;

    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  test: " + description.getMethodName());
        }

        protected void finished(Description description) {
            System.out.println();
        }
    };

    /**
     * Initialize the webdriver. Must be called before using any helper methods. *
     */
    public static void init(YouiEngineDriver webDriver, URL driverServerAddress) {
        driver = webDriver;
        serverAddress = driverServerAddress;
        int timeoutInSeconds = 30;
        driverWait = new WebDriverWait(webDriver, timeoutInSeconds);
    }

    /**
     * Set implicit wait in seconds *
     */
    public static void setWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /** Keep the same date prefix to identify job sets. **/
    private static Date date = new Date();

    private DesiredCapabilities capabilities = new DesiredCapabilities();
    private void setupCaps(Boolean isAndroid, String appPath) {
        capabilities.setCapability("appium-version", "1.5.2");
        capabilities.setCapability("app", appPath);
        if (isAndroid){
            capabilities.setCapability("platformName", "youi");
            capabilities.setCapability("deviceName", "Android");
            capabilities.setCapability("youiAppAddress", "localhost");
            capabilities.setCapability("youiAppPlatform", "android");
            capabilities.setCapability("avd", "nexus5intel");
            capabilities.setCapability("platformVersion", "6.0.1");
        } else {
            capabilities.setCapability("platformVersion", "9.3");
            capabilities.setCapability("platformName", "youi");
            capabilities.setCapability("youiAppAddress", "localhost");
            capabilities.setCapability("youiAppPlatform", "iOS");
            capabilities.setCapability("deviceName", "iPhone Simulator");
        }
    }

    /** Run before each test **/
    @Before
    public void setUp() throws Exception {
        /** Paths get parsed a little differently for iOS because it looks for Info.plist in other code. **/
        String iOsPath = Paths.get(System.getProperty("user.dir"), "../../../uswish/samples/VideoPlayer/build/ios/Debug-iphonesimulator/VideoPlayer.app").toAbsolutePath().toString();
        String iOsPath2 = Paths.get(System.getProperty("user.dir"), "../../../uswish/Test/apps/InputFieldTester/build/ios/Debug-iphonesimulator/InputFieldTester.app").toAbsolutePath().toString();
        String androidAppPath = Paths.get(System.getProperty("user.dir"), "../../../uswish/samples/VideoPlayer/build/AndroidNative/bin/VideoPlayer-debug.apk").toAbsolutePath().toString();

        boolean isAndroid = false;
        String myAppPath = isAndroid ? androidAppPath : iOsPath;
        setupCaps(isAndroid, myAppPath);

        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new YouiEngineDriver(serverAddress, capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        init(driver, serverAddress);
    }

    /** Run after each test **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) driver.quit();
    }

}