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
     * Method creates a new entity on the server.
     *
     * @param entityType    The entity type as defined by the user
     * @param dataJson      The entity's data as a json String
     * @param acl           The entity's access control list as json. A null acl implies
     *                      default permissions which make the entity readable/writeable
     *                      by only the player.
     * @param timeToLive    The duration of time, in milliseconds, the custom entity should live before being expired. 0 indicates never expires.
     * @param isOwned       Boolean to indicate whether the current user should be recorded as the owner of the object. If owned, the object will 
     *                      be automatically deleted if/when the user is deleted.
     * @param callback      Callback.
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
     * Deletes the specified custom entity on the server, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of the entity to delete
     * @param version       The version of the entity to delete. Use -1 to indicate the newest version
     * @param callback      Callback.
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
     * Counts the number of custom entities meeting the specified where clause, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param whereJson     The where clause, as JSON object.
     * @param callback      Callback.
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
     * Gets a list of up to maxReturn randomly selected custom entities from the server based on the entity type and where condition.
     * 
     * @param entityType The entity type as defined by the user
     * @param whereJson mongo style query string
     * @param maxReturn max number of returns
     * @param callback Callback.
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
     * Retrieves first page of custom entities from the server based on the custom entity type and specified query context, enforcing ownership/ACL permissions.
     *
     * @param entityType The entity type as defined by the user
     * @param rowsPerPage Quantity of rows per page
     * @param searchJson Data to look for
     * @param sortJson Data to sort by
     * @param doCount For collections with more than 1,000 records, it is recommended that doCount be set to false for better performance.
     * @param callback Callback.
     * @deprecated Use getEntityPage instead - removal after October 26 2021
     */
    @Deprecated
    public void getPage(String entityType, int rowsPerPage, String searchJson, String sortJson, Boolean doCount,
                         IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.rowsPerPage.name(), rowsPerPage);
            JSONObject searchData = new JSONObject(searchJson);
            data.put(Parameter.searchJson.name(), searchData);
            JSONObject sortData = new JSONObject(sortJson);
            data.put(Parameter.sortJson.name(), sortData);
            data.put(Parameter.doCount.name(), doCount);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_PAGE, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves first page of custom entities from the server based on the custom entity type and specified query context, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param context       A context object describing the desired paging behaviour
     * @param callback      Callback.
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
     * Gets the page of custom entities from the server based on the encoded context and specified page offset, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param context       The context string returned from the server from a previous call to GetPage or GetPageOffse
     * @param pageOffset    The positive or negative page offset to fetch. Uses the last page retrieved using the context string to determine a starting point.
     * @param callback      Callback.
     *
     * @deprecated Use getEntityPageOffset instead - removal after October 26 2021
     */
    @Deprecated
    public void getPageOffset(String entityType, String context, int pageOffset,
                         IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.GET_PAGE_BY_OFFSET, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        /**
     * Gets the page of custom entities from the server based on the encoded context and specified page offset, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param context       The context string returned from the server from a previous call to GetPage or GetPageOffset.
     * @param pageOffset    The positive or negative page offset to fetch. Uses the last page retrieved using the context string to determine a starting point.
     * @param callback      Callback.
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
     * Reads a custom entity, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of custom entity being read.
     * @param callback      Callback.
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
     * Replaces the specified custom entity's data, and optionally updates the acl and expiry, on the server, enforcing current ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of custom entity being updated.
     * @param version       Version of the custom entity being updated.
     * @param dataJson      New custom data, as JSON, to replace existing custom data.
     * @param acl           New access control list settings to replace existing acl. Optional, ignored if null.
     * @param timeToLive    The duration of time, in milliseconds, the custom entity should live from now before being expired. Null indicates never expires. 
                            To indicate no change, use -1.
     * @param callback      Callback.
     */
    public void updateEntity(String entityType, String entityId, int version, String dataJson, String acl, long timeToLive,
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
     * Replaces the specified custom entity's data, and optionally updates the acl and expiry, on the server, enforcing current ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of custom entity being updated.
     * @param version       Version of the custom entity being updated.
     * @param fieldsJson    Specific fields, as JSON, to set within entity's custom data.
     * @param callback      Callback.
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
     * Replaces the specified custom entity's data, and optionally updates the acl and expiry, on the server, enforcing current ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of custom entity being updated.
     * @param version       Version of the custom entity being updated.
     * @param fieldsJson    Specific fields, as JSON, to set within entity's custom data.
     * @param shardKeyJson  The shard key field(s) and value(s), as JSON, applicable to the entity being updated.
     * @param callback      Callback.
     */
    public void updateEntityFieldsSharded(String entityType, String entityId, int version, String fieldsJson, String shardKeyJson, IServerCallback callback) {

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
     * deletes Entities based on the criteria
     *
     * @param entityType        The entity type as defined by the user
     * @param deleteCriteria    The delete criteria to be applied.
     * @param callback          Callback.
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

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified custom entity singleton, owned by the session's user, for the specified entity type, on the server.
     *
     * @param entityType    The entity type as defined by the user
     * @param version       Version of the custom entity singleton being deleted.
     * @param callback      Callback.
     */
    public void deleteSingleton(String entityType, int version,
                         IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            //JSONObject Data = new JSONObject(version);
            data.put(Parameter.version.name(), version);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.DELETE_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the singleton owned by the user for the specified custom entity type on the server, creating the singleton if it does not exist. This operation results in the owned singleton's data being completely replaced by the passed in JSON object.
     *
     * @param entityType    The entity type as defined by the user
     * @param version       Version of the custom entity singleton being updated.
     * @param dataJson      The singleton entity's custom field data, as JSON.
     * @param acl           The singleton entity's Access Control List as an object. A null ACL implies default permissions which make the entity readable by others.
     * @param timeToLive    The duration of time, in milliseconds, the singleton custom entity should live before being expired. Null indicates never expires. Value of -1 indicates no change for updates.
     * @param callback      Callback.
     */
    public void updateSingleton(String entityType, int version, String dataJson, String acl, long timeToLive,
                         IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);
            //JSONObject data = new JSONObject();
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

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Partially updates the data, of the singleton owned by the user for the specified custom entity type, with the specified fields, on the server
     *
     * @param entityType    The entity type as defined by the user
     * @param version       Version of the custom entity singleton being updated.
     * @param fieldsJson    Specific fields, as JSON, to set within singleton entity's custom data.
     * @param callback      Callback.
     */
    public void updateSingletonFields(String entityType, int version,String fieldsJson,
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

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increments the specified fields by the specified amount within custom entity data on the server, enforcing ownership/ACL permissions.
     *
     * @param entityType    The entity type as defined by the user
     * @param entityId      The id of custom entity being updated.
     * @param fieldsJson    Specific fields, as JSON, within entity's custom data, with respective increment amount.
     * @param callback      Callback.
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

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the custom entity singleton owned by the session's user.
     *
     * @param entityType The entity type as defined by the user
     * @param callback Callback.
     */
    public void readSingleton(String entityType,
                         IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.READ_SINGLETON, data, callback);
            _client.sendRequest(serverCall);

        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increments the specified fields of the singleton owned by the user by the specified amount
     * within the custom entity data on the server.
     *
     * @param entityType The type of custom entity being updated.
     * @param fieldsJson Specific fields, as JSON, within entity's custom data with respective
     *                   increment amount.
     * @param callback Callback.
     */
    public void incrementSingletonData(String entityType, String fieldsJson, IServerCallback callback){
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.entityType.name(), entityType);

            JSONObject fieldsData = new JSONObject(fieldsJson);
            data.put(Parameter.fieldsJson.name(), fieldsData);

            ServerCall serverCall = new ServerCall(ServiceName.customEntity,
                    ServiceOperation.INCREMENT_SINGLETON_DATA, data, callback);
            _client.sendRequest(serverCall);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

    