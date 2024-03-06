package com.dovantuan.lab1_ph31763;

import java.util.List;

public class City {
    private String name;
    private String state;
    private String country;
    private boolean capital;
    private int population;
    private List<String> regions;

    public City() {
    }

    public City(String name, String state, String country, boolean capital, int population, List<String> regions) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.capital = capital;
        this.population = population;
        this.regions = regions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }
}
