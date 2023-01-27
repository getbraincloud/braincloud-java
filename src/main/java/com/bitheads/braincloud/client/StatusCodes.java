package com.bitheads.braincloud.client;

/**
 * Created by prestonjennings on 15-09-01.
 */
public interface StatusCodes {

    /**
     * Everything is ok
     */
    int OK = 0;

    /**
     * Status code for a client side error
     */
    int CLIENT_NETWORK_ERROR = 900;

    /**
     * Status code for an internal server error
     */
    int INTERNAL_SERVER_ERROR = 500;

    int BAD_REQUEST = 400;
    int FORBIDDEN = 403;
}
