package Context;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

public class WebClientExample {
    private static Logger logger = LoggerFactory.getLogger(WebClientExample.class);

    private final static WebClient webclient = WebClient.create();

    private static String forumUrl = "https://forum.littleone.ru/showthread.php?t=7891864&page=";

    public static void main(String[] args) {

        AtomicInteger number = new AtomicInteger();

        Flux.range(1, 1000)
                .map(page -> forumUrl + page)
                .flatMap(url -> {
                    long start = System.currentTimeMillis();
                    Mono<ResponseEntity<Void>> responseEntityMono =
                            webclient.get().uri(url).retrieve().toBodilessEntity();
                    return responseEntityMono.map(resp -> Tuples.of(resp, url, start));
                })
                .map(res -> {
                    if (res.getT1().getStatusCode().is2xxSuccessful()) {
                        long total = System.currentTimeMillis() - res.getT3();
                        logger.info("{}. {} - {} ms",  number.incrementAndGet(), res.getT2(),  total);
                    } else {
                        logger.info("Error while downloading  {}. Error: {}", res.getT2(), res.getT1().getBody());
                    }
                    return res;
                })
                .blockLast();
    }
}