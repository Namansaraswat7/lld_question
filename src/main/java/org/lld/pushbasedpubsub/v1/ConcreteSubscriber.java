package org.lld.pushbasedpubsub.v1;

public class ConcreteSubscriber implements Subscriber{

    private String name;

    public ConcreteSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void receive(String message) {

        System.out.println("Message received " + message + " by subscriber " + name);

    }
}
