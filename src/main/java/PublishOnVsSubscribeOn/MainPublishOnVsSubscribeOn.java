package PublishOnVsSubscribeOn;

import java.util.function.Consumer;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Dmitry Shuplyakov
 */
public class MainPublishOnVsSubscribeOn {

    public static void main(String[] args) {
        Consumer<Integer> consumer = s -> System.out.println(s + " : " + Thread.currentThread().getName());

        Flux.range(1, 5)
                .subscribeOn(Schedulers.newSingle("subscribeOn_thread"))
                .doOnNext(consumer)
                .map(i -> {
                    System.out.println("Inside map the thread is " + Thread.currentThread().getName());
                    return i * 10;
                })

                .publishOn(Schedulers.newSingle("First_PublishOn()_thread"))
                .doOnNext(consumer)
                .publishOn(Schedulers.newSingle("Second_PublishOn()_thread"))
                .doOnNext(consumer)
               // .subscribeOn(Schedulers.newSingle("subscribeOn_thread"))
                .subscribe();
    }
}
