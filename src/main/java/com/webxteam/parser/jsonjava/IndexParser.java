package com.webxteam.parser.jsonjava;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.webxteam.parser.jsonjava.dto.index.InNetworkFilesDto;
import com.webxteam.parser.jsonjava.dto.index.ReportingStructureDto;
import com.webxteam.parser.jsonjava.dto.innetwork.ProviderReferenceDto;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IndexParser {
    public void parse(String path) throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of(path));
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if(name.toLowerCase().equals("reporting_structure")) {
                    readReportingStructure(reader);
                }else{
                    String value = reader.nextString();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
    }

    public void readReportingStructure(JsonReader reader) throws IOException {
        int i =1;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        reader.beginArray();
        while (reader.hasNext()) {
            ReportingStructureDto reportingStructureDto = new Gson().fromJson(reader, ReportingStructureDto.class);
            downloadFiles(reportingStructureDto);
            System.out.println((i++) + "-"+dtf.format(LocalDateTime.now())+"-");
        }
        reader.endArray();
    }

    private void downloadFiles(ReportingStructureDto reportingStructureDto){
        for (InNetworkFilesDto file: reportingStructureDto.in_network_files) {
            downloadFile(file);
        }
    }

    private void downloadFile(InNetworkFilesDto file){
        try {
            String folder = "D:\\projects\\TiC\\working\\BCBS KC\\download";
            String fileName = extractFileName(file.location);
            System.out.println(file.location);
            FileUtils.copyURLToFile(
                    new URL(file.location),
                    new File(folder+"\\"+fileName));
            System.out.println("Completed - " + file.location);
        }catch (Exception e){
            System.out.println("Error - " + file.location);
        }
    }

    private String getFileName(String url){
        return  url.substring(url.lastIndexOf('/')+1, url.lastIndexOf('.'));
    }

    public static String extractFileName(String url) {
        int index1 = url.indexOf('?');
        int index2 = url.indexOf('#');

        if (index1 == -1) {
            index1 = url.length() + 1;
        }

        if (index2 == -1) {
            index2 = url.length() + 1;
        }

        int index = Math.min(index1, index2);
        if (index < url.length()) {
            url = url.substring(0, index);
        }

        index1 = url.lastIndexOf('/');
        index2 = url.lastIndexOf('\\');

        index = Math.max(index1, index2);

        url = url.substring(index + 1);

        return url;
    }
}
