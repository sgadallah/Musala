import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test_Cases {


    private static final String FILENAME = "./testData.xml";
    private static final String Test_CV = "C:\\Users\\Developer\\Desktop\\test_cv.docx";
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    ConfigFileReader configFileReader= new ConfigFileReader();
    public Test_Cases() throws IOException {
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() throws ParserConfigurationException, IOException, SAXException {
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(FILENAME));
        doc.getDocumentElement().normalize();
        String invalid_email_1 = doc.getElementsByTagName("invalidEmail").item(0).getTextContent();
        String invalid_email_2 = doc.getElementsByTagName("invalidEmail").item(1).getTextContent();
        String invalid_email_3 = doc.getElementsByTagName("invalidEmail").item(2).getTextContent();
        String invalid_email_4 = doc.getElementsByTagName("invalidEmail").item(3).getTextContent();
        String invalid_email_5 = doc.getElementsByTagName("invalidEmail").item(4).getTextContent();
        return new Object[][] { { invalid_email_1 }, { invalid_email_2 }, { invalid_email_3 },
                { invalid_email_4 }, { invalid_email_5 } };
    }
    @DataProvider(name = "Cities")
    public Object[][] dataProviderMethodCities() throws ParserConfigurationException, IOException, SAXException {
        return new Object[][] { { "Sofia" }, { "Skopje" }};
    }
    WebDriver driver;
    public WebDriver getDriver(){
        if(configFileReader.getBrowser().equals("chrome")){
            System.setProperty("webdriver.chrome.driver", configFileReader.getChromeDriverPath());
            driver = new ChromeDriver();
        }
        else {
            System.setProperty("webdriver.gecko.driver", configFileReader.getGeckoDriverPath());
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS) ;
        return driver;
    }

    @Test(priority=1,dataProvider = "data-provider")
    public void insert_invalid_email(String data) {
            WebDriver driver = getDriver();
            driver.get(configFileReader.getProjectUrl());
            WebElement contact_us_button = driver.findElement(By.xpath("//span[@data-alt='Contact us']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contact_us_button);
            contact_us_button.click();
            WebElement name_input = driver.findElement(By.xpath("//input[@name='your-name']"));
            name_input.sendKeys("first name");
            WebElement email_input = driver.findElement(By.xpath("//input[@name='your-email']"));
            email_input.sendKeys(data);
            WebElement mobile_input = driver.findElement(By.xpath("//input[@name='mobile-number']"));
            mobile_input.sendKeys("0021061061061");
            WebElement subject_input = driver.findElement(By.xpath("//input[@name='your-subject']"));
            subject_input.sendKeys("Test Subject");
            WebElement message_input = driver.findElement(By.xpath("//textarea[@name='your-message']"));
            message_input.sendKeys("Test Message");
            WebElement send_button = driver.findElement(By.xpath("//input[@value='Send']"));
            send_button.click();
            int invalid_email_error = driver.findElements(By.xpath("//span[text()='The e-mail address entered is invalid.']")).size();
            System.out.println("Count of invalid email address = "+invalid_email_error);
            Assert.assertEquals(invalid_email_error,1);
            System.out.println("The invalid e-mail address used is: "+data);
            driver.quit();
        }

    @Test(priority=2)
    public void verify_urls() {
        WebDriver driver = getDriver();
        driver.get(configFileReader.getProjectUrl());
        WebElement company_link = driver.findElement(By.xpath("//*[@id='menu-main-nav-1']/li[1]/a"));
        company_link.click();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.musala.com/company/");
        WebElement leadership_section = driver.findElement(By.xpath("//*[@id='content']/div[2]/section[3]/div/h2"));
        Assert.assertEquals(leadership_section.getText(),"Leadership");
        WebElement accept_button = driver.findElement(By.xpath("//a[text()='ACCEPT']"));
        accept_button.click();
        WebElement facebookLink = driver.findElement(By.xpath("/html/body/footer/div/div/a[4]/span"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", facebookLink);
        facebookLink.click();
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.facebook.com/MusalaSoft?fref=ts");
        WebElement profile_picture = driver.findElement(By.xpath("//a[@aria-label='Musala Soft profile photo']"));
        Assert.assertEquals(profile_picture.getAttribute("href"),"https://www.facebook.com/MusalaSoft/photos/a.152166551470703/3926723730681614/?__tn__=%3C");
        driver.quit();
    }

    @Test(priority=1)
    public void check_vacancies() {
        WebDriver driver = getDriver();
        driver.get(configFileReader.getProjectUrl());
        WebElement career_link = driver.findElement(By.xpath("//*[@id='menu-main-nav-1']/li[5]/a"));
        career_link.click();
        WebElement positions_button = driver.findElement(By.xpath("//span[@data-alt='Check our open positions']"));
        positions_button.click();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.musala.com/careers/join-us/");
        Select locations_select = new Select(driver.findElement(By.id("get_location")));
        locations_select.selectByVisibleText("Anywhere");
        WebElement accept_button = driver.findElement(By.xpath("//a[text()='ACCEPT']"));
        accept_button.click();
        WebElement automation_position = driver.findElement(By.xpath("//img[@alt='Automation QA Engineer']"));
        automation_position.click();
        int general_description_title = driver.findElements(By.xpath("//h2[text()='General description']")).size();
        int requirements_title = driver.findElements(By.xpath("//h2[text()='Requirements']")).size();
        int responsibilities_title = driver.findElements(By.xpath("//h2[text()='Responsibilities']")).size();
        int what_we_offer_title = driver.findElements(By.xpath("//h2[text()='What we offer']")).size();
        Assert.assertEquals(general_description_title,1);
        Assert.assertEquals(requirements_title,1);
        Assert.assertEquals(responsibilities_title,1);
        Assert.assertEquals(what_we_offer_title,1);
        WebElement apply_button = driver.findElement(By.xpath("//input[@value='Apply']"));
        int apply = driver.findElements(By.xpath("//input[@value='Apply']")).size();
        System.out.println(apply);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", apply_button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", apply_button);
        WebElement email_input = driver.findElement(By.xpath("//input[@name='your-email']"));
        WebElement name_input = driver.findElement(By.xpath("//input[@name='your-name']"));
        name_input.sendKeys("first name");
        WebElement mobile_input = driver.findElement(By.xpath("//input[@name='mobile-number']"));
        mobile_input.sendKeys("0021061061061");
        email_input.sendKeys("test@invalid");
        driver.findElement(By.id("uploadtextfield")).sendKeys(Test_CV);
        WebElement agree_checkbox = driver.findElement(By.id("adConsentChx"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agree_checkbox);
        WebElement send_button = driver.findElement(By.xpath("//input[@value='Send']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", send_button);
        WebElement close_button = driver.findElement(By.xpath("//button[text()='Close']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", close_button);
        int invalid_email_count = driver.findElements(By.xpath("//span[text()='The e-mail address entered is invalid.']")).size();
        int field_is_required_count = driver.findElements(By.xpath("//span[text()='The field is required.']")).size();
        int not_a_robot_count = driver.findElements(By.xpath("//span[text()='Please verify that you are not a robot.']")).size();
        System.out.println(invalid_email_count);
        System.out.println(field_is_required_count);
        System.out.println(not_a_robot_count);
        Assert.assertEquals(invalid_email_count,1);
        Assert.assertEquals(field_is_required_count,2);
        Assert.assertEquals(not_a_robot_count,2);
        driver.quit();
    }

    @Test(priority=1,dataProvider = "Cities")
    public void print_to_console(String city) {
        WebDriver driver = getDriver();
        driver.get(configFileReader.getProjectUrl());
        WebElement career_link = driver.findElement(By.xpath("//*[@id='menu-main-nav-1']/li[5]/a"));
        career_link.click();
        WebElement positions_button = driver.findElement(By.xpath("//span[@data-alt='Check our open positions']"));
        positions_button.click();
        Select locations_select = new Select(driver.findElement(By.id("get_location")));
        locations_select.selectByVisibleText(city);
        WebElement accept_button = driver.findElement(By.xpath("//a[text()='ACCEPT']"));
        accept_button.click();

        List<WebElement> All_Cities = driver.findElements(By.xpath("//article/div/a/div/div[1]/p[2]"));
        System.out.println(city+"\n");
        for (WebElement element : All_Cities) {
            if(element.getText().equals(city)){
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                WebElement parentElement = (WebElement)executor.executeScript("return arguments[0].parentNode;", element);
                WebElement position_child_element = parentElement.findElement(By.xpath(".//h2"));
                System.out.println("Position: "+position_child_element.getText());
                System.out.println("More info: http://www.musala.com/job/"+position_child_element.getText().replace(' ', '-')+"/");
            }
        }
        driver.quit();
    }

}

