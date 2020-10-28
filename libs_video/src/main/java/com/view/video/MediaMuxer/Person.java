package com.view.video.MediaMuxer;

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
