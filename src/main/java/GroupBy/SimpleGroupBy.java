package GroupBy;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

/**
 * @author Dmitry Shuplyakov
 */
public class SimpleGroupBy {

    public static void main(String[] args) throws InterruptedException {

        Flux.range(1, 7)
                .groupBy(a -> a % 2 == 0 ? "odd" : "not")
                .flatMap(
                      x -> x.map(String::valueOf).startWith(x.key()).collectList()
                );

        Flux.just("Hello", "world")
                .map(String::toUpperCase)
                .flatMap(s -> Flux.fromArray(s.split("")))
                .groupBy(String::toString)
                .flatMap(x -> x.collectList())
                .map(l -> Tuples.of(l.get(0), l.size()));

        Flux.just("Hello", "world")
                .map(String::toUpperCase)
                .flatMap(s -> Flux.fromArray(s.split("")))
                .groupBy(String::toString)
                .flatMap(
                        group -> group
                                .count()
                                .map(cnt -> Tuples.of(group.key(), cnt))
                )
                .log();
             //   .blockLast();



        Flux.range(1, 7000)
                .delayElements(Duration.ofMillis(10))
                .groupBy(v -> v.intValue() % 2)
                .flatMap(group -> group
                        .take(Duration.ofMillis(1000))
                        .count()
                        .map(c -> "group " + group.key() + " size = " + c)
                )
                .log();
              //  .blockLast();
    }
}
