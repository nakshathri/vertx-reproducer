package com.vertx;

import java.io.IOException;
import java.net.URL;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Server {

	public static void main(String[] args) {
		
		ClusterManager mgr = new HazelcastClusterManager(hazelcastInstance());
		VertxOptions options = new VertxOptions().setClusterManager(mgr);

		DeploymentOptions workerDeployOpts = new DeploymentOptions().setWorker(true).setMultiThreaded(true);
		
		Vertx.clusteredVertx(options, res -> {
			if (res.succeeded()) {
				Vertx vertx = res.result();
				// You should deploy verticles only when cluster has been initialized
				vertx.deployVerticle(new ServerVerticle(Integer.parseInt(args[0])));
				vertx.deployVerticle(new WorkerVerticle(), workerDeployOpts);
			} else {
			}
		});
	}

	 private static HazelcastInstance hazelcastInstance() {
	    	HazelcastInstance client = null;
	    	try{
	    		System.out.println("Creating hazelcast client instance");
	    		String configFileName = "hazelcast-client.xml";
		    	URL configFileUrl = Server.class.getClassLoader().getResource(configFileName);
		    	XmlClientConfigBuilder builder = new XmlClientConfigBuilder(configFileUrl);
				ClientConfig config = builder.build();
	    		client = HazelcastClient.newHazelcastClient(config);
	    		System.out.println("created hazelcast client instance");
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    	return client;
	    }
	
}
