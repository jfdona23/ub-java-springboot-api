package ub.prog3.exposer;

import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapaBAHandler {
    @SerializedName("Normalizacion")
    public Normalized normalizacion;
    @SerializedName("GeoCodificacion")
    public PlaceData geoCode;
    @SerializedName("resultado")
    public LonLat lonlat;

    public MapaBAHandler() {
        Logger logger = LoggerFactory.getLogger(MapaBAHandler.class);
		logger.info("Creando objeto MapaBAHandler");
    }

    public class Normalized {
        @SerializedName("TipoResultado")
        public String tipoResultado;
    }

    public class PlaceData {
        @SerializedName("x")
        public String xCoord;
        @SerializedName("y")
        public String yCoord;
    }

    public class LonLat {
        @SerializedName("x")
        public String lon;
        @SerializedName("y")
        public String lat;
    }
}
