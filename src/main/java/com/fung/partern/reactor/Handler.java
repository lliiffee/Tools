package com.fung.partern.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public final class Handler implements Runnable {
	final SocketChannel socket;
	final SelectionKey sk;
	int MAXIN=2*1024;
	int MAXOUT=2*1024;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	static final int READING = 0, SENDING = 1;
	int state = READING;

	Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}

	boolean inputIsComplete() { /* ... */
		return false;
	}

	boolean outputIsComplete() { /* ... */
		return false;
	}

	void process() { /* ... */
	}

	public void run() {
		try {
			if (state == READING)
				read();
			else if (state == SENDING)
				send();
		} catch (IOException ex) { /* ... */
		}
	}

	void read() throws IOException {
		socket.read(input);
		if (inputIsComplete()) {
			process();
			state = SENDING;
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		socket.write(output);
		if (outputIsComplete())
			sk.cancel();
	}

}
