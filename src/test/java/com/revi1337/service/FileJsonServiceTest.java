package com.revi1337.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


@Disabled
class FileJsonServiceTest {

    @Test
    public void fileDownLoadTest() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI("https://github.com/CVEProject/cvelistV5/archive/refs/heads/main.zip"))
                .GET()
                .build();
    }

    @Test
    public void extractingZipFileTest() {
        String destDirectory = "C:\\Users\\revi1\\Desktop\\result";
        Path path = Path.of("C:\\Users\\revi1\\Desktop\\cvelistV5-main.zip");
        File file = path.toFile();

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                InputStream inputStream = zipFile.getInputStream(entry);
                String destPath = destDirectory + File.separator + entryName;

                if (entry.isDirectory()) {
                    File dir = new File(destPath);
                    dir.mkdirs();
                } else {
                    FileOutputStream outputStream = new FileOutputStream(destPath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                }

                inputStream.close();
            }
            System.out.println("Zip 파일 추출이 완료되었습니다.");
        } catch (IOException exception) {
            System.out.println("exception = " + exception);
        }
    }

    @Test
    public void readDirectoryRecursiveTest() {
        String zipFolder = "C:\\Users\\revi1\\Desktop\\result\\cvelistV5-main";
        List<File> jsonFiles = findJsonFiles(zipFolder);

//        for (File jsonFile : jsonFiles) {
//            System.out.println(jsonFile.getAbsolutePath());
//        }
        System.out.println("jsonFiles.size() = " + jsonFiles.size());
    }

    @Test
    public void readAllJsonFiles() {
        String zipFolder = "C:\\Users\\revi1\\Desktop\\result\\cvelistV5-main";
        List<File> jsonFiles = findJsonFiles(zipFolder);
    }

    public static List<File> findJsonFiles(String directoryPath) {
        List<File> jsonFiles = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory())
            searchJsonFiles(directory, jsonFiles);
        else System.out.println("Invalid Directory Path");
        return jsonFiles;
    }

    private static void searchJsonFiles(File directory, List<File> jsonFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchJsonFiles(file, jsonFiles);
                } else if (file.isFile() && file.getName().endsWith(".json")) {
                    if (!file.getName().startsWith("recent_activities")) {
                        jsonFiles.add(file);
                    }
                }
            }
        }
    }

}