package com.sn.study_desgin_model.builder;

public abstract class Hero {


    public Skill heroSkill;
    private String heroName;
    private String heroSex;


    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroSex() {
        return heroSex;
    }

    public void setHeroSex(String heroSex) {
        this.heroSex = heroSex;
    }



    public abstract void setHeroSkill();

    @Override
    public String toString() {
        return "Hero{" +
                "heroSkill=" + heroSkill +
                ", heroName='" + heroName + '\'' +
                ", heroSex='" + heroSex + '\'' +
                '}';
    }
}
