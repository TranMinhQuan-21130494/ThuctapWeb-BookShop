package com.example.bookshopwebapplication.utils;


import lombok.SneakyThrows;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class ImageUtils {
    private static final String IMAGES_DIR = "/image";
    public static Optional<String> upload(HttpServletRequest request) {
        Optional<String> imageName = Optional.empty();
        try {
            Part filePart = request.getPart("image");
            ServletContext context = request.getServletContext();

            // Get the real path of the folder
            String realPath = context.getRealPath(IMAGES_DIR);

            // Create a File object for the folder
            Path targetImg = Paths.get(realPath);

            if (filePart.getSize() != 0 && filePart.getContentType().startsWith("image")) {
                Path targetLocation = Files.createTempFile(targetImg, "img-", ".jpg");
                try (InputStream fileContent = new BufferedInputStream(filePart.getInputStream())) {
                    Files.copy(fileContent, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                }
                imageName = Optional.of(targetLocation.getFileName().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageName;
    }

    @SneakyThrows
    public static void delete(String imageName, HttpServletRequest request) {
        Path imagePath = Paths.get(request.getServletContext().getRealPath(IMAGES_DIR))
                .resolve(imageName).normalize();
        boolean result = Files.deleteIfExists(imagePath);
        if (result) {
            System.out.println("File is deleted: " + imageName);
        } else {
            System.out.println("Sorry, unable to delete the file: " + imageName);
        }
    }
}
