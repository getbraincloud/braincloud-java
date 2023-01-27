package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by prestonjennings on 15-09-01.
 */
public class EntityServiceTest extends TestFixtureBase {
    private final String _defaultEntityType = "address";
    private final String _defaultEntityValueName = "street";
    private final String _defaultEntityValue = "1309 Carling";

    @Test
    public void testCreateEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEntityService().createEntity(
                _defaultEntityType,
                Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                null,
                tr);

        tr.Run();
        deleteAllDefaultEntities();
    }

    @Test
    public void testDeleteEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultAddressEntity(ACL.Access.None);

        //Delete entity
        _wrapper.getEntityService().deleteEntity(entityId, 1, tr);

        tr.Run();
        deleteAllDefaultEntities();
    }

    @Test
    public void testDeleteSingleton() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        createDefaultAddressEntity(ACL.Access.ReadWrite);

        _wrapper.getEntityService().deleteSingleton(
                _defaultEntityType,
                1,
                tr);

        tr.Run();
        deleteAllDefaultEntities();
    }

    @Test
    public void testGetInstanceEntitiesByType() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        createDefaultAddressEntity(ACL.Access.None);

        //getInstanceEntity
        _wrapper.getEntityService().getEntitiesByType(_defaultEntityType, tr);

        tr.Run();
        deleteAllDefaultEntities();
    }

    @Test
    public void testGetInstanceEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultAddressEntity(ACL.Access.None);

        //getInstanceEntity
        _wrapper.getEntityService().getEntity(entityId, tr);

        tr.Run();
        deleteAllDefaultEntities();
    }


    @Test
    public void testGetInstanceSharedEntitiesForProfileId() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        createDefaultAddressEntity(ACL.Access.None);

        //getInstanceEntity
        _wrapper.getEntityService().getSharedEntitiesForProfileId(getUser(Users.UserA).profileId, tr);
        tr.Run();
        deleteAllDefaultEntities();
    }

    @Test
    public void testUpdateEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultAddressEntity(ACL.Access.None);

        //Update entity
        String updatedAddress = "1609 Bank St";

        _wrapper.getEntityService().updateEntity(
                entityId,
                _defaultEntityType,
                Helpers.createJsonPair(_defaultEntityValueName, updatedAddress),
                null,
                1,
                tr);

        tr.Run();
        deleteAllDefaultEntities(2);
    }


    @Test
    public void testUpdateSharedEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultAddressEntity(ACL.Access.ReadWrite);

        String updatedAddress = "1609 Bank St";

        _wrapper.getEntityService().updateSharedEntity(
                _wrapper.getClient().getAuthenticationService().getProfileId(),
                entityId,
                _defaultEntityType,
                Helpers.createJsonPair(_defaultEntityValueName, updatedAddress),
                1,
                tr);

        tr.Run();
        deleteAllDefaultEntities(2);
    }


    @Test
    public void testGetSharedEntityForProfileId() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultAddressEntity(ACL.Access.ReadWrite);

        _wrapper.getEntityService().getSharedEntityForProfileId(
                _wrapper.getClient().getAuthenticationService().getProfileId(),
                entityId,
                tr);

        tr.Run();
        deleteAllDefaultEntities(1);
    }

    @Test
    public void testUpdateSingleton() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        createDefaultAddressEntity(ACL.Access.ReadWrite);

        String updatedAddress = "1609 Bank St";

        _wrapper.getEntityService().updateSingleton(
                _defaultEntityType,
                Helpers.createJsonPair(_defaultEntityValueName, updatedAddress),
                ACL.readWriteOther().toJsonString(),
                1,
                tr);

        tr.Run();
        deleteAllDefaultEntities(2);
    }

    @Test
    public void testGetSingleton() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        createDefaultAddressEntity(ACL.Access.None);

        _wrapper.getEntityService().getSingleton(
                _defaultEntityType,
                tr);

        tr.Run();
        deleteAllDefaultEntities(1);
    }

    @Test
    public void testGetList() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        createDefaultAddressEntity(ACL.Access.None);
        createDefaultAddressEntity(ACL.Access.None);

        _wrapper.getEntityService().getList(
                Helpers.createJsonPair("entityType", _defaultEntityType),
                "",
                1000,
                tr);

        tr.Run();
        deleteAllDefaultEntities(1);
    }

    @Test
    public void testGetListCount() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        createDefaultAddressEntity(ACL.Access.None);
        createDefaultAddressEntity(ACL.Access.None);

        _wrapper.getEntityService().getListCount(
                Helpers.createJsonPair("entityType", _defaultEntityType),
                tr);

        tr.Run();
        deleteAllDefaultEntities(1);
    }

    @Test
    public void testGetPage() throws Exception {
        createDefaultAddressEntity(ACL.Access.ReadWrite);

        TestResult tr = new TestResult(_wrapper);
        JSONObject context = new JSONObject();
        JSONObject pagination = new JSONObject();
        pagination.put("rowsPerPage", 50);
        pagination.put("pageNumber", 1);
        context.put("pagination", pagination);
        JSONObject searchCriteria = new JSONObject();
        searchCriteria.put("entityType", _defaultEntityType);
        context.put("searchCriteria", searchCriteria);

        _wrapper.getEntityService().getPage(context.toString(), tr);
        tr.Run();

        deleteAllDefaultEntities();
    }

    @Test
    public void testGetPageOffset() throws Exception {
        createDefaultAddressEntity(ACL.Access.ReadWrite);

        TestResult tr = new TestResult(_wrapper);
        JSONObject context = new JSONObject();
        JSONObject pagination = new JSONObject();
        pagination.put("rowsPerPage", 50);
        pagination.put("pageNumber", 1);
        context.put("pagination", pagination);
        JSONObject searchCriteria = new JSONObject();
        searchCriteria.put("entityType", _defaultEntityType);
        context.put("searchCriteria", searchCriteria);

        _wrapper.getEntityService().getPage(context.toString(), tr);
        tr.Run();

        String retCtx = tr.m_response.getJSONObject("data").getString("context");

        tr.Reset();
        _wrapper.getEntityService().getPageOffset(retCtx, 1, tr);
        tr.Run();

        deleteAllDefaultEntities();
    }

    @Test
    public void testIncrementUserEntityData() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEntityService().createEntity(
                _defaultEntityType,
                Helpers.createJsonPair("test", 1234),
                "",
                tr);
        tr.Run();
        String entityId = getEntityId(tr.m_response);

        _wrapper.getEntityService().incrementUserEntityData(
                entityId,
                Helpers.createJsonPair("test", 1234),
                tr);
        tr.Run();

        _wrapper.getEntityService().deleteEntity(entityId, -1, tr);
        tr.Run();
    }


    @Test
    public void testIncrementSharedUserEntityData() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEntityService().createEntity(
                _defaultEntityType,
                Helpers.createJsonPair("test", 1234),
                "",
                tr);
        tr.Run();
        String entityId = getEntityId(tr.m_response);
        //String profileId = getProfileId(tr.m_response);

        _wrapper.getEntityService().incrementSharedUserEntityData(
                entityId,
                "Invalid_Id",
                Helpers.createJsonPair("test", 1234),
                tr);
        tr.RunExpectFail(400, ReasonCodes.MISSING_RECORD);
    }


    @Test
    public void testGetSharedEntitiesListForProfileId() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        createDefaultAddressEntity(ACL.Access.None);
        createDefaultAddressEntity(ACL.Access.None);

        _wrapper.getEntityService().getSharedEntitiesListForProfileId(
                getUser(Users.UserA).profileId,
                Helpers.createJsonPair("entityType", _defaultEntityType),
                "",
                1000,
                tr);

        tr.Run();
        deleteAllDefaultEntities(1);
    }

    // private methods below


    /// <summary>
    /// Returns the entityId from a raw json response
    /// </summary>
    /// <param name="json"> Json to parse for ID </param>
    /// <returns> entityId from data </returns>
    private String getEntityId(JSONObject json) {
        try {
            return json.getJSONObject("data").getString("entityId");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return "";
    }

    /// <summary>
    /// Returns the profileId from a raw json response
    /// </summary>
    /// <param name="json"> Json to parse for ID </param>
    /// <returns> entityId from data </returns>
    private String getProfileId(JSONObject json) {
        try {
            return json.getJSONObject("data").getString("profileId");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return "";
    }

    /// <summary>
    /// Creates a default entity on the server
    /// </summary>
    /// <param name="accessLevel"> accessLevel for entity </param>
    /// <returns> The ID of the entity </returns>
    private String createDefaultAddressEntity(ACL.Access accessLevel) {
        TestResult tr = new TestResult(_wrapper);

        ACL access = new ACL();
        access.setOther(accessLevel);
        String entityId = "";

        //Create entity
        _wrapper.getEntityService().createEntity(
                _defaultEntityType,
                Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue),
                access.toJsonString(),
                tr);

        if (tr.Run()) {
            entityId = getEntityId(tr.m_response);
        }

        return entityId;
    }

    private void deleteAllDefaultEntities() {
        deleteAllDefaultEntities(1);
    }

    /// <summary>
    /// Deletes all default entities
    /// </summary>
    private void deleteAllDefaultEntities(int version) {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> entityIds = new ArrayList<>(0);

        //get all entities
        _wrapper.getEntityService().getEntitiesByType(_defaultEntityType, tr);

        if (tr.Run()) {
            try {
                JSONArray entities = tr.m_response.getJSONObject("data").getJSONArray("entities");
                if (entities.length() <= 0) {
                    return;
                }

                for (int i = 0, ilen = entities.length(); i < ilen; ++i) {
                    entityIds.add(entities.getJSONObject(i).getString("entityId"));
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        while (!entityIds.isEmpty()) {
            tr.Reset();
            _wrapper.getEntityService().deleteEntity(entityIds.remove(0), version, tr);
            tr.Run();
        }
    }
}