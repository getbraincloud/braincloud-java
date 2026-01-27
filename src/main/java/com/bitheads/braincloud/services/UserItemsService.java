// Copyright 2026 bitHeads, Inc. All Rights Reserved.
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
		context,
		criteria,
		defId,
		immediate,
		includeDef,
		includePromotionDetails,
		itemId,
		newItemData,
		optionsJson,
		pageOffset,
		profileId,
		quantity,
		shopId,
		version
	}

	private BrainCloudClient _client;

	public UserItemsService(BrainCloudClient client) {
		_client = client;
	}

	/**
	 * Awards item(s) to a user without collecting the purchase amount.
	 * If includeDef is true, response includes associated itemDef
	 * with language fields limited to the current or default language.
	 *
	 * Service Name - userItems
	 * Service Operation - AWARD_USER_ITEM
	 *
	 * @param defId      The unique id of the item definition to award.
	 * @param quantity   The quantity of the item to award.
	 * @param includeDef If true, include associated item definition in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
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
	 * Awards item(s) to a user with additional options.
	 *
	 * Service Name - userItems
	 * Service Operation - AWARD_USER_ITEM
	 *
	 * @param defId       The unique id of the item definition to award.
	 * @param quantity    The quantity of the item to award.
	 * @param includeDef  If true, include associated item definition in the
	 *                    response.
	 * @param optionsJson JSON string specifying additional options (e.g.,
	 *                    blockIfExceedItemMaxStackable).
	 * @param callback    The method to be invoked when the server response is
	 *                    received
	 */
	public void awardUserItemWithOptions(String defId, int quantity, boolean includeDef, String optionsJson,
			IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.defId.name(), defId);
			data.put(Parameter.quantity.name(), quantity);
			data.put(Parameter.includeDef.name(), includeDef);
			if (optionsJson != null) {
				data.put(Parameter.optionsJson.name(), new JSONObject(optionsJson));
			}

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.AWARD_USER_ITEM, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drops a quantity of a specified user item without recovering the purchase
	 * cost.
	 * If any quantity remains, it may include the associated itemDef.
	 *
	 * Service Name - userItems
	 * Service Operation - DROP_USER_ITEM
	 *
	 * @param defId      The unique id of the item definition to drop.
	 * @param quantity   The quantity of the item to drop.
	 * @param includeDef If true, include associated item definition in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
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
	 * Returns a list of promotional details for a specified item.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_ITEM_PROMOTION_DETAILS
	 *
	 * @param defId                   Item definition ID.
	 * @param shopId                  Store ID.
	 * @param includeDef              Include associated item definition if true.
	 * @param includePromotionDetails Include promotion details if true.
	 * @param callback                Callback invoked when the server response is
	 *                                received.
	 */
	public void getItemPromotionDetails(String defId, String shopId, boolean includeDef,
			boolean includePromotionDetails, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.defId.name(), defId);
			data.put(Parameter.shopId.name(), shopId);
			data.put(Parameter.includeDef.name(), includeDef);
			data.put(Parameter.includePromotionDetails.name(), includePromotionDetails);

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_ITEM_PROMOTION_DETAILS, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of items on promotion available to the current user.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_ITEMS_ON_PROMOTION
	 *
	 * @param shopId                  Store ID.
	 * @param includeDef              Include associated item definition if true.
	 * @param includePromotionDetails Include promotion details if true.
	 * @param optionsJson             JSON string specifying additional options
	 *                                (e.g., category).
	 * @param callback                Callback invoked when the server response is
	 *                                received.
	 */
	public void getItemsOnPromotion(String shopId, boolean includeDef, boolean includePromotionDetails,
			String optionsJson,
			IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.shopId.name(), shopId);
			data.put(Parameter.includeDef.name(), includeDef);
			data.put(Parameter.includePromotionDetails.name(), includePromotionDetails);
			if (optionsJson != null) {
				data.put(Parameter.optionsJson.name(), new JSONObject(optionsJson));
			}

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_ITEMS_ON_PROMOTION, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a page of the user's inventory.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_INVENTORY_PAGE
	 *
	 * @param context    Context string used to filter inventory.
	 * @param includeDef If true, include associated item definitions in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
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
	 * Retrieves a page of the user's inventory with an offset.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_INVENTORY_PAGE_OFFSET
	 *
	 * @param context    Context string used to filter inventory.
	 * @param pageOffset Page offset to retrieve.
	 * @param includeDef If true, include associated item definitions in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
	 */
	public void getUserItemsPageOffset(String context, int pageOffset, boolean includeDef, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.context.name(), context);
			data.put(Parameter.pageOffset.name(), pageOffset);
			data.put(Parameter.includeDef.name(), includeDef);

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.GET_USER_ITEMS_PAGE_OFFSET, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException ignored) {
		}
	}

	/**
	 * Retrieves a specific user item.
	 *
	 * Service Name - userItems
	 * Service Operation - GET_USER_ITEM
	 *
	 * @param itemId     ID of the user item to retrieve.
	 * @param includeDef If true, include associated item definition in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
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
	 * Gifts an item to another user.
	 *
	 * Service Name - userItems
	 * Service Operation - GIVE_USER_ITEM_TO
	 *
	 * @param profileId Profile ID of the recipient.
	 * @param itemId    ID of the item to gift.
	 * @param version   Version of the item being gifted.
	 * @param quantity  Quantity of the item to gift.
	 * @param immediate If true, the gift is delivered immediately.
	 * @param callback  The method to be invoked when the server response is
	 *                  received
	 */
	public void giveUserItemTo(String profileId, String itemId, int version, int quantity, boolean immediate,
			IServerCallback callback) {
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
	 * Opens a quantity of a bundle user item.
	 * Creates applicable items and awards any currencies.
	 *
	 * Service Name - userItems
	 * Service Operation - OPEN_BUNDLE
	 *
	 * @param itemId      ID of the bundle item to open.
	 * @param version     Version of the bundle item (pass -1 for any version).
	 * @param quantity    Quantity of the item to open.
	 * @param includeDef  Include associated item definitions if true.
	 * @param optionsJson JSON string specifying additional options.
	 * @param callback    The method to be invoked when the server response is
	 *                    received
	 */
	public void openBundle(String itemId, int version, int quantity, boolean includeDef, String optionsJson,
			IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.itemId.name(), itemId);
			data.put(Parameter.version.name(), version);
			data.put(Parameter.quantity.name(), quantity);
			data.put(Parameter.includeDef.name(), includeDef);
			if (optionsJson != null) {
				data.put(Parameter.optionsJson.name(), new JSONObject(optionsJson));
			}

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.OPEN_BUNDLE, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Purchases a user item from a store.
	 *
	 * Service Name - userItems
	 * Service Operation - PURCHASE_USER_ITEM
	 *
	 * @param defId      The unique id of the item definition to purchase.
	 * @param quantity   Quantity of the item to purchase.
	 * @param shopId     Store ID for the purchase.
	 * @param includeDef If true, include associated item definition in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
	 */
	public void purchaseUserItem(String defId, int quantity, String shopId, boolean includeDef,
			IServerCallback callback) {
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
	 * Purchases a quantity of an item from the specified store, if the user has
	 * enough funds and purchasing for listed buy price is not disabled for
	 * associated catalog item definition. If includeDef is true, response includes
	 * associated itemDef with language fields limited to the current or default
	 * language.
	 * 
	 * Service Name - User Items
	 * Service Operation - PURCHASE_USER_ITEM
	 * 
	 * @param defId       The unique id of the item definition to purchase.
	 * @param quantity    The quantity of the item to purchase.
	 * @param shopId      The id identifying the store the item is being purchased
	 *                    from, if applicable.
	 * @param includeDef  If true, the associated item definition will be included
	 *                    in the response.
	 * @param optionsJson Optional support for specifying
	 *                    'blockIfExceedItemMaxStackable' indicating how to process
	 *                    the purchase if the defId is for a stackable item with a
	 *                    max stackable quantity and the specified quantity being
	 *                    purchased is too high. If true and the quantity is too
	 *                    high, the call is blocked and an error is returned. If
	 *                    false (default) and quantity is too high, the quantity is
	 *                    adjusted to the allowed maximum and the quantity not
	 *                    purchased is reported in response key 'itemsNotPurchased'
	 *                    - unless the adjusted quantity would be 0, in which case
	 *                    the call is blocked and an error is returned.
	 * @param callback    The method to be invoked when the server response is
	 *                    received
	 */
	public void purchaseUserItemWithOptions(String defId, int quantity, String shopId, boolean includeDef,
			String optionsJson, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.defId.name(), defId);
			data.put(Parameter.quantity.name(), quantity);
			data.put(Parameter.shopId.name(), shopId);
			data.put(Parameter.includeDef.name(), includeDef);
			if (optionsJson != null) {
				data.put(Parameter.optionsJson.name(), new JSONObject(optionsJson));
			}

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.PURCHASE_USER_ITEM, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves and transfers a gift item from another user.
	 *
	 * Service Name - userItems
	 * Service Operation - RECEIVE_USER_ITEM_FROM
	 *
	 * @param profileId Profile ID of the sender.
	 * @param itemId    ID of the item being received.
	 * @param callback  The method to be invoked when the server response is
	 *                  received
	 */
	public void receiveUserItemFrom(String profileId, String itemId, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.profileId.name(), profileId);
			data.put(Parameter.itemId.name(), itemId);

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.RECEIVE_USER_ITEM_FROM, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException ignored) {
		}
	}

	/**
	 * Sells a user item back to the store.
	 *
	 * Service Name - userItems
	 * Service Operation - SELL_USER_ITEM
	 *
	 * @param itemId     ID of the user item to sell.
	 * @param version    Version of the item being sold.
	 * @param quantity   Quantity of the item to sell.
	 * @param shopId     Store ID for the sale.
	 * @param includeDef If true, include associated item definition in the
	 *                   response.
	 * @param callback   The method to be invoked when the server response is
	 *                   received
	 */
	public void sellUserItem(String itemId, int version, int quantity, String shopId, boolean includeDef,
			IServerCallback callback) {
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
	 * Updates the data of a specific user item.
	 *
	 * Service Name - userItems
	 * Service Operation - UPDATE_USER_ITEM_DATA
	 *
	 * @param itemId      ID of the user item to update.
	 * @param version     Version of the item being updated.
	 * @param newItemData JSON string with updated item data.
	 * @param callback    The method to be invoked when the server response is
	 *                    received
	 */
	public void updateUserItemData(String itemId, int version, String newItemData, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.itemId.name(), itemId);
			data.put(Parameter.version.name(), version);
			data.put(Parameter.newItemData.name(), new JSONObject(newItemData));

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.UPDATE_USER_ITEM_DATA, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException ignored) {
		}
	}

	/**
	 * Uses a user item, potentially consuming it.
	 *
	 * Service Name - userItems
	 * Service Operation - USE_USER_ITEM
	 *
	 * @param itemId      ID of the user item to use.
	 * @param version     Version of the user item (pass -1 for any version).
	 * @param newItemData Optional JSON string to update item fields.
	 * @param includeDef  If true, include associated item definition in the
	 *                    response.
	 * @param callback    The method to be invoked when the server response is
	 *                    received
	 */
	public void useUserItem(String itemId, int version, String newItemData, boolean includeDef,
			IServerCallback callback) {
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
	 * Publishes a user item to the blockchain.
	 *
	 * Service Name - userItems
	 * Service Operation - PUBLISH_USER_ITEM_TO_BLOCKCHAIN
	 *
	 * @param itemId   ID of the user item to publish.
	 * @param version  Version of the item to publish.
	 * @param callback The method to be invoked when the server response is received
	 */
	public void publishUserItemToBlockchain(String itemId, int version, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.itemId.name(), itemId);
			data.put(Parameter.version.name(), version);

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.PUBLISH_USER_ITEM_TO_BLOCKCHAIN,
					data, callback);
			_client.sendRequest(sc);
		} catch (JSONException ignored) {
		}
	}

	/**
	 * Refreshes blockchain user items.
	 *
	 * Service Name - userItems
	 * Service Operation - REFRESH_BLOCKCHAUSER_ITEMS
	 *
	 * @param callback Callback invoked when the server response is received.
	 */
	public void refreshBlockchainUserItems(IServerCallback callback) {
		JSONObject data = new JSONObject();
		ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.REFRESH_BLOCKCHAIN_USER_ITEMS, data,
				callback);
		_client.sendRequest(sc);
	}

	/**
	 * Removes a user item from the blockchain.
	 *
	 * Service Name - userItems
	 * Service Operation - REMOVE_USER_ITEM_FROM_BLOCKCHAIN
	 *
	 * @param itemId   ID of the user item to remove.
	 * @param version  Version of the user item to remove.
	 * @param callback Callback invoked when the server response is received.
	 */
	public void removeUserItemFromBlockchain(String itemId, int version, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.itemId.name(), itemId);
			data.put(Parameter.version.name(), version);

			ServerCall sc = new ServerCall(ServiceName.userItems, ServiceOperation.REMOVE_USER_ITEM_FROM_BLOCKCHAIN,
					data, callback);
			_client.sendRequest(sc);
		} catch (JSONException ignored) {
		}
	}
}
