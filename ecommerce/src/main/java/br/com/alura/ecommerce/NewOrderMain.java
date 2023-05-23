package br.com.alura.ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderProducer = new KafkaDispatcher<Order>()) {
            try (var emailProducer = new KafkaDispatcher<Email>()) {

                for(var i = 0 ; i < 10; i++){
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);
    
                    var order = new Order(userId,orderId, amount);
    
                    var key = UUID.randomUUID().toString();
                    var subject = "Sendding a email";
                    var body = "This is the email body";
                    var email = new Email(subject, body);
    
                    orderProducer.send("ECOMMERCE_NEW_ORDER", key, order);
                    emailProducer.send("ECOMMERCE_SEND_EMAIL", key, email);
                }
            }
        } catch (IOException e) {
            System.out.println("Ocorreu algum erro, então o producer foi fechado.");
        }
        
    }

}
