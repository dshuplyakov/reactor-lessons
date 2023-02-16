package AggregateOperators;

import reactor.core.publisher.Flux;

/**
 * @author Dmitry Shuplyakov
 */
public class MainScan {

    public static void main(String[] args) {
        Flux.range(1, 5)
                .scan((res, elem) -> res += elem)
                .log()
                .last()
                .log()
                .block();
    }
}
