package pageobjects;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class HomePage extends BasePage {

    WebDriver driver;

    @FindBy(xpath = "//span[contains(., 'Library')]")
    @AndroidFindBy(xpath = "//span[contains(., 'Library')]")
    private WebElement homeTitleBtn;
    
    @FindBy(xpath = "//a[@id='practiceTestLink']")
    @AndroidFindBy(xpath = "//a[@id='practiceTestLink']")
    private List<WebElement> practiceTestLinks;
    
    
    @Override
	public ExpectedCondition getPageLoadCondition() {
		
		return ExpectedConditions.visibilityOf(homeTitleBtn);
	}
    
    public HomePage open() {
		return (HomePage) openPage(HomePage.class);
	}

    public HomePage checkTitle() throws Exception {
    	fieldValidation(homeTitleBtn, "Home Title Button", "true", "Library");
        return (HomePage) openPage(HomePage.class);
    }

    public ModulePage navigateToPracticeTestLink1() throws Exception {
    	click(practiceTestLinks.get(0), "Practice Test Link 1");
    	return (ModulePage) openPage(ModulePage.class);
    }

    
   

	

}