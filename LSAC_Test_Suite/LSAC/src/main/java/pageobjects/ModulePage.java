package pageobjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class ModulePage extends BasePage {

    WebDriver driver;

    @FindBy(xpath = "//h1[@class='moduleHeader']")
    @AndroidFindBy(xpath = "//h1[@class='moduleHeader']")
    private WebElement lblModuleHeader;
    
    @Override
	public ExpectedCondition getPageLoadCondition() {
		
		return ExpectedConditions.visibilityOf(lblModuleHeader);
	}
    
    public ModulePage checkTitle() throws Exception {
    	fieldValidation(lblModuleHeader, "The Official LAST PrepTest 71", port, port);
        return (ModulePage) openPage(ModulePage.class);
    }
	

}