package ub.prog3.exposer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;


public class ApiCaller {

	public String requestUrl(String url) {

		Logger logger = LoggerFactory.getLogger(ExposerApplication.class);
		logger.info("Obteniendo data de : " + url);

		CVurl request = new CVurl();
		Response<String> response;

		response = request.get(url)
			.asString()
			.orElseThrow(RuntimeException::new);
        
		logger.debug("CVurl GET: " + response.getBody());

		return response.getBody();
	}

}
