package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;


public class Main {
    static private TreeMap<String,String> dirNameAndDirPath =new TreeMap<String, String>();
    static private String mergerFileName="./example.txt";


    public static void createMergFile(){
        try {

            File f = new File(mergerFileName);

            if (f.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }      //создаем файл в котором будет находиться весь текст


    public static void mergAllFile() throws IOException {

    for(Map.Entry<String,String> entry : dirNameAndDirPath.entrySet()) {

        String value = entry.getValue();
        String textFromDir = " ";


        RandomAccessFile aFile = new RandomAccessFile(value, "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            while(buf.hasRemaining()){
                textFromDir=textFromDir+ (char) buf.get();
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();

        //System.out.println(a);


        try {
            Files.write(Paths.get(mergerFileName), textFromDir.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

}   //в общий файл добавляем текст из всех файлов

    public static void findAllFile() throws IOException {   //ищем все файлы в директориях и добавляем их расположение в TreeMap(в нем автоматически сортируются)



    Path path= Paths.get(".//WorkDir/");
    Files.walkFileTree(path, new FileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            dirNameAndDirPath.put(file.getFileName().toString(),file.toString());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

            return FileVisitResult.CONTINUE;
        }
    });
}


    public static void main(String[] args) throws IOException {




        findAllFile();
        createMergFile();
        mergAllFile();
        System.out.println(dirNameAndDirPath.keySet());



    }
}

