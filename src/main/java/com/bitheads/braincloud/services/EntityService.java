package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class EntityService {

    private enum Parameter {
        entityId,
        entityType,
        acl,
        context,
        pageOffset,
        maxReturn,
        where,
        orderBy,
        targetPlayerId,
        version,
        data
    }

    private BrainCloudClient _client;

    public EntityService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Method creates a new entity on the server.
     *
     * @param entityType The entity type as defined by the user
     * @param jsonEntityData The entity's data as a json String
     * @param jsonEntityAcl The entity's access control list as json. A null acl implies
     *            default permissions which make the entity readable/writeable
     *            by only the player.
     * @param callback Callback.
     */
    public void createEntity(String entityType, String jsonEntityData,
                             String jsonEntityAcl, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl;
                jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.CREATE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method deletes the given entity on the server.
     *
     * @param entityId The id of the entity to update
     * @param version Current version of the entity. If the version of the
     *            entity on the server does not match the version passed in, the
     *            server operation will fail. Use -1 to skip version checking.
     * @param callback Callback.
     */
    public void deleteEntity(String entityId, int version, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.DELETE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method deletes the given singleton on the server. Note that if there are
     * multiple entities with the same entityType, this call will only delete
     * the first one found.
     *
     * @param entityType The entity type as defined by the user
     * @param version Current version of the entity. If the version of the
     *            entity on the server does not match the version passed in, the
     *            server operation will fail. Use -1 to skip version checking.
     * @param callback The callback handler
     */
    public void deleteSingleton(String entityType, int version, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.DELETE_SINGLETON, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method returns all player entities that match the given type.
     *
     * @param entityType The entity type to search for
     * @param callback The callback
     */
    public void getEntitiesByType(String entityType, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ_BY_TYPE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get a specific entity.
     *
     * @param entityId The id of the entity
     * @param callback The callback handler
     */
    public void getEntity(String entityId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method retrieves a singleton entity on the server. If the entity doesn't exist, null is returned.
     *
     * @param entityType The entity type as defined by the user
     * @param callback The callback handler
     */
    public void getSingleton(String entityType, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method returns a shared entity for the given profile and entity ID.
     * An entity is shared if its ACL allows for the currently logged
     * in user to read the data.
     *
     * Service Name - Entity
     * Service Operation - READ_SHARED_ENTITY
     *
     * @param profileId The the profile ID of the player who owns the entity
     * @param entityId The ID of the entity that will be retrieved
     * @param callback The method to be invoked when the server response is received
     */
    public void getSharedEntityForProfileId(String profileId, String entityId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.targetPlayerId.name(), profileId);
            data.put(Parameter.entityId.name(), entityId);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ_SHARED_ENTITY, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method returns all shared entities for the given profile id.
     * An entity is shared if its ACL allows for the currently logged
     * in user to read the data.
     *
     * Service Name - Entity
     * Service Operation - ReadShared
     *
     * @param profileId The profile id to retrieve shared entities for
     * @param callback The method to be invoked when the server response is received
     */
    public void getSharedEntitiesForProfileId(String profileId,
                                              IServerCallback callback) {

        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.targetPlayerId.name(), profileId);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ_SHARED, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method gets list of shared entities for the specified profile based on type and/or where clause
     *
     * Service Name - Entity
     * Service Operation - READ_SHARED_ENTITIES_LIST
     *
     * @param profileId The profile ID to retrieve shared entities for
     * @param whereJson Mongo style query
     * @param orderByJson Sort order
     * @param maxReturn The maximum number of entities to return
     * @param callback The method to be invoked when the server response is received
     */
    public void getSharedEntitiesListForProfileId(String profileId, String whereJson, String orderByJson, int maxReturn,
                                                  IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.targetPlayerId.name(), profileId);
            if (StringUtil.IsOptionalParameterValid(whereJson)) {
                data.put(Parameter.where.name(), new JSONObject(whereJson));
            }
            if (StringUtil.IsOptionalParameterValid(orderByJson)) {
                data.put(Parameter.orderBy.name(), new JSONObject(orderByJson));
            }
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.READ_SHARED_ENTITIES_LIST, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Method updates a new entity on the server. This operation results in the
     * entity data being completely replaced by the passed in JSON String.
     *
     * @param entityId The id of the entity to update
     * @param entityType The entity type as defined by the user
     * @param jsonEntityData The entity's data as a json String.
     * @param jsonEntityAcl The entity's access control list as json. A null acl implies
     *            default permissions which make the entity readable/writeable
     *            by only the player.
     * @param version Current version of the entity. If the version of the
     *            entity on the server does not match the version passed in, the
     *            server operation will fail. Use -1 to skip version checking.
     * @param callback Callback.
     */
    public void updateEntity(String entityId, String entityType,
                             String jsonEntityData, String jsonEntityAcl,
                             int version, IServerCallback callback) {

        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(jsonEntityAcl)) {
                JSONObject jsonAcl = new JSONObject(jsonEntityAcl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.UPDATE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method updates a shared entity owned by another user. This operation results in the entity
     * data being completely replaced by the passed in JSON string.
     *
     * Service Name - Entity
     * Service Operation - UpdateShared
     *
     * @param entityId The id of the entity to update
     * @param targetProfileId The id of the profile who owns the shared entity
     * @param entityType The entity type as defined by the user
     * @param jsonEntityData    The entity's data as a json string.
     * @param callback The method to be invoked when the server response is received
     */
    public void updateSharedEntity(String targetProfileId, String entityId,
                                   String entityType, String jsonEntityData, int version,
                                   IServerCallback callback) {

        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.targetPlayerId.name(), targetProfileId);
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.UPDATE_SHARED, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method updates a singleton entity on the server. This operation results
     * in the entity data being completely replaced by the passed in JSON
     * string. If the entity doesn't exist it is created.
     *
     * @param entityType The entity type as defined by the user
     * @param jsonEntityData The entity's data as a json string
     * @param jsonAclData The entity's access control list as json. A null acl implies default
     *            permissions which make the entity readable/writeable by only the player.
     * @param version Current version of the entity. If the version of the
     *            entity on the server does not match the version passed in, the
     *            server operation will fail. Use -1 to skip version checking.
     * @param callback The callback handler
     */
    public void updateSingleton(String entityType, String jsonEntityData,
                                String jsonAclData, int version, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(jsonEntityData);
            data.put(Parameter.data.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(jsonAclData)) {
                JSONObject jsonAcl = new JSONObject(jsonAclData);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.UPDATE_SINGLETON, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method gets list of entities from the server base on type and/or where clause
     *
     * Service Name - Entity
     * Service Operation - GET_LIST
     *
     * @param whereJson Mongo style query string
     * @param orderByJson Sort order
     * @param maxReturn The maximum number of entities to return
     * @param callback The callback object
     */
    public void getList(String whereJson, String orderByJson, int maxReturn,
                        IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(whereJson)) {
                data.put(Parameter.where.name(), new JSONObject(whereJson));
            }
            if (StringUtil.IsOptionalParameterValid(orderByJson)) {
                data.put(Parameter.orderBy.name(), new JSONObject(orderByJson));
            }
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.GET_LIST, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Method gets a count of entities based on the where clause
     *
     * Service Name - Entity
     * Service Operation - GET_LIST_COUNT
     *
     * @param whereJson Mongo style query string
     * @param callback The callback object
     */
    public void getListCount(String whereJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.where.name(), new JSONObject(whereJson));

            ServerCall serverCall = new ServerCall(ServiceName.entity,
                    ServiceOperation.GET_LIST_COUNT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Method uses a paging system to iterate through user entities
     * After retrieving a page of entities with this method,
     * use GetPageOffset() to retrieve previous or next pages.
     *
     * Service Name - Entity
     * Service Operation - GET_PAGE
     *
     * @param jsonContext The json context for the page request.        See the portal appendix documentation for format.
     * @param callback The callback object
     */
    public void getPage(String jsonContext, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            JSONObject context = new JSONObject(jsonContext);
            data.put(Parameter.context.name(), context);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.GET_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to retrieve previous or next pages after having called the GetPage method.
     *
     * Service Name - Entity
     * Service Operation - GET_PAGE_BY_OFFSET
     *
     * @param context The context string returned from the server from a
     *      previous call to GetPage or GetPageOffset
     * @param pageOffset The positive or negative page offset to fetch. Uses the last page
     *      retrieved using the context string to determine a starting point.
     * @param callback The callback object
     */
    public void getPageOffset(String context, int pageOffset, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.GET_PAGE_BY_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Partial increment of entity data field items. Partial set of items incremented as specified.
     *
     * Service Name - entity
     * Service Operation - INCREMENT_USER_ENTITY_DATA
     *
     * @param entityId The id of the entity to update
     * @param jsonData The entity's data object
     * @param callback The callback object
     */
    public void incrementUserEntityData(String entityId, String jsonData, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.INCREMENT_USER_ENTITY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Partial increment of entity data field items. Partial set of items incremented as specified.
     *
     * Service Name - entity
     * Service Operation - INCREMENT_USER_ENTITY_DATA
     *
     * @param entityId The id of the entity to update
     * @param targetProfileId Profile ID of the entity owner
     * @param jsonData The entity's data object
     * @param callback The callback object
     */
    public void incrementSharedUserEntityData(String entityId, String targetProfileId, String jsonData, IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.targetPlayerId.name(), targetProfileId);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.entity,
                    ServiceOperation.INCREMENT_SHARED_USER_ENTITY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
