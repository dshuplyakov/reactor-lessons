package GroupBy;

import java.time.Duration;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import static GroupBy.GroupByEvents.incomingMessages;

/**
 * @author Dmitry Shuplyakov
 */
public class GroupByEvents2 {

   //https://stackoverflow.com/questions/63838369/how-to-efficiently-split-a-single-input-flux-into-many-output-flux-based-on-a-co

    public static void main(String[] args) throws InterruptedException {

        MySubscriber<GroupByEvents.Message> mySubscriber = new MySubscriber<>();

        Flux.from(mySubscriber.pub1).subscribe(System.out::println);

        Flux.fromStream(incomingMessages())
                .delayElements(Duration.ofMillis(10))
                .log()
                .subscribe(mySubscriber);

        Thread.sleep(5000);
    }

    //идея
    /*
      - создаем своего субскрайбера, который содержит паблишеры
      - при получении сообщения - извлекаетя нужный паблишер в всего его подписчикам пересылается сообщение
      - новые подписчики подписываются на указанного паблишера из нашего субскрайбера
     */

    static class SimplePublisher<T> implements Publisher<T> {
        Subscriber<? super T> subscriber;

        @Override
        public void subscribe(Subscriber<? super T> s) {
            subscriber = s;
        }
    }

    static class MySubscriber<T> implements Subscriber<T> {

        private SimplePublisher<T> pub1 = new SimplePublisher<T>();

        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T t) {
            if (pub1.subscriber != null) {
                pub1.subscriber.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            pub1.subscriber.onError(t);
        }

        @Override
        public void onComplete() {
            pub1.subscriber.onComplete();
        }
    }

}

