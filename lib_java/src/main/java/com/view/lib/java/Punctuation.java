package com.view.lib.java;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by GuoXu on 2020/11/12 10:28.
 */
public class Punctuation {

    private static String content = "Google's," +
            " Java! " +
            " sample " +
            "code " +
            "that " +
            "comes, " +
            "code " + "code ";

    private static String[] words = new String[]{"Google's", "Java", "sample", "code", "that", "comes", "code", "code"};
    private static ArrayList<Integer> indexs = new ArrayList<Integer>();

    public static void main(String[] args) {
//        findWordindexs4Content();
        testArrayList();
    }

    private static ArrayList<Demo> demos = new ArrayList<>();

    private static void testArrayList() {
        for (int i = 0; i < 10; i++) {
            Demo demo = new Demo("demo" + i, i);
            demos.add(demo);
        }
        boolean demo5 = demos.contains("demo5");
        System.out.println("demo5 " + demo5);
    }



    private static void findWordindexs4Content() {

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int i1 = content.indexOf(word);
            if (indexs.contains(i1)) {
                i1 = content.indexOf(word, indexs.get(i - 1) + 1);
            }
            if (i1 > -1) {
                indexs.add(i1);
            }

            System.out.println("indexs " + indexs.get(i));
        }
    }

    //使用UnicodeScript方法判断
    public static boolean isChineseByScript(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }
}
