package org.lld.browserhistory;

public class HistoryNode {


        String url;
        HistoryNode prev;
        HistoryNode next;

        HistoryNode(String url) {
            this.url = url;
        }
}
