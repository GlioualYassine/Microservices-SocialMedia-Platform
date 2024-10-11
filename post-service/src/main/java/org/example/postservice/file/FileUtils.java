package org.example.postservice.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Slf4j
public class FileUtils {
    public static String  readFileFromLocation(String fileUrl){
        if(StringUtils.isBlank(fileUrl)){
            return null ;
        }
        try{
            Path filePath = new File(fileUrl).toPath();
            //return Files.readAllBytes(filePath);
            byte[] fileBytes = Files.readAllBytes(filePath);
            return Base64.getEncoder().encodeToString(fileBytes);
        }
        catch (IOException e){
            log.warn("No file found in the path {}",fileUrl);
        }
        return  null ;
    }
}
