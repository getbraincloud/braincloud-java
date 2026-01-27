// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomEntityService {

    private enum Parameter {
        entityId,
        dataJson,
        acl,
        timeToLive,
        isOwned,
        entityType,
        version,
        deleteCriteria,
        whereJson,
        maxReturn,
        rowsPerPage,
        searchJson,
        sortJson,
        doCount,
        pageOffset,
        context,
        fieldsJson,
        shardKeyJson
    }

    private BrainCloudClient _client;

    public CustomEntityService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Creates new custom entity.
     *
     * Service Name - customEntity
     * Service Operation - CREATE_ENTITY
     *
     * @param entityType     The entity type as defined by the user
     * @param jsonEntityData The entity's data as a json string
     * @param jsonEntityAcl  The entity's access control list as json. A null acl
     *                       implies default
     *                       permissions which make the entity readable/writeable by
     *                       only the user.
     * @param timeToLive     The duration of time, in milliseconds, the singleton
     *                       custom entity should live
     *                       before being expired. Null indicates never expires.
     *                       Value of -1 indicates no change for updates.
     * @param isOwned
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void createEntity(String entityType, String dataJson,
            String acl, long timeToLive, Boolean isOwned, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            JSONObject jsonData = new JSONObject(dataJson);
            data.put(Parameter.dataJson.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(acl)) {
                JSONObject jsonAcl;
                jsonAcl = new JSONObject(acl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            data.put(Parameter.timeToLive.name(), timeToLive);
            data.put(Parameter.isOwned.name(), isOwned);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.CREATE_ENTITY, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified custom entity on the server.
     *
     * Service Name - customEntity
     * Service Operation - DELETE_ENTITY
     *
     * @param entityType     The entity type as defined by the user
     * @param jsonEntityData The entity's data as a json string
     * @param version        Version of the custom entity being updated.
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void deleteEntity(String entityType, String entityId,
            int version, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.DELETE_ENTITY, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the number of custom entities meeting the specified where clause,
     * enforcing ownership/ACL permissions
     *
     * Service Name - customEntity
     * Service Operation - GET_COUNT
     *
     * @param entityType The entity type as defined by the user
     * @param whereJson  Mongo style query string
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void getCount(String entityType, String whereJson,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            JSONObject whereData = new JSONObject(whereJson);
            data.put(Parameter.whereJson.name(), whereData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_COUNT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of up to maxReturn randomly selected custom entities from the
     * server based on the entity type and where condition.
     *
     * Service Name - customEntity
     * Service Operation - GET_RANDOM_ENTITIES_MATCHING
     *
     * @param entityType The entity type as defined by the user
     * @param whereJson  Mongo style query string
     * @param maxReturn  Max number of returns
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void getRandomEntitiesMatching(String entityType, String whereJson, int maxReturn,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            JSONObject whereData = new JSONObject(whereJson);
            data.put(Parameter.whereJson.name(), whereData);
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_RANDOM_ENTITIES_MATCHING, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method uses a paging system to iterate through Custom Entities
     * After retrieving a page of Custom Entities with this method,
     * use GetEntityPageOffset() to retrieve previous or next pages.
     *
     * Service Name - customEntity
     * Service Operation - GET_ENTITY_PAGE
     *
     * @param entityType The entity type as defined by the user
     * @param context    The json context for the page request.
     *                   See the portal appendix documentation for format.
     * @param callback   The callback object
     */
    public void getEntityPage(String entityType, String context,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            JSONObject jsonContext = new JSONObject(context);
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.context.name(), jsonContext);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_ENTITY_PAGE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the page of custom entities from the server based on the encoded context
     * and specified page offset.
     *
     * Service Name - customEntity
     * Service Operation - GET_ENTITY_PAGE_OFFSET
     *
     * @param entityType The entity type as defined by the user
     * @param context    The context string returned from the server from a previous
     *                   call to GetPage or GetPageOffset.
     * @param pageOffset The positive or negative page offset to fetch. Uses the
     *                   last page retrieved using the context string to determine a
     *                   starting point.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void getEntityPageOffset(String entityType, String context, int pageOffset,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_ENTITY_PAGE_OFFSET, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the specified custom entity from the server.
     *
     * Service Name - customEntity
     * Service Operation - READ_ENTITY
     *
     * @param entityType The entity type as defined by the user
     * @param entityId   The entity id as defined by the system
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void readEntity(String entityType, String entityId,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.READ_ENTITY, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces the specified custom entity's data, and optionally updates the acl
     * and expiry, on the server.
     *
     * Service Name - customEntity
     * Service Operation - UPDATE_ENTITY
     *
     * @param entityType     The entity type as defined by the user
     * @param entityId       The id of custom entity being updated.
     * @param version        Version of the custom entity being updated.
     * @param jsonEntityData The entity's data as a json string
     * @param jsonEntityAcl  The entity's access control list as json. A null acl
     *                       implies default
     *                       permissions which make the entity readable/writeable by
     *                       only the user.
     * @param timeToLive     The duration of time, in milliseconds, the singleton
     *                       custom entity should live
     *                       before being expired. Null indicates never expires.
     *                       Value of -1 indicates no change for updates.
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void updateEntity(String entityType, String entityId, int version, String dataJson, String acl,
            long timeToLive,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(dataJson);
            data.put(Parameter.dataJson.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(acl)) {
                JSONObject jsonAcl;
                jsonAcl = new JSONObject(acl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            data.put(Parameter.timeToLive.name(), timeToLive);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.UPDATE_ENTITY, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces the specified custom entity's data, and optionally updates the acl
     * and expiry, on the server.
     *
     * Service Name - customEntity
     * Service Operation - UPDATE_ENTITY_FIELDS
     *
     * @param entityType The entity type as defined by the user
     * @param entityId   The id of custom entity being updated.
     * @param version    Version of the custom entity being updated.
     * @param fieldsJson Specific fields, as JSON, to set within entity's custom
     *                   data.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void updateEntityFields(String entityType, String entityId, int version, String fieldsJson,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.UPDATE_ENTITY_FIELDS, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * For sharded custom collection entities. Sets the specified fields within
     * custom entity data on the server, enforcing ownership/ACL permissions.
     *
     * Service Name - customEntity
     * Service Operation - UPDATE_ENTITY_FIELDS_SHARDED
     *
     * @param entityType   The entity type as defined by the user
     * @param entityId     The id of custom entity being updated.
     * @param version      Version of the custom entity being updated.
     * @param fieldsJson   Specific fields, as JSON, to set within entity's custom
     *                     data.
     * @param shardKeyJson The shard key field(s) and value(s), as JSON, applicable
     *                     to the entity being updated.
     * @param callback     The method to be invoked when the server response is
     *                     received
     */
    public void updateEntityFieldsSharded(String entityType, String entityId, int version, String fieldsJson,
            String shardKeyJson, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            JSONObject shardKeyData = new JSONObject(shardKeyJson);
            data.put(Parameter.shardKeyJson.name(), shardKeyData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.UPDATE_ENTITY_FIELDS_SHARDED, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes entities based on the delete criteria.
     *
     * Service Name - customEntity
     * Service Operation - DELETE_ENTITIES
     *
     * @param entityType     The entity type as defined by the user
     * @param deleteCriteria Json string of criteria wanted for deletion
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void deleteEntities(String entityType, String deleteCriteria,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            JSONObject Data = new JSONObject(deleteCriteria);
            data.put(Parameter.deleteCriteria.name(), Data);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.DELETE_ENTITIES, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified custom entity singleton, owned by the session's user,
     * for the specified entity type, on the server.
     *
     * Service Name - customEntity
     * Service Operation - DELETE_SINGLETON
     *
     * @param entityType The entity type as defined by the user
     * @param version    Version of the singleton being deleted.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void deleteSingleton(String entityType, int version,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            // JSONObject Data = new JSONObject(version);
            data.put(Parameter.version.name(), version);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.DELETE_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the singleton owned by the user for the specified custom entity type
     * on the server,
     * creating the singleton if it does not exist.
     * This operation results in the owned singleton's data being completely
     * replaced by the passed in JSON object.
     *
     * Service Name - customEntity
     * Service Operation - UPDATE_SINGLETON
     *
     * @param entityType The entity type as defined by the user
     * @param version    Version of the singleton being updated.
     * @param dataJson   The full data for the singleton as a json string
     * @param acl        The singleton entity's Access Control List as an object.
     *                   A null ACL implies default permissions which make the
     *                   entity readable by others.
     * @param timeToLive The duration of time, in milliseconds, the singleton custom
     *                   entity should live
     *                   before being expired. Null indicates never expires. Value
     *                   of -1 indicates no change for updates.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void updateSingleton(String entityType, int version, String dataJson, String acl, long timeToLive,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            // JSONObject data = new JSONObject();
            data.put(Parameter.version.name(), version);

            JSONObject jsonData = new JSONObject(dataJson);
            data.put(Parameter.dataJson.name(), jsonData);

            if (StringUtil.IsOptionalParameterValid(acl)) {
                JSONObject jsonAcl;
                jsonAcl = new JSONObject(acl);
                data.put(Parameter.acl.name(), jsonAcl);
            }

            data.put(Parameter.timeToLive.name(), timeToLive);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.UPDATE_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Partially updates the data, of the singleton owned by the user for the
     * specified custom entity type,
     * with the specified fields, on the server
     *
     * Service Name - customEntity
     * Service Operation - UPDATE_SINGLETON_FIELDS
     *
     * @param entityType The entity type as defined by the user
     * @param version    Version of the singleton being updated.
     * @param fieldsJson Specific fields, as JSON, within entity's custom data to be
     *                   updated.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void updateSingletonFields(String entityType, int version, String fieldsJson,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.version.name(), version);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.UPDATE_SINGLETON_FIELDS, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increments fields on the specified custom entity owned by the user on the
     * server.
     *
     * Service Name - customEntity
     * Service Operation - INCREMENT_DATA
     *
     * @param entityType The entity type as defined by the user
     * @param entityId   The entity id as defined by the system
     * @param fieldsJson Specific fields, as JSON, within entity's custom data, with
     *                   respective increment amount.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void incrementData(String entityType, String entityId, String fieldsJson,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.entityId.name(), entityId);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.INCREMENT_DATA, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the custom entity singleton owned by the session's user.
     *
     * Service Name - customEntity
     * Service Operation - READ_SINGLETON
     *
     * @param entityType The entity type as defined by the user
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void readSingleton(String entityType,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.READ_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increments the specified fields, of the singleton owned by the user, by the
     * specified amount within the custom entity data on the server.
     *
     * Service Name - customEntity
     * Service Operation - INCREMENT_SINGLETON_DATA
     *
     * @param entityType The type of custom entity being updated.
     * @param fieldsJson Specific fields, as JSON, within entity's custom data, with
     *                   respective increment amount.
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void incrementSingletonData(String entityType, String fieldsJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.INCREMENT_SINGLETON_DATA, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
