package ub.prog3.exposer;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenWeatherHandler {
    public MainBlock main;
    public List<Weather> weather;

    public OpenWeatherHandler() {
        Logger logger = LoggerFactory.getLogger(OpenWeatherHandler.class);
		logger.info("Creando objeto OpenWeatherHandler");
    }

    public class MainBlock {
        public float temp;
        @SerializedName("feels_like")
        public float feelsLike;
        @SerializedName("temp_min")
        public float tempMin;
        @SerializedName("temp_max")
        public float tempMax;
        public float pressure;
        public float humidity;    
    }

    public class Weather {
        public String description;
    }
}
