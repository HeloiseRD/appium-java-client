package io.appium.java_client.YouiEngine;

import io.appium.java_client.YouiEngine.util.TestUtility;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.YouiEngineCapabilityType;
import io.appium.java_client.TouchAction;
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
            //capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6 Plus");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "localhost");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "NoProxy");
            //capabilities.setCapability(YouiEngineCapabilityType.APP_ADDRESS, "192.168.24.23");
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
    /* This test utilizes the TouchAction methods and tests the Youi Engine implementation.
     *  */
    @org.junit.Test
    public void LocationTest() throws Exception {

        Point elementLocation;

        WebElement textEdit;
        textEdit = driver.findElement(By.name("TextEdit"));

        elementLocation = textEdit.getLocation();
        Assert.assertEquals(elementLocation.x, 55);
        Assert.assertEquals(elementLocation.y, 245);

        WebElement passwordEdit;
        passwordEdit = driver.findElement(By.name("PasswordEdit"));

        elementLocation = passwordEdit.getLocation();
        Assert.assertEquals(elementLocation.x, 55);
        Assert.assertEquals(elementLocation.y, 372);

        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("ToggleButton"));

        elementLocation = toggleButton.getLocation();
        Assert.assertEquals(elementLocation.x, 61);
        Assert.assertEquals(elementLocation.y, 811);
    }

    /* This test utilizes the TouchAction methods and tests the Youi Engine implementation for taps and presses.
     * */
    @org.junit.Test
    public void pressActionTest() throws Exception {

        final String toggleOn = "Toggled ON";
        final String toggleOff = "Toggled OFF";

        // Test Press and Release which is really a Tap

        WebElement toggleButton;
        toggleButton = driver.findElement(By.name("ToggleButton"));

        String captionFound = toggleButton.findElement(By.name("Text")).getText();
        Assert.assertEquals(toggleOff, captionFound);

        // Test press(WebElement) and release()
        TouchAction downUpElement = new TouchAction(driver).press(toggleButton).release();
        downUpElement.perform();

        captionFound = toggleButton.findElement(By.name("Text")).getText();
        Assert.assertEquals(toggleOn, captionFound);

        // Test press(x, y) and release()
        Point toggleLocation = toggleButton.getLocation(); // num args 0 - error
        TouchAction downUpXY = new TouchAction(driver).press(toggleLocation.x + 10, toggleLocation.y + 10).release();
        downUpXY.perform();

        captionFound = toggleButton.findElement(By.name("Text")).getText();
        Assert.assertEquals(toggleOff, captionFound);

        // Test press(WebElement, x, y) and release()
        TouchAction downUpWEXY = new TouchAction(driver).press(toggleButton, 10, 10).release();
        downUpWEXY.perform();

        captionFound = toggleButton.findElement(By.name("Text")).getText();
        Assert.assertEquals(toggleOn, captionFound);
    }

    /* This test utilizes the TouchAction methods for moving, swiping and flicking and tests the Youi Engine implementation.
     * */
    @org.junit.Test
    public void moveActionTest() throws Exception {
        // Test scrolling the list view

        WebElement listView;
        listView = driver.findElement(By.name("MyListView"));

        List listViewItems = driver.findElementsByClassName("MyListItemView");

        if (listViewItems.size() != 0) {
            WebElement listViewItem = (WebElement) listViewItems.get(2);

            // Test press(WebElement), move and release() to scroll list view (doesn't work on device, likely needs to be center or something)
            TouchAction scrollListItem1 = new TouchAction(driver).press(listViewItem).moveTo(-100, 0).release();
            scrollListItem1.perform();

            // need 2 other APIS
            Point location = listViewItem.getLocation();
            TouchAction scrollListItem2 = new TouchAction(driver).press(location.x + 10, location.y + 10).moveTo(-100, 0).release();
            scrollListItem2.perform();

            TouchAction scrollListItem3 = new TouchAction(driver).press(listViewItem, 10, 10).moveTo(-100, 0).release();
            scrollListItem3.perform();
        }
    }

    /* This test ...
     * */
    @org.junit.Test
    public void longPressActionTest() throws Exception {


        // longpress on element
        WebElement button;
        button = driver.findElement(By.name("ToggleButton"));
        TouchAction longPress = new TouchAction(driver).longPress(button).release();
        longPress.perform();

        // have to wait here!!!
        //driverWait.wait(2000);

        String captionFound = driver.findElement(By.name("PartHiddenButton")).getText();
        Assert.assertEquals("Long Pressed", captionFound);

        // longpress on x,y

        Point location = button.getLocation();
        driver.findElement(By.name("PartHiddenButton")).clear(); // clar Long Pressed caption

        longPress = new TouchAction(driver).longPress(location.x + 10, location.y + 10).release();
        longPress.perform();

        // have to wait here!!!
        //driverWait.wait(2000);

        captionFound = driver.findElement(By.name("PartHiddenButton")).getText();
        Assert.assertEquals("Long Pressed", captionFound);
    }

    /* This test ...
     * */
        @org.junit.Test
    public void longPressMoveTest() throws Exception {
        // have the sample do something on long press move that we can measure
        // longpress + context menu + move
        // WebElement contextMenu, menuItem;
        //contextMenu = driver.findElement(By.name("ContextMenu"));
        //TouchAction contextMenu = new TouchAction(driver).longPress(toggleButton).moveTo(menuItem).release();


    }
}
