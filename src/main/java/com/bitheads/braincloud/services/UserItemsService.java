package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

public class UserItemsService {

    private enum Parameter {
        defId,
        quantity,
        includeDef,
        criteria, 
        context, 
        pageOffset,
        itemId,
        version,
        immediate,
        shopId,
        newItemData,
        profileId
    }

    private BrainCloudClient _client;

    public UserItemsService(BrainCloudClient client) {
        _client = client;
    }

	/**
	 * Allows item(s) to be awarded to a user without collecting
	 *  the purchase amount. If includeDef is true, response 
	 * includes associated itemDef with language fields limited
	 *  to the current or default language.
	 *
	 * Service Name - userItems
	 * Service Operation - AWARD_USER_ITEM
	 *
	 * @param defId	The unique id of the item definition to award.
	 * @param quantity	The quantity of the item to award.
	 * @param includeDef	If true, the associated item definition will be included in the response.
	 * @param callback	The callback handler
	 */
    public void awardUserItem(String defId, int quantity, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.defId.name(), defId);
            data.put(Parameter.quantity.name(), quantity);
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.AWARD_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Allows a quantity of a specified user item to be dropped, 
	 * without any recovery of the money paid for the item. 
	 * If any quantity of the user item remains, it will be returned,
	 * potentially with the associated itemDef (with language fields 
	 * limited to the current or default language).
	 *
	 * Service Name - userItems
	 * Service Operation - DROP_USER_ITEM
	 *
	 * @param itemId		The unique id of the user item.
	 * @param quantity		The quantity of the user item to drop.
	 * @param includeDef	If true and any quantity of the user item remains, the associated item definition will be included in the response.
	 * @param callback		The callback handler
	 */
    public void dropUserItem(String itemId, int quantity, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.quantity.name(), quantity);
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.DROP_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Retrieves the page of user's items from the server 
	 * based on the context. If includeDef is true, response
	 *  includes associated itemDef with each user item, with 
	 * language fields limited to the current or default language.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_ITEMS_PAGE
	 *
	 * @param context	The json context for the page request.
	 * @param includeDef	If true, the associated item definition will be included in the response.
	 * @param callback	The callback handler
	 */
    public void getUserItemsPage(String context, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.context.name(), new JSONObject(context));
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_USER_ITEMS_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Retrieves the page of user's items from the server
	 *  based on the encoded context. If includeDef is true, 
	 * response includes associated itemDef with each user item, 
	 * with language fields limited to the current or default
	 * language.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_ITEMS_PAGE_OFFSET
	 *
	 * @param context		The context string returned from the server from a previous call to SysGetCatalogItemsPage or SysGetCatalogItemsPageOffset.
	 * @param pageOffset	The positive or negative page offset to fetch. 
	 						Uses the last page retrieved using the context string to determine a starting point.
	 * @param includeDef 	If true, the associated item definition will be included in the response.
	 * @param callback		The callback handler
	 */
    public void getUserItemsPageOffset(String context, int pageOffset, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_USER_ITEMS_PAGE_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Retrieves the identified user item from the server. 
	 * If includeDef is true, response includes associated
	 * itemDef with language fields limited to the current 
	 * or default language.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_ITEM
	 *
	 * @param itemId	The unique id of the user item.
	 * @param includeDef	If true, the associated item definition will be included in the response.
	 * @param callback The method to be invoked when the server response is received
	 */
    public void getUserItem(String itemId, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Gifts item to the specified player.
	 *
	 * Service Name - userItems
	 * Service Operation - GIVE_USER_ITEM_TO
	 *
	 * @param profileId	The ID of the recipient's user profile.
	 * @param itemId	The ID uniquely identifying the user item to be transferred.
	 * @param version	The version of the user item being transferred.
	 * @param quantity	The quantity of the user item to transfer.
	 * @param immediate	Flag set to true if item is to be immediately transferred, 
	 					otherwise false to have the sender send an event and transfers item(s) only when recipient calls receiveUserItemFrom.
	 * @param callback	The callback handler
	 */
    public void giveUserItemTo(String profileId, String itemId, int version, int quantity, boolean immediate, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.itemId.name(), itemId);
			data.put(Parameter.version.name(), version);
			data.put(Parameter.quantity.name(), quantity);
            data.put(Parameter.immediate.name(), immediate);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GIVE_USER_ITEM_TO, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Retrieves the identified user item from the server. 
	 * If includeDef is true, response includes associated
	 * itemDef with language fields limited to the current 
	 * or default language.
	 *
	 * Service Name - userItems
	 * Service Operation - PURCHASE_USER_ITEM
	 *
	 * @param defId	The unique id of the item definition to purchase.
	 * @param quantity	The quantity of the item to purchase.
	 * @param shopId	The id identifying the store the item is being purchased from 
	 					(not yet supported) 
						Use null or empty string to specify the default shop price.
	 * @param includeDef	If true, the associated item definition will be included in the response.
	 * @param callback	The callback handler
	 */
    public void purchaseUserItem(String defId, int quantity, String shopId, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.defId.name(), defId);
            data.put(Parameter.quantity.name(), quantity);
            data.put(Parameter.shopId.name(), shopId);
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.PURCHASE_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Retrieves and transfers the gift item from 
	 * the specified player, who must have previously 
	 * called giveUserItemTo.
	 *
	 * Service Name - userItems
	 * Service Operation - RECEVIE_USER_ITEM_FROM
	 *
	 * @param profileId	The profile ID of the user who is giving the item.
	 * @param itemId	The ID uniquely identifying the user item to be transferred.
	 * @param callback	The callback handler
	 */
    public void receiveUserItemFrom(String profileId, String itemId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.itemId.name(), itemId);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.RECEIVE_USER_ITEM_FROM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Allows a quantity of a specified user item to be sold. 
	 * If any quantity of the user item remains, it will be returned, 
	 * potentially with the associated itemDef (with language fields 
	 * limited to the current or default language), along with the 
	 * currency refunded and currency balances.
	 *
	 * Service Name - userItems
	 * Service Operation - SELL_USER_ITEM
	 *
	 * @param itemId	The unique id of the user item.
	 * @param version	The version of the user item being sold.
	 * @param quantity	The quantity of the user item to sell.
	 * @param shopId	The id identifying the store the item is being purchased from 
	 					(not yet supported) 
						Use null or empty string to specify the default shop price.
	 * @param includeDef 	If true and any quantity of the user item remains, the associated item definition will be included in the response.
	 * @param callback	The callback handler
	 */
    public void sellUserItem(String itemId, int version, int quantity, String shopId, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.quantity.name(), quantity);
            data.put(Parameter.shopId.name(), shopId);
            data.put(Parameter.includeDef.name(), includeDef);


            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.SELL_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Updates the item data on the specified user item.
	 *
	 * Service Name - userItems
	 * Service Operation - UPDATE_USER_ITEM_DATA
	 *
	 * @param itemId	The unique id of the user item.
	 * @param version	The version of the user item being updated.
	 * @param newItemData	New item data to replace existing user item data.
	 * @param callback	The callback handler
	 */
    public void updateUserItemData(String itemId, int version, String newItemData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.newItemData.name(), new JSONObject(newItemData));

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.UPDATE_USER_ITEM_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
	 * Uses the specified item, potentially consuming it.
	 *
	 * Service Name - userItems
	 * Service Operation - USE_USER_ITEM
	 *
	 * @param itemId	The unique id of the user item.
	 * @param version	The version of the user item being used.
	 * @param newItemData	Optional item data to replace existing user item data. 
	 						Specify null to leave item data unchanged. 
							Specify empty map to clear item data.
	 * @param includeDef 	If true, the associated item definition will be included in the response.
	 * @param callback	The callback handler
	 */
    public void useUserItem(String itemId, int version, String newItemData, boolean includeDef, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.newItemData.name(), new JSONObject(newItemData));
            data.put(Parameter.includeDef.name(), includeDef);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.USE_USER_ITEM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
	}
	
	/**
	 * Publishes the specified item to the item management attached blockchain. Results are reported asynchronously via an RTT event.
	 *
	 * Service Name - userItems
	 * Service Operation - PUBLISH_USER_ITEM_TO_BLOCKCHAIN
	 *
	 * @param itemId	The unique id of the user item.
	 * @param version	The version of the user item being published.
	 * @param callback	The callback handler
	 */
    public void publishUserItemToBlockchain(String itemId, int version, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.PUBLISH_USER_ITEM_TO_BLOCKCHAIN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
	}
	
	/**
	 * Syncs the caller's user items with the item management attached blockchain. Results are reported asynchronously via an RTT event.
	 *
	 * Service Name - userItems
	 * Service Operation - REFRESH_BLOCKCHAIN_USER_ITEMS
	 *
	 * @param callback	The callback handler
	 */
    public void refreshBlockchainUserItems(IServerCallback callback) {
            JSONObject data = new JSONObject();
            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.REFRESH_BLOCKCHAIN_USER_ITEMS, data, callback);
            _client.sendRequest(sc);
	}
	
	/**
	 * Removes the specified item from the item management attached blockchain. Results are reported asynchronously via an RTT event.
	 * Service Name - userItems
	 * Service Operation - REMOVE_USER_ITEM_FROM_BLOCKCHAIN
	 *
	 * @param itemId	The unique id of the user item.
	 * @param version	The version of the user item being removed.
	 * @param callback	The callback handler
	 */
    public void removeUserItemFromBlockchain(String itemId, int version, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.itemId.name(), itemId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.REMOVE_USER_ITEM_FROM_BLOCKCHAIN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
	}
}
