package au.edu.swin.ajass.enums;

/**
 * Represents all the possible states that an order can have.
 */
public enum OrderState {
    WAITING, // The order has been created by the waiter.
    SERVED, // The order has been prepared by the chef and served.
    BILLED // The order has been billed by the cashier.
}
