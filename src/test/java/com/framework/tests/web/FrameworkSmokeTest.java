package com.framework.tests.web;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.orchestrator.base.BaseTest;

/**
 * Framework smoke test.
 *
 * Purpose:
 * Verify framework startup works.
 */
public class FrameworkSmokeTest extends BaseTest {

    @Test
    public void verifyApplicationLoads() {

        String title = driver.getTitle();

        System.out.println("Page Title = " + title);

        Assert.assertFalse(
                title.isBlank(),
                "Page title should not be empty");
    }
}
