package ub.prog3.exposer;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ub.prog3.exposer.MercadoLibreHandler.ResultBlock;

@CrossOrigin("*")
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
public class ExposerController {

    private final long cacheExpirationSeconds = 7200L;

    @Autowired
    DataRetriever dr;

    private final ResponsesRepository repository;

    public ExposerController(ResponsesRepository repository) {
        this.repository = repository;
    }

    @GetMapping("*")
    public String defaultRoute(HttpServletResponse response) {
        response.addHeader("x-wrong-route", "true");
        return "{\"msg\": \"Ruta incorrecta\"}";
    }

    @GetMapping("/search")
    public String getPlaceData(@RequestParam String calle, @RequestParam String altura, HttpServletResponse response) throws UnsupportedEncodingException {
        Logger logger = LoggerFactory.getLogger(ExposerController.class);

        // Verificar cache por respuesta existente
        logger.info("Verificando existencias en Cache");

        String query = calle+"+"+altura; 
        Optional<ResponsesCache> isQuery = repository.findById(query);
        if (isQuery.isPresent()) {
            long timeNow = Instant.now().getEpochSecond();
            if ((timeNow - isQuery.get().getEpochTimestamp()) <= cacheExpirationSeconds) {
                logger.info("Respuesta hallada en Cache");
                response.addHeader("x-cache-hit", "true");
                return isQuery.get().getResponse();
            } else {
                logger.info("Respuesta en Cache expirada. Eliminando.");
                repository.deleteById(query);
            }
        }

        // Obtener las coordenadas GKBA a partir de una direccion
        MapaBAHandler coordsGkba = dr.normalizerRequest(calle, altura);

        // Validar la respuesta. Verificar direccion
        logger.info("Verificando dirección válida");
        if (!dr.respuestaValida(coordsGkba)) {
            logger.info("Dirección inválida");
            ResponseHandler finalResponse = new ResponseHandler();
            finalResponse.tipoResultado = coordsGkba.normalizacion.tipoResultado;
            Gson gson = new Gson();
            String json = gson.toJson(finalResponse);
            return json;
        }
        logger.info("Dirección validada correctamente");

        // Convertir las coordenadas GKBA a Lon/Lat
        MapaBAHandler coords = dr.coorConverterRequest(coordsGkba.geoCode.xCoord, coordsGkba.geoCode.yCoord);

        // Obtener los datos del clima para las coordenadas
        OpenWeatherHandler weather = dr.openWeatherRequest(coords.lonlat.lat, coords.lonlat.lon);

        // Obtener el producto recomendado
        HashMap<String, String> climaProducto = new HashMap<String, String>();
        climaProducto.put("clear sky", "remera algodon");
        climaProducto.put("few clouds", "jersey");
        climaProducto.put("scattered clouds", "buzo");
        climaProducto.put("broken clouds", "campera");
        climaProducto.put("overcast clouds", "campera");
        climaProducto.put("shower rain", "piloto de lluvia");
        climaProducto.put("rain", "paraguas");
        climaProducto.put("thunderstorm", "paraguas y botas de lluvia");
        climaProducto.put("snow", "botas de nieve");
        climaProducto.put("mist", "cuello polar");
        climaProducto.put("fog", "cuello polar");

        String climaActual = weather.weather.get(0).description;
        String productoRecomendado = climaProducto.getOrDefault(climaActual, "zapatillas urbanas");
        logger.info("Recomendando producto para el clima: "+climaActual);

        ResultBlock producto = dr.mercadoLibreRequest(productoRecomendado);
        
        // Obtener imagen de la porcion del mapa solicitada
        logger.info("Generando la imagen del mapa");

        String urlMapImg = "http://servicios.usig.buenosaires.gov.ar/LocDir/mapa.phtml?x="+coordsGkba.geoCode.xCoord+"&y="+coordsGkba.geoCode.yCoord+"&h=1024&w=1024&punto=1&r=500";

        // Construir la respuesta final
        logger.info("Construyendo la respuesta final");

        ResponseHandler finalResponse = new ResponseHandler();
        finalResponse.tipoResultado = coordsGkba.normalizacion.tipoResultado;
        finalResponse.longitud = coords.lonlat.lon;
        finalResponse.latitud = coords.lonlat.lat;
        finalResponse.temp = weather.main.feelsLike;
        finalResponse.tempMin = weather.main.tempMin;
        finalResponse.tempMax = weather.main.tempMax;
        finalResponse.presion = weather.main.pressure;
        finalResponse.humedad = weather.main.humidity;
        finalResponse.urlMapa = urlMapImg;
        finalResponse.productoTitulo = producto.title;
        finalResponse.productoPrecio = producto.price;
        finalResponse.productoUrl = producto.itemUrl;
        finalResponse.productoImg = producto.itemImgUrl;

        Gson gson = new Gson();
        String json = gson.toJson(finalResponse);

        // Cachear la respuesta
        logger.info("Guardando respuesta en el Cache");

        ResponsesCache toCache = new ResponsesCache();
        toCache.setAddress(query);
        toCache.setResponse(json);
        repository.save(toCache);

        return json;
    }
}
