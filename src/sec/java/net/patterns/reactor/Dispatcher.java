package net.patterns.reactor;


/**
 * Represents the event dispatching strategy. When {@link NioReactor} senses any event on the
 * registered {@link AbstractNioChannel}s then it de-multiplexes the event type, read or write or
 * connect, and then calls the {@link Dispatcher} to dispatch the read events. This decouples the
 * I/O processing from application specific processing. <br/>
 * Dispatcher should call the {@link ChannelHandler} associated with the channel on which event
 * occurred.
 * 
 * <p>
 * The application can customize the way in which event is dispatched such as using the reactor
 * thread to dispatch event to channels or use a worker pool to do the non I/O processing.
 * 
 * @see SameThreadDispatcher
 * @see ThreadPoolDispatcher
 */
public interface Dispatcher {

}
