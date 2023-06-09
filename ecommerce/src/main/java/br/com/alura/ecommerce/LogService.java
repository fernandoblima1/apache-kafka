package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        var LogService = new LogService();
        var consumer = new KafkaService(Pattern.compile("ECOMMERCE.*"), 
            LogService::parse, 
            LogService.class.getSimpleName(),
            String.class,
            Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
        consumer.run();
    }
    public void parse(ConsumerRecord<String, String> record){

        System.out.println("------------------------------------------");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }
}

