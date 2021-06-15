package ub.prog3.exposer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResponseHandler {
    public String tipoResultado;
    public String longitud;
    public String latitud;
    public Float temp;
    public Float tempMin;
    public Float tempMax;
    public Float presion;
    public Float humedad;
    public String urlMapa;
    public String productoUrl;
    public String productoImg;
    public String productoTitulo;
    public String productoPrecio;

    public ResponseHandler() {
        Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
		logger.info("Creando objeto ResponseHandler");
    }
}