package br.com.alura.ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> parse;

    private KafkaService(ConsumerFunction<T> parse, String className, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>(getProperties(type, className, properties));
    }

    KafkaService(String topic, ConsumerFunction<T> parse, String className, Class<T> type, Map<String, String> properties) {
        this(parse, className, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(Pattern topic, ConsumerFunction<T> parse, String className, Class<T> type, Map<String, String> properties) {
        this(parse, className, type, properties);
        consumer.subscribe(topic);
    }

    public void run() {
        while(true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                for(var record : records){
                parse.consume(record);
                }
            }
        }
    }
    
    private Properties getProperties(Class<T> type, String className, Map<String, String> overritedProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, className);
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overritedProperties);
        return properties;
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
