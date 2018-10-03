package au.edu.swin.ajass.controllers;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This Interface holds common code and fields
 * shared between both server and client controllers.
 *
 * @author Bradley Chick
 * @version 1.0
 * @since 0.1
 */
public interface ISocketController {

    // Messages waiting to be processed.
    LinkedBlockingQueue<Object> input = new LinkedBlockingQueue<>();

    /**
     * Adds a message from a client to the processing queue.
     *
     * @param message The message received from a Client.
     * @throws InterruptedException May be interrupted while waiting.
     */
    default void newMessage(Object message) throws InterruptedException {
        input.put(message);
    }

    /**
     * Takes a message from the processing queue, for, processing.
     *
     * @return The message to process.
     * @throws InterruptedException May be interrupted while taking.
     */
    default Object takeMessage() throws InterruptedException {
        return input.take();
    }
}
