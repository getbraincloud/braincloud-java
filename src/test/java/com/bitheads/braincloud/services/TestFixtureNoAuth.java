package com.bitheads.braincloud.services;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class TestFixtureNoAuth extends TestFixtureBase
{
    @Override
    public boolean shouldAuthenticate()
    {
        return false;
    }

}
