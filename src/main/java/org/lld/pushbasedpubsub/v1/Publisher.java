package org.lld.pushbasedpubsub.v1;

import java.util.ArrayList;
import java.util.List;

public class Publisher {

    private String name;

    List<Subscriber> subscriberList;

    void subscribe(Subscriber subscriber) {
        subscriberList.add(subscriber);
    }

    void removeSubscriber(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    public Publisher(String name) {
        this.subscriberList = new ArrayList<>();
        this.name = name;
    }

    void publish(String message) {
        for(Subscriber subscriber: subscriberList) {
            subscriber.receive(message);
        }
    }
}
