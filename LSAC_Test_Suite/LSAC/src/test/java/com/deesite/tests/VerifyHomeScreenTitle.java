package com.deesite.tests;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.deesite.base.BaseTest;
import com.deesite.utilities.TestUtils;

import pageobjects.HomePage;

public class VerifyHomeScreenTitle extends BaseTest {
	@Test(dataProvider="dp", dataProviderClass=TestUtils.class, description="Verify - Home screen title.")
	public void verifyHomeScreenTitle(Hashtable<String, String> data) throws Exception {
		System.out.println("Home Screen Title Test.............");
		HomePage homePage = new HomePage().open();
		homePage.checkTitle();
		
	}
	
	
}
