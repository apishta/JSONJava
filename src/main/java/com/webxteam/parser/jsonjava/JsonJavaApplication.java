package com.webxteam.parser.jsonjava;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.webxteam.parser.jsonjava.dto.innetwork.InNetworkDto;
import com.webxteam.parser.jsonjava.dto.innetwork.ProviderReferenceDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class JsonJavaApplication implements CommandLineRunner {

    public static void main(String[] args)
    {
        SpringApplication.run(JsonJavaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        IndexParser indexParser = new IndexParser();
        indexParser.parse("D:\\projects\\TiC\\working\\BCBS KC\\2023-01-01_blue-cross-and-blue-shield-of-kansas-city_index.json");

//        InNetworkParser inNetworkParser = new InNetworkParser();
//        inNetworkParser.parse("D:\\projects\\TiC\\working\\BCBS KC\\2023-01_020_02E0_in-network-rates_1_of_2.json");
    }


    //radi
    private void readLargeJson(String path) throws IOException {
        try (
                InputStream inputStream = Files.newInputStream(Path.of(path));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            int i=1;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            while (reader.hasNext()) {
                //Map<String, Object> map = new Gson().fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
                //System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-"+ map.get("billing_code"));
                InNetworkDto inNet = new Gson().fromJson(reader, InNetworkDto.class);
                System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-"+ inNet.billing_code);
            }
            reader.endArray();
        }
    }

    //kompletan json
    private void readLargeJsonComplete(String path) throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of(path));
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if(name.toLowerCase().equals("in_network")) {
                    readInNetwork(reader);
                } else if(name.toLowerCase().equals("provider_references")){
                    readProviderGroups(reader);
                }else{
                    String value = reader.nextString();
                }
            }
            reader.endObject();
        } finally {
        reader.close();
        }
    }

    public void readProviderGroups(JsonReader reader) throws IOException {
        int i =1;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        reader.beginArray();
        while (reader.hasNext()) {
            ProviderReferenceDto providerReferenceDto = new Gson().fromJson(reader, ProviderReferenceDto.class);
            System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-"+ providerReferenceDto.provider_groups.size()+"-" + providerReferenceDto.provider_group_id);
        }
        reader.endArray();
    }
    public void readInNetwork(JsonReader reader) throws IOException {
        int i =1;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(now));
        reader.beginArray();
        while (reader.hasNext()) {
            //Map<String, Object> map = new Gson().fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            //System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-"+ map.get("billing_code"));
            InNetworkDto inNet = new Gson().fromJson(reader, InNetworkDto.class);
            System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-"+ inNet.billing_code);
        }
        reader.endArray();
    }

}
