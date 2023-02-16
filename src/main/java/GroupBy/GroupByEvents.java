package GroupBy;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Dmitry Shuplyakov
 */
public class GroupByEvents {

    /*
    - groupBy генерируется последоватлеьности GroupedFlux, которые запускаются автоматически
    - count срабатывает когда послед завершилась
    - flatMap к groupBy преобразует подпоследовательности в одну последовательность из событий
    - Flux<GroupedFlux1<Msg>, GroupedFlux1<Msg>> => Flux<Msg>
    - unicastprocessor which can be consumed by only one subscriber.
     */

    public static void main(String[] args) {
        Flux.fromStream(incomingMessages())
                .delayElements(Duration.ofMillis(10))
                .groupBy(Message::getEntityId)
                .flatMap(g -> g.publishOn(Schedulers.newParallel("groupByPool"))
                        .doOnNext(k -> processMessage(k)))
                //.log()
                .blockLast();
    }

    public static Stream<Message> incomingMessages() {
        return IntStream.range(0, 10000).mapToObj(i -> new Message(i, i % 10));
    }

    public static void processMessage(Message message) {
        System.out.println(String.format("Message: %s processed by the thread: %s", message, Thread.currentThread().getName()));
    }

    public static class Message {
        private final int id;
        private final int entityId;

        public Message(int id, int entityId) {
            this.id = id;
            this.entityId = entityId;
        }

        public int getId() {
            return id;
        }

        public int getEntityId() {
            return entityId;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", entityId=" + entityId +
                    '}';
        }
    }


}

