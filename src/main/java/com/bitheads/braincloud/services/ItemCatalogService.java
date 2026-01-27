// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

public class ItemCatalogService {

    private enum Parameter {
        defId,
        context,
        pageOffset
    }

    private BrainCloudClient _client;

    public ItemCatalogService(BrainCloudClient client) {
        _client = client;
    }

    /**
		 * Reads an existing item definition from the server, with language fields
		 * limited to the current or default language.
		 *
		 * Service Name - ItemCatalog
		 * Service Operation - GET_CATALOG_ITEM_DEFINITION
		 *
		 * @param defId The identifier of the catalog item definition to retrieve
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getCatalogItemDefinition(String defId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.defId.name(), defId);

            ServerCall sc = new ServerCall(ServiceName.itemCatalog, ServiceOperation.GET_CATALOG_ITEM_DEFINITION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
		 * Retrieve a page of catalog items from the server, with language fields
		 * limited to the text for the current or default language.
		 *
		 * Service Name - ItemCatalog
		 * Service Operation - GET_CATALOG_ITEMS_PAGE
		 *
		 * @param context The pagination context returned from a previous catalog page request
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getCatalogItemsPage(String context, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), new JSONObject(context));

            ServerCall sc = new ServerCall(ServiceName.itemCatalog, ServiceOperation.GET_CATALOG_ITEMS_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

	/**
		 * Gets a page of catalog items from the server based on the encoded
		 * context and specified page offset, with language fields limited to the
		 * text for the current or default language.
		 *
		 * Service Name - ItemCatalog
		 * Service Operation - GET_CATALOG_ITEMS_PAGE_OFFSET
		 *
		 * @param context The pagination context returned from a previous catalog page request
		 * @param pageOffset The page offset relative to the current context
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getCatalogItemsPageOffset(String context, int pageOffset, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall sc = new ServerCall(ServiceName.itemCatalog, ServiceOperation.GET_CATALOG_ITEMS_PAGE_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }
}
