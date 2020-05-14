package com.deesite.tests;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.deesite.base.BaseTest;
import com.deesite.utilities.TestUtils;

import pageobjects.HomePage;

public class VerifyNavigationToPrepTest extends BaseTest {
	@Test(dataProvider="dp", dataProviderClass=TestUtils.class, description="Verify - Navigation to practice test.")
	public void verifyNavigationToPracticeTest(Hashtable<String, String> data) throws Exception {
		System.out.println("Navigation to practice test.............");
		HomePage homePage = new HomePage().open();
		homePage.navigateToPracticeTestLink1()
		.checkTitle();
		
	}
	
	
}
