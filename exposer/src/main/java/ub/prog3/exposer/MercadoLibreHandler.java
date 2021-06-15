package ub.prog3.exposer;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MercadoLibreHandler {
    public List<ResultBlock> results;

    public MercadoLibreHandler() {
        Logger logger = LoggerFactory.getLogger(MercadoLibreHandler.class);
		logger.info("Creando objeto MercadoLibreHandler");
    }

    public class ResultBlock {
        public String title;
        public String price;
        @SerializedName("permalink")
        public String itemUrl;
        @SerializedName("thumbnail")
        public String itemImgUrl;
    }
}
