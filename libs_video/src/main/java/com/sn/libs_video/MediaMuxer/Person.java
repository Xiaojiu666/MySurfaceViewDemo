package com.sn.libs_video.MediaMuxer;

public class Person {

    public static int personCount;

    private int personAge;

    private String personName;

    public  Person(int personAge, String personName) {
        this.personAge = personAge;
        this.personName = personName;
        personCount++;
    }

    static {

    }

}
