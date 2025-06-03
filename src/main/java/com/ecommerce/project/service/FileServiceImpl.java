package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {

        String fileName = image.getOriginalFilename();

        String randomUUID = UUID.randomUUID().toString();

        assert fileName != null;
        String newFileName = randomUUID.concat(fileName.substring(fileName.lastIndexOf(".")));

        String newFilePath = path + File.separator + newFileName;

        File folder = new File(path);

        if(!folder.exists()){
            folder.mkdirs();
        }

        Files.copy(image.getInputStream(), Paths.get(newFilePath));
        return newFileName;
    }
}
