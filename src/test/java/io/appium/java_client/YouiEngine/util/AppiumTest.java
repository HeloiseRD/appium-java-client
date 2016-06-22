package io.appium.java_client.YouiEngine.util;

import io.appium.java_client.YouiEngine.YouiEngineDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.YouiEngineCapabilityType;
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
/**
 * This is a utility class to contain settings for a test class and prepare the test session.
 * Extend this to create a new test class.
 *
 * This follows the pattern found in the tutorial for writing Appium java_client tests.
 */
public class AppiumTest {

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }

    public static TestUtility utils;
    public static YouiEngineDriver driver;
    public static URL serverAddress;

    public String iosAppPath;
    public String androidAppPath;
    public boolean isAndroid;

    private static WebDriverWait driverWait;
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    /** Keep the same date prefix to identify job sets. **/
    private static Date date = new Date();


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
     * Set implicit wait in seconds.
     */
    public static void setWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    private void setupCaps(String appPath) {
        capabilities.setCapability(MobileCapabilityType.APP, appPath);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "YouiEngine");
        capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "localhost");

        if (isAndroid) {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
            capabilities.setCapability(YouiEngineCapabilityType.APP_PLATFORM, "android");
            capabilities.setCapability(AndroidMobileCapabilityType.AVD, "nexus5intel");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0.1");
        } else {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3");
            capabilities.setCapability(YouiEngineCapabilityType.APP_PLATFORM, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone Simulator");
        }
    }

    /** Run before each test. **/
    @Before
    public void setUp() throws Exception {

        isAndroid = false;

        String currentPath = System.getProperty("user.dir");
        String javaClientPath = "java/io/appium/java_client/";
        String appName = "YouiEngineAppiumSample";
        String fullAppName = isAndroid ? appName + "-debug.apk" : appName + ".app";
        iosAppPath = Paths.get(currentPath, javaClientPath + fullAppName).toAbsolutePath()
                .toString();
        androidAppPath = Paths.get(currentPath, javaClientPath + fullAppName).toAbsolutePath()
                .toString();
        String myAppPath = isAndroid ? androidAppPath : iosAppPath;
        setupCaps(myAppPath);

        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new YouiEngineDriver(serverAddress, capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        init(driver, serverAddress);
    }

    /** Run after each test. **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

}
