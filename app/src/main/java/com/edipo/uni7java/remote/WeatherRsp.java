package com.edipo.uni7java.remote;

import java.util.List;

public class WeatherRsp {

    public Main main;
    public List<Weather> weather;

    public static class Main {

        public int temp;

    }

    public static class Weather {

        public String description;
        public String icon;

    }

}
