package CreateFluxAndMono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import reactor.core.publisher.Flux;

/**
 * @author Dmitry Shuplyakov
 */
public class CreatingFlux {

    public static void main(String[] args) {

        Flux.just(1, 2, 3, 5);

        Flux.range(100, 10);

        Flux.fromIterable(Arrays.asList(1, 2));

        Flux.range(100, 40).repeat();

        Flux.interval(Duration.ofMillis(100));

        Flux.create(emitter -> {
            Random rnd = new Random();
            for (int i = 0; i <= 10; i++) {
                emitter.next(rnd.nextDouble());
            }
            emitter.complete();

            //emitter.error();
        });

       /*
        Flux.generate

        Flux.create

        Flux.push
        */
    }
}