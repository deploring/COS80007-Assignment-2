package au.edu.swin.ajass.enums;

/**
 * Represents an intent of communication between a client
 * and server. This should be used by the processing threads
 * to identify what needs to be done with the data given.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public enum Communication {
    SERVER_SEND_ITEMS, // The server is acknowledging a client's request for all available menu items.
    SERVER_UPDATE_ORDER, // The server is acknowledging an order being moved into a new order state.
    SERVER_ADD_ORDER, // The server is acknowledging a new order being placed into the system.
    SERVER_SEND_ORDERS, // The server is acknowledging a client's request for all current orders.
    CLIENT_WANT_ITEMS, // The client wants to retrieve all available menu items.
    CLIENT_CREATE_ORDER, // The client is sending a new order that it created.
    CLIENT_UPDATE_ORDER, // The client is sending a request to place an existing order into a new order state.
    CLIENT_WANT_ORDERS, // The client wants to retrieve all current orders.
    SENTINEL // No further data retrieval should take place, if any has taken place.
}
