package br.com.alura.ecommerce;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;;

public class GsonDeserializer<T> implements Deserializer<T>{
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;
    public static final String TYPE_CONFIG = "br.com.alura.ecommerce.type_config";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch (Exception e) {
            throw new RuntimeException("This class doesn't exists.");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), type);
    }
}
