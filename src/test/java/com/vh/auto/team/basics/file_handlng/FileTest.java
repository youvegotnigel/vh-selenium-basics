package com.vh.auto.team.basics.file_handlng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {

    public static void main(String[] args) {


//        Path filePath = Paths.get(System.getProperty("user.dir"), "src");
//
//                System.getProperty("user.dir") + File.separator + "src\\test\\java\\com\\vh\\auto\\team\\basics\\file_handlng\\file.txt";
//
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                //System.out.println(line); // Print each line of the file
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        final Path EXCEL_FILE_PATH = Paths.get(System.getProperty("user.dir"), "data-files", "patient-questionnaire");

        System.out.println(EXCEL_FILE_PATH);

    }



}
