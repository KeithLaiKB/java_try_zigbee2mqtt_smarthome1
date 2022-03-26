# java_try_zigbee2mqtt_smarthome1

I tried diffrent design

now I tried domain-driven design
create a thread one time running to sent publish_get message to the IoT device, 
because initially, if service want the information, but now the IoT device didn't send the topic(because IoT device would send the notification in interval) 
in this case, you can use publish_get message to notify the IoT device to send the status immediately right now to the broker
![image](https://github.com/KeithLaiKB/java_try_zigbee2mqtt_smarthome1/blob/main/doc/ddd_doc/Model_sendMessage_Main_0.jpg)

create a thread all time running to receive the notification(status) of IoT device and save the status in database
![image](https://github.com/KeithLaiKB/java_try_zigbee2mqtt_smarthome1/blob/main/doc/ddd_doc/Model_subscribeMessage_Main_2.jpg)

create a thread one time running to get the status of the IoT device from the database
![image](https://github.com/KeithLaiKB/java_try_zigbee2mqtt_smarthome1/blob/main/doc/ddd_doc/Model_readMessage_Main_3.jpg)

intergration of publish message t
![image](https://github.com/KeithLaiKB/java_try_zigbee2mqtt_smarthome1/blob/main/doc/ddd_doc/Model_publishget_and_subscribe_ObjectDiagram1_4.jpg)
![image](https://github.com/KeithLaiKB/java_try_zigbee2mqtt_smarthome1/blob/main/doc/ddd_doc/Model_readAndPublish_ObjectDiagram1_5.jpg)
