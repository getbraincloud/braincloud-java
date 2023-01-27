package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalEntityService {
    private BrainCloudClient _client;

    public GlobalEntityService(BrainCloudClient client) {
        _client = client;
    }

    private enum Parameter {
        entityId,
        entityType,
        entityIndexedId,
        data,
        acl,
        version,
        maxReturn,
        where,
        orderBy,
        timeToLive,
        context,
        pageOffset,
        ownerId,
    }

    /**
     * Method creates a new entity on the server.
     *
     * @param entityType The entity type as defined by the user
     * @param timeToLive Sets expiry time in millis for entity if > 0
     * @param jsonEntityAcl The entity's access control list as json. A null acl implies default
     * @param jsonEntityData The entity's data as a json String
     * @param callback Callback.
     */
    public void createEntity(String entityType, long timeToLive,
                             String jsonEntityAcl, String jsonEntityData,
                             IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.timeToLive.name(), timeToLive);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.CREATE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method creates a new entity on the server with an indexed id.
     *
     * @param entityType The entity type as defined by the user
     * @param indexedId A secondary ID that will be indexed
     * @param timeToLive Sets expiry time in millis for entity if > 0
     * @param jsonEntityAcl The entity's access control list as json. A null acl implies default
     * @param jsonEntityData The entity's data as a json String
     * @param callback Callback.
     */
    public void createEntityWithIndexedId(String entityType,
                                          String indexedId, long timeToLive, String jsonEntityAcl,
                                          String jsonEntityData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityIndexedId.name(), indexedId);
            data.put(Parameter.timeToLive.name(), timeToLive);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.CREATE_WITH_INDEXED_ID, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates an existing entity on the server.
     *
     * @param entityId The entity ID
     * @param version Current version of the entity. If the version of the entity on
     *            the server does not match the version passed in, the server
     *            operation will fail. Use -1 to skip version checking.
     * @param jsonEntityData The entity's data as a json String
     * @param callback Callback.
     */
    public void updateEntity(String entityId, int version,
                             String jsonEntityData, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.UPDATE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates an existing entity's Acl on the server.
     *
     * @param entityId The entity ID
     * @param version Current version of the entity. If the version of the entity on
     *            the server does not match the version passed in, the server
     *            operation will fail. Use -1 to skip version checking.
     * @param jsonEntityAcl The entity's access control list as json.
     * @param callback Callback.
     */
    public void updateEntityAcl(String entityId, int version,
                                String jsonEntityAcl, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.UPDATE_ACL, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates an existing entity's time to live on the server.
     *
     * @param entityId The entity id
     * @param version Current version of the entity. If the version of the entity on
     *            the server does not match the version passed in, the server
     *            operation will fail. Use -1 to skip version checking.
     * @param timeToLive Sets expiry time in millis for entity if > 0
     * @param callback Callback
     */
    public void updateEntityTimeToLive(String entityId, int version,
                                       long timeToLive, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.timeToLive.name(), timeToLive);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.UPDATE_TIME_TO_LIVE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method deletes an existing entity on the server.
     *
     * @param entityId The entity ID
     * @param version The version of the entity to delete
     * @param callback Callback.
     */
    public void deleteEntity(String entityId, int version,
                             IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.DELETE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method reads an existing entity from the server.
     *
     * @param entityId The entity ID
     * @param callback Callback.
     */
    public void readEntity(String entityId,
                           IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.READ, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Method gets list of entities from the server base on type and/or where
     * clause
     *
     * @param where Mongo style query string
     * @param orderBy Specifies the order in which the query returns matching
     *            documents. The sort parameter consists of a field followed by
     *            an ascending(1)/descending flag(-1). eg. { name : 1} sorts by
     *            name in ascending order
     * @param maxReturn The maximum number of entities to return
     * @param callback Callback.
     */
    public void getList(String where, String orderBy, int maxReturn,
                        IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(where)) {
                JSONObject whereObj = new JSONObject(where);
                data.put(Parameter.where.name(), whereObj);
            }
            if (StringUtil.IsOptionalParameterValid(orderBy)) {
                JSONObject orderByObj = new JSONObject(orderBy);
                data.put(Parameter.orderBy.name(), orderByObj);
            }
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_LIST, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method gets list of entities from the server base on indexed id
     *
     * @param entityIndexedId The entity indexed Id
     * @param maxReturn The maximum number of entities to return
     * @param callback Callback.
     */
    public void getListByIndexedId(String entityIndexedId, int maxReturn,
                                   IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityIndexedId.name(), entityIndexedId);
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_LIST_BY_INDEXED_ID, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method gets a count of entities based on the where clause
     *
     * @param where Mongo style query string
     * @param callback Callback.
     */
    public void getListCount(String where,
                             IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(where)) {
                JSONObject whereObj = new JSONObject(where);
                data.put(Parameter.where.name(), whereObj);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_LIST_COUNT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method uses a paging system to iterate through Global Entities.
     * After retrieving a page of Global Entities with this method,
     * use GetPageOffset() to retrieve previous or next pages.
     *
     * Service Name - GlobalEntity
     * Service Operation - GetPage
     *
     * @param jsonContext The json context for the page request.
     * See the portal appendix documentation for format
     * @param callback Callback.
     */
    public void getPage(String jsonContext, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONObject context = new JSONObject(jsonContext);
            data.put(Parameter.context.name(), context);
            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_PAGE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }


    /**
     * Method to retrieve previous or next pages after having called
     * the GetPage method.
     *
     * Service Name - GlobalEntity
     * Service Operation - GetPageOffset
     *
     * @param context The context string returned from the server from a previous call
     * @param pageOffset The positive or negative page offset to fetch. Uses the last page
     * retrieved using the context string to determine a starting point.
     * @param callback Callback.
     */
    public void getPageOffset(String context, int pageOffset, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);
            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_PAGE_BY_OFFSET, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Partial increment of entity data field items. Partial set of items incremented as specified.
     *
     * Service Name - globalEntity
     * Service Operation - INCREMENT_GLOBAL_ENTITY_DATA
     *
     * @param entityId The id of the entity to update
     * @param jsonData The entity's data object
     * @param callback The callback object
     */
    public void incrementGlobalEntityData(String entityId, String jsonData, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.INCREMENT_GLOBAL_ENTITY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of up to randomCount randomly selected
     entities from the server based on the where condition and
     specified maximum return count.
     *
     * Service Name - globalEntity
     * Service Operation - GET_RANDOM_ENTITIES_MATCHING
     *
     * @param where Mongo style query string
     * @param maxReturn The maximum number of entities to return
     * @param callback The callback object
     */
    public void getRandomEntitiesMatching(String where, int maxReturn, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (StringUtil.IsOptionalParameterValid(where)) { JSONObject whereObj = new JSONObject(where); data.put(Parameter.where.name(), whereObj);
            }
            data.put(Parameter.maxReturn.name(), maxReturn);
            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.GET_RANDOM_ENTITIES_MATCHING, data, callback); _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates an existing entity's Indexed ID.
     *
     * Service Name - globalEntity
     * Service Operation - UPDATE_INDEXED_ID
     *
     * @param entityId The entity ID
     * @param version The version of the entity to update
     * @param entityIndexedId the id index of the entity
     * @param callback The callback object
     */
    public void updateEntityIndexedId(String entityId, int version, String entityIndexedId, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.entityIndexedId.name(), entityIndexedId);

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.UPDATE_INDEXED_ID, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates an existing entity's Owner and ACL on the server.
     *
     * Service Name - globalEntity
     * Service Operation - UPDATE_ENTITY_OWNER_AND_ACL
     *
     * @param entityId The entity ID
     * @param version The version of the entity to update
     * @param ownerId The owner ID
     * @param jsonEntityAcl The entity's access control list as JSON.
     * @param callback The callback object
     */
    public void updateEntityOwnerAndAcl(String entityId, int version, String ownerId, String jsonEntityAcl, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.ownerId.name(), ownerId);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.UPDATE_ENTITY_OWNER_AND_ACL, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method clears the owner id of an existing entity and sets the ACL on the server.
     *
     * Service Name - globalEntity
     * Service Operation - MAKE_SYSTEM_ENTITY
     *
     * @param entityId The entity ID
     * @param version The version of the entity to update
     * @param jsonEntityAcl The entity's access control list as JSON.
     * @param callback The callback object
     */
    public void makeSystemEntity(String entityId, int version, String jsonEntityAcl, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.globalEntity,
                    ServiceOperation.MAKE_SYSTEM_ENTITY, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }
}
