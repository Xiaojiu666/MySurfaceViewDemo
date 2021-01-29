package com.view.lib.java;

/**
 * Created by GuoXu on 2020/11/20 11:38.
 */
public class Demo {
    public Demo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return getName().equals((String) obj);
        }
        return super.equals(obj);
    }
}
