package br.com.alura.ecommerce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService("ECOMMERCE_SEND_EMAIL",
        emailService::parse, EmailService.class.getName(),
        Email.class,
        new HashMap<String, String>())) {
            service.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void parse(ConsumerRecord<String, String> record){
        System.out.println("------------------------------------------");
        System.out.println("Processing new email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
        System.out.println("Email was sent");
    }

}