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


public class SanityTest extends AppiumTest {

    @org.junit.Test
    public void pageSourceTest() throws Exception {
        String source;
        source = driver.getPageSource();
        System.out.println("\nPageSource: " + source);
        Assert.assertThat(source, not(""));

    }

    @org.junit.Test
    public void screenshotTest() throws Exception {
        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        File output = new File("screenShot.PNG");
        FileUtils.copyFile(screenShot, output);

        System.out.println("\nOutput: " + output.getAbsolutePath());
    }

    @org.junit.Test
    public void screenshotWithPathTest() throws Exception {
        String fileWithPath = Paths.get(System.getProperty("user.home"))
                + "/Desktop/Screenshots/screenShot.PNG";
        File output = new File(fileWithPath);

        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, output);

        System.out.println("\nOutput: " + output.getAbsolutePath());
    }

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

    @org.junit.Test
    public void findMultipleElementsTest() throws Exception {
        int expectedCount = 4;
        List<WebElement> atlasTextSceneViewList = driver
                .findElementsByClassName("CYIAtlasTextSceneNode");
        utils.outputListContents(atlasTextSceneViewList);

        Assert.assertEquals(atlasTextSceneViewList.size(), expectedCount);
    }

    @org.junit.Test
    public void findSingleElementFromElementTest() throws Exception {
        WebElement buttonView = driver.findElement(By.className("CYITextEditView"));
        WebElement childItem = buttonView.findElement(By.name("Text"));

        String foundText = childItem.getText();
        String expectedText = "TextEdit";
        Assert.assertEquals(expectedText, foundText);
    }

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

    @org.junit.Test
    public void getTextTest() throws Exception {
        WebElement textLabel = driver.findElement(By.className("CYIAtlasTextSceneNode"));

        String foundText = textLabel.getText();
        String expectedText = "Pushed 0 Times";
        Assert.assertEquals(expectedText, foundText);
    }

    @org.junit.Test
    public void getNameTest() throws Exception {
        String expected = "TextEdit";

        WebElement textCaption = driver.findElementByName(expected);
        String actual = textCaption.getAttribute("name");

        Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void valueSetTest() throws Exception {
        //String expected = "One Two 3";
        String expected = "OneTwo3";
        WebElement field = driver.findElement(By.name("TextEdit"));
        field.sendKeys(expected);
        utils.delayInSeconds(2);

        String found = field.findElement(By.name("Text")).getText();
        Assert.assertEquals(expected, found);
    }

    //TODO create one with special keys

    @org.junit.Test
    public void startStopAppTest() throws Exception {
        driver.closeApp();
        utils.delayInSeconds(5);
        driver.launchApp();
        utils.delayInSeconds(5);
    }

    @org.junit.Test
    public void runInBackgroundTest() throws Exception {
        driver.runAppInBackground(10);
        utils.delayInSeconds(3);
    }

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

    @org.junit.Test
    public void getContextTest() throws Exception {
        String contextValue = driver.getContext();

        Assert.assertNotNull(contextValue);
        System.out.println("\nContext value: " + contextValue);
    }

    @org.junit.Test
    public void getContextsTest() throws Exception {
        Set<String> contextValues = driver.getContextHandles();

        Assert.assertNotNull(contextValues);
        System.out.println("\nContext values: " + contextValues);
    }

    @org.junit.Test
    public void getSessionIdTest() throws Exception {
        SessionId sessionId = driver.getSessionId();
        Assert.assertNotNull(sessionId);

        System.out.println("\nSession Id: " + sessionId);
    }

    @org.junit.Test
    public void removeAppTest() throws Exception {
        String bundleId = "tv.youi.SimpleViews";

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

    @org.junit.Test
    public void isAppInstalledTest() throws Exception {
        String bundleId = "tv.youi.SimpleViews";
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

    //TODO this does not appear to be implemented in the appium-ios-driver
    /* @org.junit.Test
    public void installAppTest() throws Exception {
        String appName = "VideoPlayer";
        String androidAppPath = Paths.get(System.getProperty("user.dir"),
                "../../../../uswish/samples/" + appName + "/build/AndroidNative/bin/" + appName
                        + "-debug.apk").toAbsolutePath().toString();
        driver.installApp(androidAppPath);
    } */

    // TODO needs to be right...
    /* @org.junit.Test
    public void isLockedTest() throws Exception {
        boolean locked;
        try {
            driver.isLocked();
        } catch (NoSuchMethodException nsmException) {
            if (!driver.appPlatform.equals(driver.IOS)) {
                Assert.fail("NoSuchMethodException was thrown when not on iOS.");
            } else {
                System.out.println("Expected exception was thrown.");
            }
        }
    } */

    // TODO fix to take a string map for the args
    /* @org.junit.Test
    public void keysTest() throws Exception {
        driver.execute("keys"); // does not appear to exist?
    } */

    // TODO update to reflect this was not implemented
    /* @org.junit.Test
    public void findElementByCssSelectorTest() throws Exception {
        WebElement posterList = null;
        try {
            posterList = driver.findElement(By.cssSelector("???"));
        } catch (NoSuchElementException exception) {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(posterList);
    } */

    //TODO not reliable to use ID while we're dynamically assigning them
    /* @org.junit.Test
    public void findElementByIdTest() throws Exception {
        WebElement posterList = null;
        try {
            posterList = driver.findElement(By.id("0"));
        } catch (NoSuchElementException exception) {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(posterList);
    } */

    // TODO update this to reflect the correct error
    /* @org.junit.Test
    public void findElementByXPathTest() throws Exception {
        utils.delayInSeconds(15);

        WebElement sceneView = null;
        try {
            sceneView = driver.findElement(By.xpath("./AppiumUAT/CYISceneView"));
        } catch (NoSuchElementException exception) {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(sceneView);

        utils.delayInSeconds(3);
    } */

    /* @org.junit.Test
    public void findElementByTagNameTest() throws Exception {
        WebElement posterItem = null;
        try {
            posterItem = driver.findElement(By.tagName("LanderItemView"));
        }
        catch (NoSuchElementException exception)
        {
            Assert.fail("Did not find the control.");
        }

        Assert.assertNotNull(posterItem);
    } */

}

