package Context;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

/**
 * @author Dmitry Shuplyakov
 */
public class Context {

    private static Logger logger = LoggerFactory.getLogger(WebClientExample.class);
    private final static WebClient webclient = WebClient.create();
    private static String forumUrl = "https://forum.littleone.ru/showthread.php?t=7891864&page=";

    public static void main2(String[] args) {
        Flux.range(0, 50)
                .flatMap(k ->
                    Mono.deferContextual(ctx ->
                         Mono.just(ctx.get("x"))
                      )
                )
                .log()
                .contextWrite(ctx -> ctx.put("x", 123))
                .blockLast();
    }

    public static void main(String[] args) {
        Flux.range(0, 1)
                .transformDeferredContextual((original, context) -> original.doOnNext(e -> {
                     logger.info("for RequestID = " + context.get("RequestID"));
                }))
                .map(x -> x)
                .log()
                .contextWrite(ctx -> ctx.put("RequestID", 123))
                .blockLast();

    }

    public static void main3(String[] args) {

        Flux.range(1, 3)
                .map(page -> forumUrl + page)
                .flatMap(k ->
                           Mono.deferContextual(ctx -> {
                               HashMap<String, Long> map = ctx.get("x");
                               map.put(k,  System.currentTimeMillis());
                               return Mono.just(k);
                           })
                )
                .log()
                .flatMap(k ->
                     Mono.deferContextual(ctx -> {
                        HashMap<String, Long> map = ctx.get("x");
                        return Mono.just(map.get(k));
                    })
                )
                .log()
                .contextWrite(ctx ->
                    ctx.put("x", new HashMap<String, Long>())
                )
                .blockLast();
    }
}
