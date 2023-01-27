package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Test;

import static org.junit.Assert.*;

public class GlobalFileServiceTest extends TestFixtureBase
{
    String testfileName = "testGlobalFile.png";
    String testFileId = "ed2d2924-4650-4a88-b095-94b75ce9aa18";
    String testFolderPath = "/fname/";

    @Test
    public void testGetFileInfo() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalFileService().getFileInfo(
            testFileId,
            tr);

        tr.Run();
    }

    @Test
    public void testGetFileInfoSimple() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalFileService().getFileInfoSimple(
                testFolderPath,
                testfileName,
                tr);

        tr.Run();
    }

    @Test
    public void testGetGlobalCDNUrl() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalFileService().getGlobalCDNUrl(
                testFileId,
                tr);

        tr.Run();
    }

    @Test
    public void testGetGlobalFileList() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalFileService().getGlobalFileList(
                testFolderPath,
                true,
                tr);

        tr.Run();
    }
}
