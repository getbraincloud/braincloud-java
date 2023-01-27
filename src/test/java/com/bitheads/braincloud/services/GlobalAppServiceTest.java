package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Test;
import java.util.Arrays;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class GlobalAppServiceTest extends TestFixtureBase
{

    @Test
    public void testReadProperties() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalAppService().readProperties(
                tr);

        tr.Run();
    }

    @Test
    public void testReadSelectedProperties() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> propertyNames = new ArrayList<String>(
            Arrays.asList("prop1", "prop2", "prop3")
        );

        _wrapper.getGlobalAppService().readSelectedProperties(propertyNames, tr);

        tr.Run();
    }

    @Test
    public void testReadPropertiesInCategories() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> categories = new ArrayList<String>(
            Arrays.asList("test")
        );

        _wrapper.getGlobalAppService().readPropertiesInCategories(categories, tr);

        tr.Run();
    }
}