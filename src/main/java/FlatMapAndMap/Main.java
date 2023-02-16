package FlatMapAndMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;

/**
 * @author Dmitry Shuplyakov
 */
public class Main {
    public static void main(String[] args) {

        List<String> first = Arrays.asList("a", "b", "c", "d", "e", "f");

        Flux<String> second = Flux.just("1", "2", "3");

        Flux.fromIterable(first)
                .flatMap( f -> {
                    return second.map(s -> s + f);
                })
                .collect(Collectors.toList())
                .subscribe(System.out::println);
    }
}
