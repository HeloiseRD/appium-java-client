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
import org.openqa.selenium.Keys;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
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


/**
 * This YouiEngineAppiumSampleTest class is available to illustrate how to create a simple test
 * class.
 *
 * <p>Within this test class you will find several tests that are designed to interact with the
 * YouiEngineAppiumSampleTest.
 *
 * <p>Unlike the SanityTest class, this class does not extend the AppiumTest utility class.
 *
 * <p>Uncompress the iOS YouiEngineAppiumSample.app.zip before using this with iOS as a target.
 * */
public class YouiEngineAppiumSampleTest {

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

    /**
     * Keep the same date prefix to identify job sets.
     **/
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

    // Adjust the capabilities you wish to use in here.
    private void setupCaps(String appPath) {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.APP, appPath);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "YouiEngine");

        if (isAndroid) {
            // The lines below can be modified to target a device or an AVD. Update accordingly.
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
            //capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "devicename");
            capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "localhost");
            //capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "ip.add.res.ss");
            capabilities.setCapability(AndroidMobileCapabilityType.AVD, "nexus5intel");

        } else {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6s Plus");
            //capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "NoProxy");
            capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "localhost");
        }
    }

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
     * Run before each test.
     **/
    @Before
    public void setUp() throws Exception {
        // Toggle this to switch between Android and iOS
        isAndroid = false;

        String currentPath = System.getProperty("user.dir");
        String javaClientPath = "java/io/appium/java_client/";
        String appName = "YouiEngineAppiumSample";
        String fullAppName = isAndroid ? appName + "-debug.apk" : appName + ".app.zip";

        String iosAppPath = Paths.get(currentPath, javaClientPath + fullAppName).toAbsolutePath()
                .toString();
        String androidAppPath = Paths.get(currentPath, javaClientPath + fullAppName)
                .toAbsolutePath().toString();
        String myAppPath = isAndroid ? androidAppPath : iosAppPath;

        setupCaps(myAppPath);

        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new YouiEngineDriver(serverAddress, capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        init(driver, serverAddress);
    }

    /**
     * Run after each test.
     **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    // Retrieves the source from the app and that it is not empty.
    @org.junit.Test
    public void pageSourceTest() throws Exception {
        String source;
        source = driver.getPageSource();

        System.out.println("\nPageSource: " + source);

        Assert.assertThat(source, not(""));
    }

    // Sets the input field's value then retrieves that value to confirm it was set.
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

        String actual = textField.findElement(By.name("Text")).getText();

        Assert.assertEquals(expected, actual);

        WebElement noField;
        boolean found = true;
        try {
            noField = driver.findElement(By.name("NoField"));
        } catch (NoSuchElementException nsee) {
            System.out.println("\tDid not find: NoField: error:" + nsee.toString() + " message = " + nsee.getMessage());
            found = false;
        }

        Assert.assertFalse(found);
    }

    /* Sets the password field's value then retrieves that value to confirm it was set but also
     * that we cannot read it. It is a password field after all!
     * */
    @org.junit.Test
    public void findPasswordFieldAndSetGetTextTest() throws Exception {
        final String expected = "Something";

        WebElement passwordField = driver.findElement(By.name("PasswordEdit"));
        passwordField.sendKeys(expected);
        utils.delayInSeconds(2);

        String actual = passwordField.findElement(By.name("Text")).getText();

        // Text returned from a get should be the masked text, not the real text we set because
        // this is a password field.
        Assert.assertThat(expected, not(actual));
    }

    /* Click on the button 10 times. The button in this app is designed to update its caption
     * with the number of clicks. We can confirm it was clicked 10 times by checking this caption
     * value.
     * */
    @org.junit.Test
    public void findPushButtonAndClickSeveralTimesTest() throws Exception {
        final String expected = "Pushed 10 Times";

        WebElement pushButton = driver.findElement(By.name("PushButton"));

        for (int i = 0; i < 10; i++) {
            pushButton.click();
            utils.delayInSeconds(1);
        }

        String actual = pushButton.findElement(By.name("Text")).getText();

        Assert.assertEquals(expected, actual);
    }

    /* Click on the toggle button and confirm it is on or off. This toggle button updates its
     * caption to reflect what state it is in.
     *  */
    @org.junit.Test
    public void findToggleButtonAndToggleSeveralTimesTest() throws Exception {
        final String toggleOn = "Toggled ON";
        final String toggleOff = "Toggled OFF";

        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("ToggleButton"));

        // Toggle the button on
        toggleButton.click();
        utils.delayInSeconds(2);

        String captionFound = toggleButton.findElement(By.name("Text")).getText();
        Assert.assertEquals(toggleOn, captionFound);

        // Toggle the button off
        toggleButton.click();
        captionFound = toggleButton.findElement(By.name("Text")).getText();

        Assert.assertEquals(toggleOff, captionFound);
    }

    /* Set the text of an input field and delete a portion of it using BACK_SPACE.
     *  */
    @org.junit.Test
    public void sendKeysBackspaceTest() throws Exception {
        String expectedText = "You.i";
        String deletedText = " Engine";
        String sentText = expectedText + deletedText;

        WebElement field = driver.findElement(By.name("TextEdit"));
        field.sendKeys(sentText);
        utils.delayInSeconds(2);
        Assert.assertEquals(sentText, field.getText());

        for (int i = 0; i < deletedText.length(); ++i) {
            field.sendKeys(Keys.BACK_SPACE);
        }
        Assert.assertEquals(expectedText, field.getText());
    }

    /* Set the text of an input field and delete a portion of it using ARROW_LEFT and DELETE.
     *  */
    @org.junit.Test
    public void sendKeysDeleteTest() throws Exception {
        String expectedText = "You.i";
        String deletedText = " Engine";
        String sentText = expectedText + deletedText;

        WebElement field = driver.findElement(By.name("TextEdit"));
        field.sendKeys(sentText);
        utils.delayInSeconds(2);
        Assert.assertEquals(sentText, field.getText());

        // move the cursor to the front of the text to be deleted
        for (int i = 0; i < deletedText.length(); ++i) {
            field.sendKeys(Keys.ARROW_LEFT);
        }
        utils.delayInSeconds(2);
        Assert.assertEquals(sentText, field.getText());

        for (int i = 0; i < deletedText.length(); ++i) {
            field.sendKeys(Keys.DELETE);
        }
        Assert.assertEquals(expectedText, field.getText());
    }

    /* This test iterates through an array that contains the names of all the GUI objects on
     * this screen and confirms they can be found.
     *  */
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
            } catch (NoSuchElementException nsee) {
                System.out.println("\tDid not find: " + toFind + ".");
                allFound = false;
            }
        }
        Assert.assertTrue(allFound);
    }

    /* Display tests 
    * */
    @org.junit.Test
    public void testVisibility() throws Exception {
        boolean displayed = false;

        // We will check if it is hidden, opacity is 0 and if it is reachable from the root

        // Test fully visible button - should be true 
        WebElement pushButton;
        pushButton = driver.findElement(By.name("PushButton"));
        displayed = pushButton.isDisplayed();
        Assert.assertTrue(displayed);

        // Test fully hidden button - visibility is off - shouldn't even be able to find the button and it won't show in the page source  
        displayed = true;
        try {
            WebElement hiddenButton = driver.findElement(By.name("HiddenButton"));
        } catch (NoSuchElementException nsee) {
            // assert we get here - can't find element 
            System.out.println("\tDid not find: HiddenButton");
            displayed = false;
        }
        Assert.assertFalse(displayed);

        // Test button partially hidden by another object - should be true 

        WebElement partHiddenButton;
        partHiddenButton = driver.findElement(By.name("PartHiddenButton"));
        displayed = partHiddenButton.isDisplayed();
        Assert.assertTrue(displayed);

        // Test button fully hidden behind another object - this will be true as we don't cover this situation

        WebElement fullyHiddenButton;
        fullyHiddenButton = driver.findElement(By.name("FullyHiddenButton"));
        displayed = fullyHiddenButton.isDisplayed();
        Assert.assertTrue(displayed);

        // Test opaque button - should be false

        WebElement opaqueButton;
        opaqueButton = driver.findElement(By.name("OpaqueButton"));
        displayed = opaqueButton.isDisplayed();
        Assert.assertFalse(displayed);
    }

    @org.junit.Test
    public void clearNodeTest() throws Exception {

        WebElement textElement = driver.findElement(By.name("TextEdit"));
        textElement.sendKeys("Test Text");
        utils.delayInSeconds(2);
        textElement.clear();
        utils.delayInSeconds(2);
        // CYITextEditViews revert to node name when they are cleared
        Assert.assertEquals("TextEdit", textElement.findElement(By.name("Text")).getText());

        WebElement passwordElement = driver.findElement(By.name("PasswordEdit"));
        passwordElement.sendKeys(("B@DP@55W0rD"));
        utils.delayInSeconds(2);
        passwordElement.clear();
        utils.delayInSeconds(2);
        // CYITextEditViews revert to node name when they are cleared
        Assert.assertEquals("PasswordEdit", passwordElement.findElement(By.name("Text")).getText());

        WebElement toggleElement = driver.findElement(By.name("ToggleButton"));

        toggleElement.click();
        utils.delayInSeconds(2);

        // make sure that clicking the toggle toggled it on.
        Assert.assertTrue(toggleElement.isSelected());

        toggleElement.clear();
        utils.delayInSeconds(2);
        Assert.assertFalse(toggleElement.isSelected());
    }

    /* This test utilizes the TouchAction methods and tests the Youi Engine implementation.
      *  */
    @org.junit.Test
    public void LocationTest() {

        Point elementLocation;

        WebElement textEdit;
        textEdit = driver.findElement(By.name("TextEdit"));

        elementLocation = textEdit.getLocation();
        Assert.assertEquals(elementLocation.x, 55);
        Assert.assertEquals(elementLocation.y, 245);

        WebElement passwordEdit;
        passwordEdit = driver.findElement(By.name("PasswordEdit"));

        elementLocation = passwordEdit.getLocation();
        Assert.assertEquals(elementLocation.x, 49);
        Assert.assertEquals(elementLocation.y, 399);

        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("ToggleButton"));
        elementLocation = toggleButton.getLocation();
        Assert.assertEquals(elementLocation.x, 76);
        Assert.assertEquals(elementLocation.y, 718);

        // Test offscreen button this should give the proper coordinates even if it is off-screen
        WebElement offScreenButton;
        offScreenButton = driver.findElement(By.name("OffScreenButton"));
        elementLocation = offScreenButton.getLocation();
        Assert.assertEquals(elementLocation.x, -448);
        Assert.assertEquals(elementLocation.y, 781);

        // When we have tap implemented we can add a test to get the location of an element and tap on it to round-trip the data
        // and rely less on absolute values
    }


    @org.junit.Test
    public void SizeTest() throws Exception {

        Dimension elementSize;

        WebElement textEdit;
        textEdit = driver.findElement(By.name("TextEdit"));
        elementSize = textEdit.getSize();
        Assert.assertEquals(elementSize.width, 525);
        Assert.assertEquals(elementSize.height, 90);

        WebElement passwordEdit;
        passwordEdit = driver.findElement(By.name("PasswordEdit"));
        elementSize = passwordEdit.getSize();
        Assert.assertEquals(elementSize.width, 525);
        Assert.assertEquals(elementSize.height, 90);

        // a hidden and partly hidden button will return the full size
        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("PartHiddenButton"));
        elementSize = toggleButton.getSize();
        Assert.assertEquals(elementSize.width, 513);
        Assert.assertEquals(elementSize.height, 99);
    }
}
