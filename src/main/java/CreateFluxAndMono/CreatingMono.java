package CreateFluxAndMono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import reactor.core.publisher.Mono;

/**
 * @author Dmitry Shuplyakov
 */
public class CreatingMono {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static CompletableFuture<HttpResponse<String>> asyncHttpRequest() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ya.ru"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void main(String[] args) {

        Mono.empty();

        Mono.justOrEmpty(Optional.empty());

        Mono.fromFuture(asyncHttpRequest());

        Mono.fromSupplier(() -> Instant.now());

        Mono.fromRunnable(() -> System.out.println("I'am runnable"));

        //Самостоятельно обернет результат коллабла в Mono в отличие от defer
        Mono.fromCallable(() -> "I'am callable");


        //Defer ожидает на вход лямбду которая вернет моно.
        // - Тут больше контроля при построении моно (ошибки/сам объект)
        // - Лямбла  выполняется лениво
        Mono<Integer> defer = Mono.defer(() -> {
            return Mono.just(new Random().nextInt());
        });

        Mono.defer(() -> {
            return  (100 % 2 == 0) ?
                Mono.just("element") :
                Mono.error(new RuntimeException());
        });




    }
}
