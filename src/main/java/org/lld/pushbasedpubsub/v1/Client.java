package org.lld.pushbasedpubsub.v1;

public class Client {


    public static void main(String[] args) {

        ConcreteSubscriber sub1 = new ConcreteSubscriber("sub1");
        ConcreteSubscriber sub2 = new ConcreteSubscriber("sub2");

        Publisher pub1 = new Publisher("pub1");

        pub1.subscribe(sub1);
        pub1.subscribe(sub2);

        pub1.publish("hello");

    }


}
