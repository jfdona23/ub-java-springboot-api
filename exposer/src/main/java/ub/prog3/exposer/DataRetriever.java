package ub.prog3.exposer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ub.prog3.exposer.MercadoLibreHandler.ResultBlock;

@Service
public class DataRetriever {

    @Value("${openweather.key}")
    private String owApiKey;

    public OpenWeatherHandler openWeatherRequest(String lat, String lon) {
        Logger logger = LoggerFactory.getLogger(DataRetriever.class);
		logger.info("Obteniendo informacion del clima para las coordenadas: Lat: "+lat+" Lon: "+lon);

        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid="+owApiKey;

        ApiCaller request = new ApiCaller(); 
		String response = new String();
		Gson gson = new Gson();

		response = request.requestUrl(url);
		OpenWeatherHandler ow = gson.fromJson(response, OpenWeatherHandler.class);

        return ow;
    }

    public ResultBlock mercadoLibreRequest(String buscar) throws UnsupportedEncodingException {
        Logger logger = LoggerFactory.getLogger(DataRetriever.class);
		logger.info("Obteniendo informacion de Mercado Libre");

        String encodedQuery = URLEncoder.encode(buscar, StandardCharsets.UTF_8.toString());
        String url ="https://api.mercadolibre.com/sites/MLA/search?q="+encodedQuery;

        ApiCaller request = new ApiCaller();
        String response = new String();
		Gson gson = new Gson();

        response = request.requestUrl(url);
        MercadoLibreHandler ml = gson.fromJson(response, MercadoLibreHandler.class);
        ResultBlock firstProduct = ml.results.get(0);
        
        return firstProduct;
    }

    public MapaBAHandler normalizerRequest(String calle, String altura) throws UnsupportedEncodingException {
        Logger logger = LoggerFactory.getLogger(DataRetriever.class);
        logger.info("Obteniendo las coordenadas GKBA para "+calle+" "+altura);

        String encodedQuery = URLEncoder.encode(calle, StandardCharsets.UTF_8.toString());
        String url ="https://ws.usig.buenosaires.gob.ar/rest/normalizar_y_geocodificar_direcciones?calle="+encodedQuery+"&altura="+altura+"&desambiguar=1";

        ApiCaller request = new ApiCaller();
        String response = new String();
		Gson gson = new Gson();

        response = request.requestUrl(url);
        
        return gson.fromJson(response, MapaBAHandler.class);
    }

    public MapaBAHandler coorConverterRequest(String xCoord, String yCoord) throws UnsupportedEncodingException {
        Logger logger = LoggerFactory.getLogger(DataRetriever.class);
        logger.info("Convirtiendo las coordenadas a Lon/Lat");

        String url = "https://ws.usig.buenosaires.gob.ar/rest/convertir_coordenadas?x="+xCoord+"&y="+yCoord+"&output=lonlat";
        
        ApiCaller request = new ApiCaller();
        String response = new String();
		Gson gson = new Gson();

        response = request.requestUrl(url);
        
        return gson.fromJson(response, MapaBAHandler.class);
    }

    public Boolean respuestaValida(MapaBAHandler respuesta) {
        String tipoResultado = respuesta.normalizacion.tipoResultado;

        return tipoResultado.equals("DireccionNormalizada") ? true : false;
    }
}
