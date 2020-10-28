package com.view.study_desgin_model.builder;

public class AshBuilder extends Builder {
    private Hero mHero = new HeroAsh();

    public void setHero(Hero hero) {
        this.mHero = hero;
    }

    @Override
    public void buildName(String name) {
        mHero.setHeroName(name);
    }

    @Override
    public void buildSex(String sex) {
        mHero.setHeroSex(sex);
    }
}
