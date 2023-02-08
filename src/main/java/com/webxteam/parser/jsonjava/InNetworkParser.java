package com.webxteam.parser.jsonjava;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.webxteam.parser.jsonjava.dto.innetwork.InNetworkDto;
import com.webxteam.parser.jsonjava.dto.innetwork.ProviderReferenceDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InNetworkParser {


    public void parse(String path) throws IOException {
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
