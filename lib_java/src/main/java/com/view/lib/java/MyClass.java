package com.view.lib.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class MyClass {
    public static void main(String[] args) {
        String file = "E:\\a.TXT";
        File writeFile = new File("E:\\a1.TXT");
        readFile(file, writeFile);
    }

    public static void readFile(String filePath, File writeFile) {
        try {
            Reader fileReader = new FileReader(filePath);
            FileWriter fileWriter = new FileWriter(writeFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile),"UTF-8"));
            String string = null;
            while ((string = bufferedReader.readLine()) != null) {
                System.out.println(string);
                if (checkNotes(string)) {
                    bufferedWriter.write(string);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkNotes(String lineContant) {
        String trim = lineContant.trim();
        return !trim.startsWith("//") && !trim.startsWith("/*") && !trim.startsWith("*") && !trim.startsWith("*/");
    }
}