package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Dictionary;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class GlobalEntityServiceTest extends TestFixtureBase {
    private final String _defaultEntityType = "testGlobalEntity";
    private final String _defaultEntityValueName = "globalTestName";
    private final String _defaultEntityValue = "Test Name 01";

    @Test
    public void testCreateEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalEntityService().createEntity(
                _defaultEntityType,
                3434343,
                null,
                Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                tr);

        tr.Run();
    }

    @Test
    public void testCreateEntityWithIndexedId() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalEntityService().createEntityWithIndexedId(
                _defaultEntityType,
                "indexedIdTest",
                3434343,
                null,
                Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().updateEntity(
                entityId,
                1,
                Helpers.createJsonPair(_defaultEntityValueName, "Test Name 02 Changed"),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateEntityAcl() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().updateEntityAcl(
                entityId,
                1,
                ACL.readWriteOther().toJsonString(),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateEntityTimeToLive() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().updateEntityTimeToLive(
                entityId,
                1,
                1000,
                tr);

        tr.Run();
    }

    @Test
    public void testDeleteEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().deleteEntity(
                entityId,
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testReadEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().readEntity(
                entityId,
                tr);

        tr.Run();
    }

    @Test
    public void testGetList() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        createDefaultGlobalEntity();
        createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().getList(
                Helpers.createJsonPair("entityType", _defaultEntityType),
                Helpers.createJsonPair("data.name", 1),
                1000,
                tr);

        tr.Run();
    }

    @Test
    public void testGetListByIndexedId() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String indexedId = "testIndexedId";

        createDefaultGlobalEntity(ACL.Access.None, indexedId);
        createDefaultGlobalEntity(ACL.Access.None, indexedId);

        _wrapper.getGlobalEntityService().getListByIndexedId(
                indexedId,
                100,
                tr);

        tr.Run();
    }

    @Test
    public void testGetListCount() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        createDefaultGlobalEntity();
        createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().getListCount(
                Helpers.createJsonPair("entityType", _defaultEntityType),
                tr);

        tr.Run();
    }

    @Test
    public void testGetPage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        generateDefaultEntitites(200);

        _wrapper.getGlobalEntityService().getPage(
                createContext(125, 1, _defaultEntityType),
                tr);

        tr.Run();
    }

    @Test
    public void testGetPageOffset() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        generateDefaultEntitites(200);

        _wrapper.getGlobalEntityService().getPage(
                createContext(50, 1, _defaultEntityType),
                tr);
        tr.Run();

        int page = 0;
        page = tr.m_response.getJSONObject("data").getJSONObject("results").getInt("page");

        String context = tr.m_response.getJSONObject("data").getString("context");

        _wrapper.getGlobalEntityService().getPageOffset(
                context,
                page,
                tr);

        tr.Run();
    }

    @Test
    public void testIncrementGlobalEntityData() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalEntityService().createEntity(
                _defaultEntityType,
                3434343,
                "",
                Helpers.createJsonPair("test", 1234),
                tr);
        tr.Run();

        String entityId = getEntityId(tr.m_response);

        _wrapper.getGlobalEntityService().incrementGlobalEntityData(
                entityId,
                Helpers.createJsonPair("test", 1234),
                tr);
        tr.Run();
    }

    @Test
    public void testGetRandomEntitiesMatching() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalEntityService().getRandomEntitiesMatching("{data.property : entityType}", 2,tr);

        tr.Run();
    }

    @Test
    public void testUpdateEntityIndexedId() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity(ACL.Access.None, "indexedIdTest");
        //_wrapper.getGlobalEntityService().createEntityWithIndexedId(_defaultEntityType,"indexedIdTest",0,null,Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),tr);


        _wrapper.getGlobalEntityService().updateEntityIndexedId(
                entityId,
                1,
                "indexedIdTest",
                tr);
        tr.Run();
    }


    @Test
    public void testUpdateEntityOwnerAndAcl() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().updateEntityOwnerAndAcl(
                entityId,
                -1,
                getUser(Users.UserA).profileId,
                ACL.readWriteOther().toJsonString(),
                tr);
        tr.Run();
    }

    @Test
    public void testMakeSystemEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String entityId = createDefaultGlobalEntity();

        _wrapper.getGlobalEntityService().makeSystemEntity(
                entityId,
                -1,
                ACL.readWriteOther().toJsonString(),
                tr);
        tr.Run();
    }


    ////// helpers

    private static String getEntityId(JSONObject json) throws Exception {
        return json.getJSONObject("data").getString("entityId");
    }

    private String createDefaultGlobalEntity() throws Exception {
        return createDefaultGlobalEntity(ACL.Access.None);
    }

    private String createDefaultGlobalEntity(ACL.Access accessLevel) throws Exception {
        return createDefaultGlobalEntity(accessLevel, "");
    }

    /// <summary>
    /// Creates a default entity on the server
    /// </summary>
    /// <param name="accessLevel"> accessLevel for entity </param>
    /// <returns> The ID of the entity </returns>
    private String createDefaultGlobalEntity(ACL.Access accessLevel, String indexedId) throws Exception {
        TestResult tr = new TestResult(_wrapper);

        ACL access = new ACL();
        access.setOther(accessLevel);
        String entityId = "";

        //Create entity
        if (indexedId.length() <= 0) {
            _wrapper.getGlobalEntityService().createEntity(
                    _defaultEntityType,
                    3434343,
                    access.toJsonString(),
                    Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                    tr);
        } else {
            _wrapper.getGlobalEntityService().createEntityWithIndexedId(
                    _defaultEntityType,
                    indexedId,
                    3434343,
                    access.toJsonString(),
                    Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                    tr);
        }

        if (tr.Run()) {
            entityId = getEntityId(tr.m_response);
        }

        return entityId;
    }

    private String createContext(int numberOfEntitiesPerPage, int startPage, String entityType) throws Exception {
        JSONObject context = new JSONObject();

        JSONObject pagination = new JSONObject();
        pagination.put("rowsPerPage", numberOfEntitiesPerPage);
        pagination.put("pageNumber", startPage);
        context.put("pagination", pagination);

        JSONObject searchCriteria = new JSONObject();
        searchCriteria.put("entityType", entityType);
        context.put("searchCriteria", searchCriteria);

        return context.toString();
    }

    private void generateDefaultEntitites(int numberOfEntites) throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGlobalEntityService().getListCount(
                Helpers.createJsonPair("entityType", _defaultEntityType),
                tr);

        tr.Run();

        int existing = tr.m_response.getJSONObject("data").getInt("entityListCount");

        numberOfEntites -= existing;
        if (numberOfEntites <= 0) return;

        for (int i = 0; i < numberOfEntites; i++) {
            createDefaultGlobalEntity(ACL.Access.ReadWrite);
        }
    }
}