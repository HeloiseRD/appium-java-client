package io.appium.java_client.YouiEngine;

import io.appium.java_client.YouiEngine.util.AppiumTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import org.junit.Assert;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.SessionId;

import static org.hamcrest.CoreMatchers.not;

/**
 * This test class performs a simple series of tests to confirm our implementation in the
 * YouiEngine driver and in this java_client. This test class uses the included
 * YouiEngineAppiumSample app as a target for these tests. The intent of each test can be found
 * before each of the test methods.
 *
 * <p>This test uses the model provided in the Appium java_client test tutorial.
 *
 * <p>Uncompress the iOS YouiEngineAppiumSample.app.zip before using this with iOS as a target.
 */
public class SanityTest extends AppiumTest {

    // Confirm we can get the page source and it is not empty.
    @org.junit.Test
    public void pageSourceTest() throws Exception {
        String source;
        source = driver.getPageSource();
        System.out.println("\nPageSource: " + source);
        Assert.assertThat(source, not(""));

    }

    // Confirm we can take a screenshot.
    @org.junit.Test
    public void screenshotTest() throws Exception {
        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        File output = new File("screenShot.PNG");
        FileUtils.copyFile(screenShot, output);

        System.out.println("\nOutput: " + output.getAbsolutePath());
    }

    // Confirm we can take a screenshot with a path supplied.
    @org.junit.Test
    public void screenshotWithPathTest() throws Exception {
        String fileWithPath = Paths.get(System.getProperty("user.home"))
                + "/Desktop/Screenshots/screenShot.PNG";
        File output = new File(fileWithPath);

        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, output);

        System.out.println("\nOutput: " + output.getAbsolutePath());
    }

    // Confirm we can find an element using the class name strategy.
    @org.junit.Test
    public void findElementByClassTest() throws Exception {
        WebElement posterItem = null;
        try {
            posterItem = driver.findElement(By.className("CYIPushButtonView"));
        } catch (NoSuchElementException exception) {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(posterItem);
    }

    // Confirm we can find an element using the name strategy.
    @org.junit.Test
    public void findElementByNameTest() throws Exception {
        WebElement posterList = null;
        try {
            posterList = driver.findElement(By.name("TextEdit"));
        } catch (NoSuchElementException exception) {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(posterList);
    }

    // Confirm we throw a NoSuchElementException when the element was not found.
    @org.junit.Test
    public void findElementNotFoundTest() throws Exception {
        boolean exceptionThrown = false;

        try {
            WebElement posterItem = driver.findElement(By.className("DoesNotExistView"));
        } catch (NoSuchElementException exception) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

    // Confirm we can find multiple elements using the class name strategy.
    @org.junit.Test
    public void findMultipleElementsTest() throws Exception {
        int expectedCount = 4;
        List<WebElement> atlasTextSceneViewList = driver
                .findElementsByClassName("CYIAtlasTextSceneNode");
        utils.outputListContents(atlasTextSceneViewList);

        Assert.assertEquals(atlasTextSceneViewList.size(), expectedCount);
    }

    /* Confirm we can perform a relative find. The parent is found using the class name strategy
     * and the child is found using the name strategy. */
    @org.junit.Test
    public void findSingleElementFromElementTest() throws Exception {
        WebElement buttonView = driver.findElement(By.className("CYITextEditView"));
        WebElement childItem = buttonView.findElement(By.name("Text"));

        String foundText = childItem.getText();
        String expectedText = "TextEdit";
        Assert.assertEquals(expectedText, foundText);
    }

    /* Confirm we can perform a relative find resulting in multiple elements. The parent is found
     * using the class name strategy and its children are also found using the class name strategy.
     * */
    @org.junit.Test
    public void findMultipleElementsFromElementTest() throws Exception {
        WebElement listContainer = driver.findElement(By.className("CYIScreenView"));
        List<WebElement> labelItems = listContainer.findElements(
                By.className("CYIAtlasTextSceneNode"));
        utils.outputListContents(labelItems);

        int expectedCount = 4;
        int actualCount = labelItems.size();
        Assert.assertEquals(expectedCount, actualCount);
    }

    // Confirm we get the text of a button.
    @org.junit.Test
    public void getTextTest() throws Exception {
        WebElement pushButton = driver.findElement(By.className("CYIPushButtonView"));

        String foundText = pushButton.findElement(By.name("Text")).getText();
        String expectedText = "Pushed 0 Times";
        Assert.assertEquals(expectedText, foundText);
    }

    // Confirm we can retrieve the name attribute of an input field.
    @org.junit.Test
    public void getNameTest() throws Exception {
        String expected = "TextEdit";

        WebElement inputField = driver.findElementByName(expected);
        String actual = inputField.getAttribute("name");

        Assert.assertEquals(expected, actual);
    }

    // Confirm we can set the text of an input field.
    @org.junit.Test
    public void valueSetTest() throws Exception {
        String expected = "One Two 3";
        WebElement field = driver.findElement(By.name("TextEdit"));
        field.sendKeys(expected);
        utils.delayInSeconds(2);

        String found = field.findElement(By.name("Text")).getText();
        Assert.assertEquals(expected, found);
    }

    //TODO create one with special keys to validate we support special keys

    // Confirm we can stop and then start up the app.
    @org.junit.Test
    public void startStopAppTest() throws Exception {
        driver.closeApp();
        utils.delayInSeconds(5);
        driver.launchApp();
        utils.delayInSeconds(5);
    }

    // Confirm we can send the app to the background for a short time.
    @org.junit.Test
    public void runInBackgroundTest() throws Exception {
        driver.runAppInBackground(10);
        utils.delayInSeconds(3);
    }

    // Confirm we can toggle the Android device's location services.
    @org.junit.Test
    public void toggleLocationServicesTest() throws Exception {
        try {
            driver.toggleLocationServices(); // off
            utils.delayInSeconds(5);
            driver.toggleLocationServices(); // on
            utils.delayInSeconds(5);
        } catch (NoSuchMethodException nsmException) {
            if (!driver.appPlatform.equals(driver.IOS)) {
                Assert.fail("NoSuchMethodException was thrown when not on iOS.");
            } else {
                System.out.println("\nExpected exception was thrown.");
            }
        }
    }

    // Confirm we can get the context.
    @org.junit.Test
    public void getContextTest() throws Exception {
        String contextValue = driver.getContext();

        Assert.assertNotNull(contextValue);
        System.out.println("\nContext value: " + contextValue);
    }

    /* Confirm we can get all contexts.
     * NOTE: YouiEngine currently only supports one context. */
    @org.junit.Test
    public void getContextsTest() throws Exception {
        Set<String> contextValues = driver.getContextHandles();

        Assert.assertNotNull(contextValues);
        System.out.println("\nContext values: " + contextValues);
    }

    // Confirm we can get the session id.
    @org.junit.Test
    public void getSessionIdTest() throws Exception {
        SessionId sessionId = driver.getSessionId();
        Assert.assertNotNull(sessionId);

        System.out.println("\nSession Id: " + sessionId);
    }

    /* Confirm we can remove the app.
     * NOTE: Only supported on Android and this will also shut down the driver. */
    @org.junit.Test
    public void removeAppTest() throws Exception {
        String bundleId = "tv.youi.YouiEngineAppiumSample";

        boolean actual = false;
        try {
            driver.removeApp(bundleId);
            actual = true; // did not throw exception
        } catch (WebDriverException wdException) {
            if (!driver.appPlatform.equals(driver.IOS)) {
                Assert.fail("WebDriverException was thrown when not on iOS.");
            } else {
                System.out.println("\nExpected exception was thrown.");
                actual = true;
            }
        }
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Confirm we can determine if an app is installed.
    @org.junit.Test
    public void isAppInstalledTest() throws Exception {
        String bundleId = "tv.youi.YouiEngineAppiumSample";
        boolean actual = false;
        try {
            actual = driver.isAppInstalled(bundleId);
        } catch (WebDriverException wdException) {
            if (!driver.appPlatform.equals(driver.IOS)) {
                Assert.fail("WebDriverException was thrown when not on iOS.");
            } else {
                System.out.println("\nExpected exception was thrown.");
                actual = true; // did not get a value, but we threw an exception for iOS
            }
        }
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Regression - ensure no exceptions occur when sending a click to a CYIAtlasTextSceneNode.
    @org.junit.Test
    public void clickOnCyiTextAtlasTest() throws Exception {
        WebElement textLabel = driver.findElement(By.className("CYIAtlasTextSceneNode"));
        try {
            textLabel.click();
        } catch (Exception ex) {
            // we don't want any exceptions to be thrown
            Assert.fail("An exception occurred when clicking on a CYIAtlasTextSceneNode.");
        }
    }

    //TODO the following tests need more testing or actual implementation in the YouiEngine driver
    /* @org.junit.Test
    public void installAppTest() throws Exception {
    } */

    /* @org.junit.Test
    public void isLockedTest() throws Exception {
    } */

    /* @org.junit.Test
    public void findElementByIdTest() throws Exception {
    } */

    // TODO update these to reflect the correct error
    /* @org.junit.Test
    public void findElementByXPathTest() throws Exception {
    } */

    /* @org.junit.Test
    public void findElementByCssSelectorTest() throws Exception {
    } */

    /* @org.junit.Test
    public void findElementByTagNameTest() throws Exception {
    } */

    /* @org.junit.Test
    public void findElementByLinkTextTest() throws Exception {
    } */

    /* @org.junit.Test
    public void findElementByPartialLinkTextTest() throws Exception {
    } */
}

