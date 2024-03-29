package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;


public class App
{
  private WebDriver chromeDriver;

//  private static final String baseUrl = "https://www.nmu.org.ua/ua/";
    private static final String baseUrl = "https://operahouse.od.ua/";

  @BeforeClass(alwaysRun = true)
  public void setUp(){

      WebDriverManager.chromedriver().setup();
      ChromeOptions chromeOptions = new ChromeOptions();

      chromeOptions.addArguments("--start-fullscreen");

      chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));

      this.chromeDriver = new ChromeDriver(chromeOptions);
  }

  @BeforeMethod
  public void preconditions() {
      chromeDriver.get(baseUrl);
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
      chromeDriver.quit();
  }

  @Test
  public void testClickButtonToEventsPage() {
      WebElement eventsButton = chromeDriver.findElement(By.xpath("/html/body/main/div/div[1]/div[3]/a"));
      String eventsPagUrl = "afisha/";

      eventsButton.click();
      Assert.assertEquals( chromeDriver.getCurrentUrl(),baseUrl+eventsPagUrl);
  }

  @Test
   public void testRepertoireTabExists() {
      WebElement header = chromeDriver.findElement(By.id("menu-item-2414"));

      Assert.assertNotNull(header);
  }

    @Test
    public void testInputIntoSearchField() {
        WebElement searchBtn = chromeDriver.findElement(By.xpath("/html/body/header/div[1]/div[1]/a"));

        searchBtn.click();

        WebElement input = chromeDriver.findElement(By.id("search-form-1"));

        Assert.assertTrue(input.isDisplayed());

        String inputValue = "Дон Кіхот";

        input.sendKeys(inputValue);

        Assert.assertEquals(input.getAttribute("value"), inputValue);
    }

//  @Test
//    public void testHeaderExists() {
//      WebElement header = chromeDriver.findElement(By.id("heder"));
//
//      Assert.assertNotNull(header);
//  }
//
//  @Test
//  public void testClickOnForStudents() {
//      WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a/span"));
//
//      Assert.assertNotNull(forStudentButton);
//      forStudentButton.click();
//
//      Assert.assertNotNull(chromeDriver.getCurrentUrl(), baseUrl);
//  }
//
//  @Test
//  public void testSearchFieldOnForStudentPage() {
//      String studentPageUrl = "content/students/";
//      chromeDriver.get(baseUrl + studentPageUrl);
//
//      WebElement searchField = chromeDriver.findElement(By.tagName("input"));
//
//      Assert.assertNotNull(searchField);
//
//      String inputValue = "I need info";
//
//      searchField.sendKeys(inputValue);
//
//      Assert.assertEquals(searchField.getText(), inputValue);
//
//      searchField.sendKeys(Keys.ENTER);
//
//      Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
//  }
//
//  @Test
//  public void testSlider() {
//      WebElement nextButton = chromeDriver.findElement(By.className("next"));
//
//      WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("a.next"));
//
//      Assert.assertEquals(nextButton, nextButtonByCss);
//
//      WebElement previousButton = chromeDriver.findElement(By.className("prev"));
//
//      for (int i = 0; i < 20; i++) {
//          if (nextButton.getAttribute("class").contains("disabled")) {
//              previousButton.click();
//              Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
//              Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
//          } else {
//              nextButton.click();
//              Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
//              Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
//          }
//
//      }
//  }

}
