package io.appium.java_client.YouiEngine;

import io.appium.java_client.YouiEngine.util.TestUtility;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.YouiEngineCapabilityType;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.not;


/* TODO: describe make up of this test. Self contained, some of these could be re-used by building
a support/util class. */

public class BasicViewsTest {

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }

    public static TestUtility utils;
    public static YouiEngineDriver driver;
    public static URL serverAddress;

    private boolean isAndroid;

    private static WebDriverWait driverWait;
    private DesiredCapabilities capabilities;

    /** Keep the same date prefix to identify job sets. **/
    private static Date date = new Date();

    /**
     * Initialize the webdriver. Must be called before using any helper methods. We call this
     * in the setup phase of the test. *
     */
    public static void init(YouiEngineDriver webDriver, URL driverServerAddress) {
        driver = webDriver;
        serverAddress = driverServerAddress;
        int timeoutInSeconds = 30;
        driverWait = new WebDriverWait(webDriver, timeoutInSeconds);
    }

    /**
     * Set implicit wait in seconds. *
     */
    public static void setWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    private void setupCaps(String appPath) {
        capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "YouiEngine");
        capabilities.setCapability(MobileCapabilityType.APP, appPath);
        capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "localhost");

        if (isAndroid) {
            capabilities.setCapability(AndroidMobileCapabilityType.AVD, "nexus5intel");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");
            capabilities.setCapability(YouiEngineCapabilityType.APP_PLATFORM, "android");
        } else {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3");
            capabilities.setCapability(YouiEngineCapabilityType.APP_PLATFORM, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6s Plus");
        }
    }

    // junit specific items

    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  test: " + description.getMethodName());
        }

        protected void finished(Description description) {
            System.out.println();
        }
    };

    /** Run before each test. **/
    @Before
    public void setUp() throws Exception {
        /** Paths get parsed a little differently for iOS because it looks for Info.plist in
         * other code. **/
        String appName = "SimpleViews";
        String iosAppPath = Paths.get(System.getProperty("user.dir"),
                "java/io/appium/java_client/" +
                appName + ".app").toAbsolutePath().toString();
        String androidAppPath = Paths.get(System.getProperty("user.dir"),
                "java/io/appium/java_client/" +
                appName + "-debug.apk").toAbsolutePath().toString();

        // Toggle this to switch between Android and iOS
        isAndroid = false;

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


    // tests begin here

    /** Verify that we can get the source from the app and that it is not empty. **/
    @org.junit.Test
    public void pageSourceTest() throws Exception {
        String source;
        source = driver.getPageSource();

        System.out.println("\nPageSource: " + source);

        Assert.assertThat(source, not(""));

    }

    @org.junit.Test
    public void findInputFieldAndSetGetTextTest() throws Exception {
        final String expected;
        expected = "Something";

        // If it was not found, then we would get an ElementNodeFoundException which will fail the
        // test.
        WebElement textField;
        textField = driver.findElement(By.name("TextEdit"));

        // Set the value of the field by sending it a sequence of keys.
        textField.sendKeys(expected);
        utils.delayInSeconds(2);

        String actual;
        actual = textField.findElement(By.name("Text")).getText();
        // TODO issue exists where the text is in a different element
        //actual = textField.getText();

        Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void findPasswordFieldAndSetGetTextTest() throws Exception {
        final String expected = "Something";

        // If it was not found, then we would get an ElementNodeFoundException which will fail the
        // test.
        WebElement passwordField = driver.findElement(By.name("PasswordEdit"));

        // Set the value of the field by sending it a sequence of keys.
        passwordField.sendKeys(expected);
        utils.delayInSeconds(2);

        String actual = passwordField.findElement(By.name("Text")).getText();
        // TODO issue exists where the text is in a different element
        //String actual = textField.getText();

        // Text returned from a get should be the masked text, not the real text we set because
        // this is a password field.
        Assert.assertThat(expected, not(actual));
    }

    @org.junit.Test
    public void findPushButtonAndClickSeveralTimesTest() throws Exception {
        final String expected = "Pushed 10 Times";

        // If it was not found, then we would get an ElementNodeFoundException which will fail
        // the test.
        WebElement pushButton = driver.findElement(By.name("PushButton"));

        for (int i = 0; i < 10; i++) {
            pushButton.click();
            utils.delayInSeconds(1);
        }

        String actual;
        actual = pushButton.findElement(By.name("Text")).getText();
        // TODO issue exists where the text is in a different element
        //String actual = pushButton.getText();

        Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void findToggleButtonAndToggleSeveralTimesTest() throws Exception {
        final String toggleOn = "Toggled ON";
        final String toggleOff = "Toggled OFF";

        // If it was not found, then we would get an ElementNodeFoundException which will fail
        // the test.
        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("ToggleButton"));

        // Toggle the button on
        toggleButton.click();
        utils.delayInSeconds(2);

        String captionFound = toggleButton.findElement(By.name("Text")).getText();
        // TODO issue exists where the text is in a different element
        //String actual = toggleButton.getText();
        Assert.assertEquals(toggleOn, captionFound);

        // Toggle the button off
        toggleButton.click();
        captionFound = toggleButton.findElement(By.name("Text")).getText();
        // TODO issue exists where the text is in a different element
        //String actual = toggleButton.getText();
        Assert.assertEquals(toggleOff, captionFound);

    }

    @org.junit.Test
    public void verifyUiItemsExistTest() throws Exception {
        List<String> uiControlNames = Arrays.asList(
                "TextEdit",
                "PasswordEdit",
                "PushButton",
                "ToggleButton");
        boolean allFound;
        allFound = true;

        for (Iterator<String> item = uiControlNames.iterator(); item.hasNext(); ) {
            String toFind = item.next();
            System.out.println("\n\tLooking for: " + toFind + "...");
            try {
                driver.findElement(By.name(toFind));
                System.out.println("\tFound: " + toFind + ".");
            } catch (NoSuchElementException nseException) {
                System.out.println("\tDid not find: " + toFind + ".");
                allFound = false;
            }
        }
        Assert.assertTrue(allFound);
    }

    @org.junit.Test
    public void verifyOrientationChangesTest() throws Exception {
        ScreenOrientation currentOrientation = driver.getOrientation();

        // Check that the default orientation for this app was Portrait.
        if (currentOrientation == ScreenOrientation.PORTRAIT) {
            System.out.println("\nOrientation was Portrait.");
        } else {
            Assert.fail("\nOrientation was not Portrait.");
        }

        // Switch the orientation to Landscape
        driver.rotate(ScreenOrientation.LANDSCAPE);
        utils.delayInSeconds(3);

        currentOrientation =  driver.getOrientation();
        if (currentOrientation == ScreenOrientation.LANDSCAPE) {
            System.out.println("\nOrientation was Landscape.");
        } else {
            Assert.fail("\nOrientation was not Landscape.");
        }

        // Switch back to Portrait
        driver.rotate(ScreenOrientation.PORTRAIT);
        utils.delayInSeconds(3);

        currentOrientation =  driver.getOrientation();
        if (currentOrientation == ScreenOrientation.PORTRAIT) {
            System.out.println("\nOrientation was Portrait.");
        } else {
            Assert.fail("\nOrientation was not Portrait.");
        }
    }

}
