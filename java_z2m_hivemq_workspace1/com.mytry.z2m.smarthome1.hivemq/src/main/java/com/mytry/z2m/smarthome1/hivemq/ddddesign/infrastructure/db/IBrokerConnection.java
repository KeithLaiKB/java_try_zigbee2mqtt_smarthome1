package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.db;

import java.util.concurrent.CompletableFuture;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;

public interface IBrokerConnection {
	void myReleaseConnect();
	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> myConnect();
}
