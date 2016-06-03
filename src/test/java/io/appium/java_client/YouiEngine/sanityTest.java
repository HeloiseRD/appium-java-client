package io.appium.java_client.YouiEngine;

import io.appium.java_client.YouiEngine.util.AppiumTest;
import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.SessionId;

import static org.hamcrest.CoreMatchers.not;


public class sanityTest extends AppiumTest {
        @org.junit.Test
        public void pageSourceTest() throws Exception {
            delayInSeconds(15);

            String source;
            source = driver.getPageSource();
            System.out.println("\nPageSource: " + source);
            Assert.assertThat(source, not(""));

            delayInSeconds(3);
        }

        @org.junit.Test
        public void screenshotTest() throws Exception {
            delayInSeconds(15);

            File screenShot = driver.getScreenshotAs(OutputType.FILE);
            File output = new File("screenShot.PNG");
            FileUtils.copyFile(screenShot, output);

            System.out.println("\nOutput: " + output.getAbsolutePath());

            delayInSeconds(3);
        }

        @org.junit.Test
        public void screenshotWithPathTest() throws Exception {
            delayInSeconds(15);

            String fileWithPath = Paths.get(System.getProperty("user.home")) + "/Desktop/Screenshots/screenShot.PNG";
            File output = new File(fileWithPath);

            File screenShot = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenShot, output);

            System.out.println("\nOutput: " + output.getAbsolutePath());

            delayInSeconds(3);
        }

        // ---- START OF findElement TESTS -------------------------------------------------------------------------

        @org.junit.Test
        public void findElementByClassTest() throws Exception {
            delayInSeconds(15);

            WebElement posterItem = null;
            try {
                posterItem = driver.findElement(By.className("LanderItemView"));
            }
            catch (NoSuchElementException exception)
            {
                Assert.fail("Did not find the control.");
            }

            Assert.assertNotNull(posterItem);

            delayInSeconds(3);
        }

        @org.junit.Test
        public void findElementByNameTest() throws Exception {
            delayInSeconds(15);

            WebElement posterList = null;
            try {
                posterList = driver.findElement(By.name("PosterList"));
            }
            catch (NoSuchElementException exception)
            {
                Assert.fail("Did not find the control.");
            }

            Assert.assertNotNull(posterList);

            delayInSeconds(3);
        }

        // TODO update to reflect this was not implemented
//    @org.junit.Test
//    public void findElementByCssSelectorTest() throws Exception {
//        delayInSeconds(15);
//
//        WebElement posterList = null;
//        try {
//            posterList = driver.findElement(By.cssSelector("???"));
//        }
//        catch (NoSuchElementException exception)
//        {
//            Assert.fail("Did not find the control.");
//        }
//
//        Assert.assertNotNull(posterList);
//
//        delayInSeconds(3);
//    }

        //TODO not reliable to use ID while we're dynamically assigning them
//    @org.junit.Test
//    public void findElementByIdTest() throws Exception {
//        delayInSeconds(15);
//
//        WebElement posterList = null;
//        try {
//            posterList = driver.findElement(By.id("0"));
//        }
//        catch (NoSuchElementException exception)
//        {
//            Assert.fail("Did not find the control.");
//        }
//
//        Assert.assertNotNull(posterList);
//
//        delayInSeconds(3);
//    }

        // TODO update this to reflect the correct error
//    @org.junit.Test
//    public void findElementByXPathTest() throws Exception {
//        delayInSeconds(15);
//
//        WebElement sceneView = null;
//        try {
//            sceneView = driver.findElement(By.xpath("./AppiumUAT/CYISceneView"));
//        }
//        catch (NoSuchElementException exception)
//        {
//            Assert.fail("Did not find the control.");
//        }
//
//        Assert.assertNotNull(sceneView);
//
//        delayInSeconds(3);
//    }

//        @org.junit.Test
//        public void findElementByTagNameTest() throws Exception {
//            delayInSeconds(15);
//
//            WebElement posterItem = null;
//            try {
//                posterItem = driver.findElement(By.tagName("LanderItemView"));
//            }
//            catch (NoSuchElementException exception)
//            {
//                Assert.fail("Did not find the control.");
//            }
//
//            Assert.assertNotNull(posterItem);
//
//            delayInSeconds(3);
//        }

        @org.junit.Test
        public void findElementNotFoundTest() throws Exception {
            delayInSeconds(15);

            boolean exceptionThrown = false;

            try {
                WebElement posterItem = driver.findElement(By.className("DoesNotExistView"));
            }
            catch (NoSuchElementException exception)
            {
                exceptionThrown = true;
            }
            Assert.assertTrue(exceptionThrown);

            delayInSeconds(3);
        }

        // ---- END OF findElement TESTS ---------------------------------------------------------------------------

        // ---- START OF findElements TESTS ------------------------------------------------------------------------
        @org.junit.Test
        public void findElementsTest() throws Exception {
            delayInSeconds(15);

            int expectedCount = 5;

            List<WebElement> landerItemViewList = driver.findElementsByClassName("LanderItemView");
            outputListContents(landerItemViewList);

            Assert.assertEquals(landerItemViewList.size(), expectedCount);

            delayInSeconds(3);
        }

        @org.junit.Test
        public void findElementFromElementTest() throws Exception {
            delayInSeconds(15);
            WebElement firstPosterItem = driver.findElement(By.className("LanderItemView"));

            WebElement nextPosterItem = firstPosterItem.findElement(By.className("LanderItemView"));
            nextPosterItem.click();
            delayInSeconds(15);

            System.out.println("\nShould be on Llama Drama...");
            WebElement backButton = driver.findElement(By.name("Btn-Back"));
            backButton.click();
            delayInSeconds(15);
        }

        @org.junit.Test
        public void findElementsFromElementTest() throws Exception {
            delayInSeconds(15);
            int expectedCount = 5;

            WebElement listContainer = driver.findElement(By.className("CYIListView"));
            List<WebElement> posterItems = listContainer.findElements(By.className("LanderItemView"));
            outputListContents(posterItems);

            Assert.assertEquals(posterItems.size(), expectedCount);

            delayInSeconds(15);
        }

        // ---- END OF findElements TESTS --------------------------------------------------------------------------

        @org.junit.Test
        public void getTextTest() throws Exception {
            delayInSeconds(15);

            String expected = "Big Buck Bunny";
            String actual;

            WebElement textCaption = driver.findElementByName("placeholder-title");
            System.out.println(textCaption);
            Assert.assertNotNull(textCaption);

            actual = textCaption.getText();
            System.out.println("\nActual text: '" + actual + "'\nExpected text: '" + expected + "'");

            Assert.assertEquals(expected, actual);

            delayInSeconds(3);
        }

        @org.junit.Test
        public void getNameTest() throws Exception {
            delayInSeconds(15);

            String expected = "placeholder-title";
            String actual;

            WebElement textCaption = driver.findElementByName(expected);
            actual = textCaption.getAttribute("name");

            System.out.println("\nActual text: '" + actual + "'\nExpected text: '" + expected + "'");

            Assert.assertEquals(expected, actual);

            delayInSeconds(3);
        }

        // TODO need a better sample to send keys to because this doesn't actually do anything because the posteritem
        // isn't hooked up to take an 'enter' key on a handset.
        @org.junit.Test
        public void valueSetTest() throws Exception {
            delayInSeconds(15);

            WebElement posterItem = driver.findElement(By.className("LanderItemView"));
            posterItem.sendKeys(Keys.ENTER);
            // To get this to work, you need to use the InputFieldTester app.
//            WebElement textfield = driver.findElement(By.className("CYITextEditView"));
//            textfield.sendKeys("TestText");
//            delayInSeconds(3);
//            textfield.sendKeys(Keys.DELETE);
//            delayInSeconds(10);
        }

        @org.junit.Test
        public void startStopAppTest() throws Exception {
            delayInSeconds(15);

            driver.closeApp();
            delayInSeconds(5);
            driver.launchApp();
            delayInSeconds(15);
        }

        //TODO this does not appear to be implemented in the appium-ios-driver
//    @org.junit.Test
//    public void installAppTest() throws Exception {
//        delayInSeconds(15);
//
//        String androidAppPath = Paths.get(System.getProperty("user.dir"), "../../../../uswish/samples/VideoPlayer/build/AndroidNative/bin/VideoPlayer-debug.apk").toAbsolutePath().toString();
//        driver.installApp(androidAppPath);
//
//        delayInSeconds(3);
//    }

        @org.junit.Test
        public void runInBackgroundTest() throws Exception {
            delayInSeconds(15);

            driver.runAppInBackground(10);
            delayInSeconds(3);
        }

        @org.junit.Test
        public void toggleLocationServicesTest() throws Exception {
            delayInSeconds(15);
            try {
                driver.toggleLocationServices(); // off
                delayInSeconds(5);
                driver.toggleLocationServices(); // on
                delayInSeconds(5);
            } catch (NoSuchMethodException nsmException) {
                if (!driver.appPlatform.equals(driver.IOS)) {
                    Assert.fail("NoSuchMethodException was thrown when not on iOS.");
                } else {
                    System.out.println("Expected exception was thrown.");
                }
            }
        }

        @org.junit.Test
        public void getContextTest() throws Exception {
            delayInSeconds(15);

            String contextValue = driver.getContext();

            Assert.assertNotNull(contextValue);
            System.out.println("\nContext value: " + contextValue);
            delayInSeconds(5);
        }

        @org.junit.Test
        public void getContextsTest() throws Exception {
            delayInSeconds(15);

            Set<String> contextValues = driver.getContextHandles();

            Assert.assertNotNull(contextValues);
            System.out.println("\nContext values: " + contextValues);
            delayInSeconds(5);
        }

        @org.junit.Test
        public void isLockedTest() throws Exception {
            delayInSeconds(15);

            try {
                driver.isLocked();
            } catch (NoSuchMethodException nsmException) {
                if (!driver.appPlatform.equals(driver.IOS)) {
                    Assert.fail("NoSuchMethodException was thrown when not on iOS.");
                } else {
                    System.out.println("Expected exception was thrown.");
                }
            }

            delayInSeconds(5);
        }

        @org.junit.Test
        public void keysTest() throws Exception {
            delayInSeconds(15);

            //driver.sendKeys(); // does not appear to exist?
            delayInSeconds(5);
        }

        @org.junit.Test
        public void getSessionIdTest() throws Exception {
            delayInSeconds(15);

            SessionId sessionId = driver.getSessionId();
            Assert.assertNotNull(sessionId);

            System.out.println("\nSession Id: " + sessionId);
            delayInSeconds(5);
        }

        @org.junit.Test
        public void removeAppTest() throws Exception {
            String bundleId = "tv.youi.VideoPlayer";
            delayInSeconds(15);

            driver.removeApp(bundleId);

            delayInSeconds(5);

        }

        @org.junit.Test
        public void isAppInstalledTest() throws Exception {
            String bundleId = "tv.youi.VideoPlayer";
            delayInSeconds(15);

            System.out.println("\nIs App Installed: " + driver.isAppInstalled(bundleId));

            delayInSeconds(5);
        }
        // ---- HELPER METHODS ----------------------------------------------------------------------------------------

        // create a little delay in seconds
        private void delayInSeconds(long delay) throws InterruptedException {
            delay = delay*1000;
            Thread.sleep(delay);
        }

        // print the contents of the list out to the console
        private void outputListContents(List<WebElement> listItems) {
            System.out.println("\nList Contents:");
            for(Iterator<WebElement> item = listItems.iterator(); item.hasNext(); ) {
                System.out.println("\tItem: " + item.next());
            }
        }
}

