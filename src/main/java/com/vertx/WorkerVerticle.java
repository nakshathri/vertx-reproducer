package com.vertx;

import io.vertx.core.AbstractVerticle;

public class WorkerVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		getVertx().eventBus().consumer("event.address").handler(msg -> {
			System.out.println("Handling the message");
			msg.reply(" API Call Successful. ");
		});
		super.start();
	}
}
