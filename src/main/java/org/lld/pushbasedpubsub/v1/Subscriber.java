package org.lld.pushbasedpubsub.v1;

public interface Subscriber {

    void receive(String message);
}
