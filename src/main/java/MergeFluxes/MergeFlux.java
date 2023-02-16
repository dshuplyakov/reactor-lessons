package MergeFluxes;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Dmitry Shuplyakov
 */
public class MergeFlux {

    public static void main(String[] args) {

        Flux<String> flux1 = Flux.interval(Duration.ofMillis(50)).take(10).map(e -> "1 seq: " + e);
        Flux<String> flux2 = Flux.interval(Duration.ofMillis(40)).take(10).map(e -> "2 seq: " + e);
        Flux<String> flux3 = Flux.interval(Duration.ofMillis(30)).take(10).map(e -> "3 seq: " + e);

        //поочередное объединение источников
        //последовательности запускаются по очереди
        Flux.concat(
                flux1,
                flux2,
                flux3
        );

        //объединение в порядке получения элементов из последовательностей
        Flux.merge(
                flux1,
                flux2,
                flux3
        );

        //первый элементы объедняются черзе Tuple
        Flux.zip(
                flux1,
                flux2,
                flux3
        );

        //объединяется самые свежие элементы
        Flux.combineLatest(
                flux1,
                flux2,
                (x1, x2) -> {
                    return x1+x2;
                }
        );

        //похоже на concat но последотвательности запускаются сразу
        Flux.mergeSequential(
                flux1,
                flux2,
                flux3
        );

        //Этот вариант задержит любую ошибку до тех пор, пока остальная часть последовательности не будет обработана.
        Flux.mergeDelayError(1,
                Mono.error(new RuntimeException()),
                flux1);
    }
}
