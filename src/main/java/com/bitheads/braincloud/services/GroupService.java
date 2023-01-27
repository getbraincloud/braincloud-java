package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by bradleyh on 5/6/2016.
 */
public class GroupService {

    public enum Role {
        OWNER, ADMIN, MEMBER, OTHER
    }

    public enum AutoJoinStrategy {
        JoinFirstGroup, JoinRandomGroup
    }

    private enum Parameter {
        groupId,
        profileId,
        role,
        attributes,
        name,
        groupType,
        groupTypes,
        isOpenGroup,
        acl,
        data,
        ownerAttributes,
        defaultMemberAttributes,
        isOwnedByGroupMember,
        entityId,
        version,
        context,
        pageOffset,
        autoJoinStrategy,
        where,
        summaryData,
        maxReturn
    }

    private BrainCloudClient _client;

    public GroupService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Accept an outstanding invitation to join the group.
     *
     * Service Name - group
     * Service Operation - ACCEPT_GROUP_INVITATION
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void acceptGroupInvitation(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.ACCEPT_GROUP_INVITATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Add a member to the group.
     *
     * Service Name - group
     * Service Operation - ADD_GROUP_MEMBER
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the member being added.
     * @param role Role of the member being added.
     * @param jsonAttributes Attributes of the member being added.
     * @param callback The method to be invoked when the server response is received
     */
    public void addGroupMember(
            String groupId,
            String profileId,
            Role role,
            String jsonAttributes,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.role.name(), role.name());

            if (StringUtil.IsOptionalParameterValid(jsonAttributes)) {
                JSONObject obj = new JSONObject(jsonAttributes);
                data.put(Parameter.attributes.name(), obj);
            }

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.ADD_GROUP_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Approve an outstanding request to join the group.
     *
     * Service Name - group
     * Service Operation - APPROVE_GROUP_JOIN_REQUEST
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the invitation being deleted.
     * @param role Role of the member being invited.
     * @param jsonAttributes Attributes of the member being invited.
     * @param callback The method to be invoked when the server response is received
     */
    public void approveGroupJoinRequest(
            String groupId,
            String profileId,
            Role role,
            String jsonAttributes,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.role.name(), role.name());

            if (StringUtil.IsOptionalParameterValid(jsonAttributes)) {
                data.put(Parameter.attributes.name(), new JSONObject(jsonAttributes));
            }

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.APPROVE_GROUP_JOIN_REQUEST, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Automatically join an open group that matches the search criteria and has space available.
     *
     * Service Name - group
     * Service Operation - AUTO_JOIN_GROUP
     *
     * @param groupType Name of the associated group type.
     * @param autoJoinStrategy Selection strategy to employ when there are multiple matches
     * @param dataQueryJson Query parameters (optional)
     * @param callback The method to be invoked when the server response is received
     */
    public void autoJoinGroup(String groupType, AutoJoinStrategy autoJoinStrategy, String dataQueryJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupType.name(), groupType);
            data.put(Parameter.autoJoinStrategy.name(), autoJoinStrategy);

            if (StringUtil.IsOptionalParameterValid(dataQueryJson))
                data.put(Parameter.where.name(), dataQueryJson);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.AUTO_JOIN_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     *Find and join an open group in the pool of groups in multiple group types provided as input arguments.     *
     * Service Name - group
     * Service Operation - AUTO_JOIN_GROUP_MULTI
     *
     * @param groupTypes Name of the associated group type.
     * @param autoJoinStrategy Selection strategy to employ when there are multiple matches
     * @param where Query parameters (optional)
     * @param callback The method to be invoked when the server response is received
     */
    public void autoJoinGroupMulti(String[]  groupTypes, AutoJoinStrategy autoJoinStrategy, String where, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonData = new JSONArray();
            for (String att : groupTypes) {
                jsonData.put(att);
            }
            data.put(Parameter.groupTypes.name(), groupTypes);
            data.put(Parameter.autoJoinStrategy.name(), autoJoinStrategy);

            if (StringUtil.IsOptionalParameterValid(where))
                data.put(Parameter.where.name(), where);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.AUTO_JOIN_GROUP_MULTI, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Cancel an outstanding invitation to the group.
     *
     * Service Name - group
     * Service Operation - CANCEL_GROUP_INVITATION
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the invitation being deleted.
     * @param callback The method to be invoked when the server response is received
     */
    public void cancelGroupInvitation(
            String groupId,
            String profileId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.CANCEL_GROUP_INVITATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Create a group.
     *
     * Service Name - group
     * Service Operation - CREATE_GROUP
     *
     * @param name Name of the group.
     * @param groupType Name of the type of group.
     * @param isOpenGroup true if group is open; false if closed.
     * @param acl The group's access control list. A null ACL implies default.
     * @param jsonOwnerAttributes Attributes for the group owner (current player).
     * @param jsonDefaultMemberAttributes Default attributes for group members.
     * @param jsonData Custom application data.
     * @param callback The method to be invoked when the server response is received
     */
    public void createGroup(
            String name,
            String groupType,
            boolean isOpenGroup,
            GroupACL acl,
            String jsonData,
            String jsonOwnerAttributes,
            String jsonDefaultMemberAttributes,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.name.name(), name);
            data.put(Parameter.groupType.name(), groupType);
            data.put(Parameter.isOpenGroup.name(), isOpenGroup);

            if (acl != null)
                data.put(Parameter.acl.name(), new JSONObject(acl.toJsonString()));
            if (StringUtil.IsOptionalParameterValid(jsonData))
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            if (StringUtil.IsOptionalParameterValid(jsonOwnerAttributes))
                data.put(Parameter.ownerAttributes.name(), new JSONObject(jsonOwnerAttributes));
            if (StringUtil.IsOptionalParameterValid(jsonDefaultMemberAttributes))
                data.put(Parameter.defaultMemberAttributes.name(), new JSONObject(jsonDefaultMemberAttributes));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.CREATE_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Create a group with summaryData.
     *
     * Service Name - group
     * Service Operation - CREATE_GROUP
     *
     * @param name Name of the group.
     * @param groupType Name of the type of group.
     * @param isOpenGroup true if group is open; false if closed.
     * @param acl The group's access control list. A null ACL implies default.
     * @param jsonOwnerAttributes Attributes for the group owner (current player).
     * @param jsonDefaultMemberAttributes Default attributes for group members.
     * @param jsonData Custom application data.
     * @param summaryData summary
     * @param callback The method to be invoked when the server response is received
     */
    public void createGroupWithSummaryData(
            String name,
            String groupType,
            boolean isOpenGroup,
            GroupACL acl,
            String jsonData,
            String jsonOwnerAttributes,
            String jsonDefaultMemberAttributes,
            String summaryData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.name.name(), name);
            data.put(Parameter.groupType.name(), groupType);
            data.put(Parameter.isOpenGroup.name(), isOpenGroup);

            if (acl != null)
                data.put(Parameter.acl.name(), new JSONObject(acl.toJsonString()));
            if (StringUtil.IsOptionalParameterValid(jsonData))
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            if (StringUtil.IsOptionalParameterValid(jsonOwnerAttributes))
                data.put(Parameter.ownerAttributes.name(), new JSONObject(jsonOwnerAttributes));
            if (StringUtil.IsOptionalParameterValid(jsonDefaultMemberAttributes))
                data.put(Parameter.defaultMemberAttributes.name(), new JSONObject(jsonDefaultMemberAttributes));
            if (StringUtil.IsOptionalParameterValid(summaryData))
                data.put(Parameter.summaryData.name(), new JSONObject(summaryData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.CREATE_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Create a group entity.
     *
     * Service Name - group
     * Service Operation - CREATE_GROUP_ENTITY
     *
     * @param groupId ID of the group.
     * @param isOwnedByGroupMember true if entity is owned by a member; false if owned by the entire group.
     * @param entityType Type of the group entity.
     * @param acl Access control list for the group entity.
     * @param jsonData Custom application data.
     * @param callback The method to be invoked when the server response is received
     */
    public void createGroupEntity(
            String groupId,
            String entityType,
            boolean isOwnedByGroupMember,
            GroupACL acl,
            String jsonData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.groupId.name(), groupId);
            if (StringUtil.IsOptionalParameterValid(entityType))
                data.put(Parameter.groupType.name(), entityType);
            data.put(Parameter.isOwnedByGroupMember.name(), isOwnedByGroupMember);
            if (acl != null)
                data.put(Parameter.acl.name(), new JSONObject(acl.toJsonString()));
            if (StringUtil.IsOptionalParameterValid(jsonData))
                data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.CREATE_GROUP_ENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Delete a group.
     *
     * Service Name - group
     * Service Operation - DELETE_GROUP
     *
     * @param groupId ID of the group.
     * @param version Current version of the group
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteGroup(String groupId, long version, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.DELETE_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Delete a group entity.
     *
     * Service Name - group
     * Service Operation - DELETE_GROUP_ENTITY
     *
     * @param groupId ID of the group.
     * @param entityId ID of the entity.
     * @param version The current version of the group entity (for concurrency checking).
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteGroupEntity(String groupId, String entityId, long version, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.DELETE_GROUP_ENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read information on groups to which the current player belongs.
     *
     * Service Name - group
     * Service Operation - GET_MY_GROUPS
     * @param callback The method to be invoked when the server response is received
     */
    public void getMyGroups(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.group, ServiceOperation.GET_MY_GROUPS, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Increment elements for the group's data field.
     *
     * Service Name - group
     * Service Operation - INCREMENT_GROUP_DATA
     *
     * @param groupId ID of the group.
     * @param jsonData Partial data map with incremental values.
     * @param callback The method to be invoked when the server response is received
     */
    public void incrementGroupData(String groupId, String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.INCREMENT_GROUP_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Increment elements for the group entity's data field.
     *
     * Service Name - group
     * Service Operation - INCREMENT_GROUP_ENTITY_DATA
     *
     * @param groupId ID of the group.
     * @param entityId ID of the entity.
     * @param jsonData Partial data map with incremental values.
     * @param callback The method to be invoked when the server response is received
     */
    public void incrementGroupEntityData(String groupId, String entityId, String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.INCREMENT_GROUP_ENTITY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Invite a member to the group.
     *
     * Service Name - group
     * Service Operation - INVITE_GROUP_MEMBER
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the member being invited.
     * @param role Role of the member being invited.
     * @param jsonAttributes Attributes of the member being invited.
     * @param callback The method to be invoked when the server response is received
     */
    public void inviteGroupMember(
            String groupId,
            String profileId,
            Role role,
            String jsonAttributes,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.role.name(), role.name());

            if (StringUtil.IsOptionalParameterValid(jsonAttributes)) {
                JSONObject obj = new JSONObject(jsonAttributes);
                data.put(Parameter.attributes.name(), obj);
            }

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.INVITE_GROUP_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Join an open group or request to join a closed group.
     *
     * Service Name - group
     * Service Operation - JOIN_GROUP
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void joinGroup(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.JOIN_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Leave a group in which the player is a member.
     *
     * Service Name - group
     * Service Operation - LEAVE_GROUP
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void leaveGroup(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.LEAVE_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read a page of group information.
     *
     * Service Name - group
     * Service Operation - LIST_GROUPS_PAGE
     *
     * @param jsonContext Query context.
     * @param callback The method to be invoked when the server response is received
     */
    public void listGroupsPage(String jsonContext, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), new JSONObject(jsonContext));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.LIST_GROUPS_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read a page of group information.
     *
     * Service Name - group
     * Service Operation - LIST_GROUPS_PAGE_BY_OFFSET
     *
     * @param encodedContext Encoded reference query context.
     * @param pageOffset Number of pages by which to offset the query.
     * @param callback The method to be invoked when the server response is received
     */
    public void listGroupsPageByOffset(String encodedContext, int pageOffset, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), encodedContext);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.LIST_GROUPS_PAGE_BY_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read information on groups to which the specified player belongs.  Access is subject to restrictions.
     *
     * Service Name - group
     * Service Operation - LIST_GROUPS_WITH_MEMBER
     *
     * @param profileId
     * @param callback The method to be invoked when the server response is received
     */
    public void listGroupsWithMember(String profileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.LIST_GROUPS_WITH_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read the specified group.
     *
     * Service Name - group
     * Service Operation - READ_GROUP
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroup(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read a page of group entity information.
     *
     * Service Name - group
     * Service Operation - READ_GROUP_ENTITIES_PAGE
     *
     * @param jsonContext Query context.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroupEntitiesPage(String jsonContext, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), new JSONObject(jsonContext));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP_ENTITIES_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read a page of group entity information.
     *
     * Service Name - group
     * Service Operation - READ_GROUP_ENTITIES_PAGE_BY_OFFSET
     *
     * @param encodedContext Encoded reference query context.
     * @param pageOffset Number of pages by which to offset the query.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroupEntitiesPageByOffset(String encodedContext, int pageOffset, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), encodedContext);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP_ENTITIES_PAGE_BY_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read the data of the specified group.
     *
     * Service Name - group
     * Service Operation - READ_GROUP_DATA
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroupData(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read the specified group entity.
     *
     * Service Name - group
     * Service Operation - READ_GROUP_ENTITY
     *
     * @param groupId ID of the group.
     * @param entityId ID of the entity.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroupEntity(String groupId, String entityId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.entityId.name(), entityId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP_ENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Read the members of the group.
     *
     * Service Name - group
     * Service Operation - READ_MEMBERS_OF_GROUP
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void readGroupMembers(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.READ_GROUP_MEMBERS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Reject an outstanding invitation to join the group.
     *
     * Service Name - group
     * Service Operation - REJECT_GROUP_INVITATION
     *
     * @param groupId ID of the group.
     * @param callback The method to be invoked when the server response is received
     */
    public void rejectGroupInvitation(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.REJECT_GROUP_INVITATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Reject an outstanding request to join the group.
     *
     * Service Name - group
     * Service Operation - REJECT_GROUP_JOIN_REQUEST
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the invitation being deleted.
     * @param callback The method to be invoked when the server response is received
     */
    public void rejectGroupJoinRequest(String groupId, String profileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.REJECT_GROUP_JOIN_REQUEST, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Remove a member from the group.
     *
     * Service Name - group
     * Service Operation - REMOVE_GROUP_MEMBER
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the member being deleted.
     * @param callback The method to be invoked when the server response is received
     */
    public void removeGroupMember(String groupId, String profileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.REMOVE_GROUP_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Set whether a group is open (true) or closed (false).
     *
     * Service Name - group
     * Service Operation - SET_GROUP_OPEN
     *
     * @param groupId ID of the group.
     * @param isOpenGroup true if group is open; false if closed
     * @param callback The method to be invoked when the server response is received
     */
    public void setGroupOpen(String groupId, boolean isOpenGroup, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.isOpenGroup.name(), isOpenGroup);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.SET_GROUP_OPEN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates a group's data.
     *
     * Service Name - group
     * Service Operation - UPDATE_GROUP_DATA
     *
     * @param groupId ID of the group.
     * @param version Version to verify.
     * @param jsonData Data to apply.
     * @param callback The method to be invoked when the server response is received
     */
    public void updateGroupData(String groupId, long version, String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.UPDATE_GROUP_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Update a group entity.
     *
     * Service Name - group
     * Service Operation - UPDATE_GROUP_ENTITY_DATA
     *
     * @param groupId ID of the group.
     * @param entityId ID of the entity.
     * @param version The current version of the group entity (for concurrency checking).
     * @param jsonData Custom application data.
     * @param callback The method to be invoked when the server response is received
     */
    public void updateGroupEntityData(
            String groupId,
            String entityId,
            long version,
            String jsonData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.entityId.name(), entityId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.data.name(), new JSONObject(jsonData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.UPDATE_GROUP_ENTITY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Update a member of the group.
     *
     * Service Name - group
     * Service Operation - UPDATE_GROUP_MEMBER
     *
     * @param groupId ID of the group.
     * @param profileId Profile ID of the member being updated.
     * @param role Role of the member being updated (optional).
     * @param jsonAttributes Attributes of the member being updated (optional).
     * @param callback The method to be invoked when the server response is received
     */
    public void updateGroupMember(
            String groupId,
            String profileId,
            Role role,
            String jsonAttributes,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.profileId.name(), profileId);
            if (role != null) data.put(Parameter.role.name(), role.name());
            if (StringUtil.IsOptionalParameterValid(jsonAttributes))
                data.put(Parameter.attributes.name(), new JSONObject(jsonAttributes));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.UPDATE_GROUP_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates a group's name.
     *
     * Service Name - group
     * Service Operation - UPDATE_GROUP_NAME
     *
     * @param groupId ID of the group.
     * @param name Name to apply.
     * @param callback The method to be invoked when the server response is received
     */
    public void updateGroupName(
            String groupId,
            String name,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.name.name(), name);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.UPDATE_GROUP_NAME, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates a group's summaryData
     *
     * Service Name - group
     * Service Operation - UPDATE_GROUP_SUMMARY_DATA
     *
     * @param groupId ID of the group.
     * @param version version of the group
     * @param jsonSummaryData summary
     * @param callback The method to be invoked when the server response is received
     */
    public void updateGroupSummaryData(
            String groupId,
            int version,
            String jsonSummaryData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.version.name(), version);
            if (StringUtil.IsOptionalParameterValid(jsonSummaryData))
                data.put(Parameter.summaryData.name(), new JSONObject(jsonSummaryData));

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.UPDATE_GROUP_SUMMARY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a list of up to maxReturn randomly selected groups from the server based on the where condition
     *
     * Service Name - group
     * Service Operation - GET_RANDOM_GROUPS_MATCHING
     *
     * @param jsonWhere ID of the group.
     * @param maxReturn max num groups to search
     * @param callback The method to be invoked when the server response is received
     */
    public void getRandomGroupsMatching(
            String jsonWhere,
            int maxReturn,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (StringUtil.IsOptionalParameterValid(jsonWhere))
                data.put(Parameter.where.name(), new JSONObject(jsonWhere));
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall sc = new ServerCall(ServiceName.group,
                    ServiceOperation.GET_RANDOM_GROUPS_MATCHING, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}


















