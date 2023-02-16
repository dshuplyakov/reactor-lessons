package GroupBy;

import java.time.Duration;

import reactor.core.publisher.Flux;

/**
 * @author Dmitry Shuplyakov
 */
public class MainGroupByLimit {

    public static void main(String[] args) {
        //ограничение по времени, затем подпоток закрываетс, но создается новый подпоток
        //лямбда flatmap вызовется один раз при подписке на каждую подспоследовательность
        //
        Flux.interval(Duration.ofMillis(50))
                .groupBy(c -> c % 3 )
                .flatMap(group -> group
                                     .take(Duration.ofMillis(1000))
                                     .count()
                                     .map(c -> "group " + group.key() + " size = " + c)
                )
                .log()
                .blockLast();

    }
}
