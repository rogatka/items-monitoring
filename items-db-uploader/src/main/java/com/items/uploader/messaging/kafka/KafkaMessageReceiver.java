package com.items.uploader.messaging.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface KafkaMessageReceiver<T> {
    void receive(ConsumerRecord<String, T> consumerRecord, Acknowledgment acknowledgment);
}
