package org.lld.browserhistory;

/**
 * BrowserHistory implementation using doubly-linked list for efficient navigation.
 * 
 * Design:
 * - Uses a doubly-linked list where each node represents a visited page
 * - Current pointer tracks the current position in history
 * - visit() creates new node and clears forward history
 * - back()/forward() move the pointer and return the URL
 * 
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n) where n is number of visited pages
 */
public class BrowserHistory {
    
    private HistoryNode current;

    /**
     * Initialize the browser history with the homepage.
     * @param homepage the initial URL to visit
     */
    public BrowserHistory(String homepage) {
        this.current = new HistoryNode(homepage);
    }
    
    /**
     * Visit a new URL. This clears any forward history.
     * @param url the URL to visit
     */
    public void visit(String url) {
        // Create new node for the visited URL
        HistoryNode newNode = new HistoryNode(url);
        
        // Link the new node after current node
        current.next = newNode;
        newNode.prev = current;
        
        // Move current pointer to the new node
        // This effectively clears forward history as current.next becomes null
        current = newNode;
    }
    
    /**
     * Go back to the previously visited page.
     * @return the URL of the previous page, or current URL if no previous page
     */
    public String back() {
        if (current.prev != null) {
            current = current.prev;
        }
        return current.url;
    }
    
    /**
     * Go forward to the next page if a back operation was done before.
     * @return the URL of the next page, or current URL if no forward history
     */
    public String forward() {
        if (current.next != null) {
            current = current.next;
        }
        return current.url;
    }
    
    /**
     * Get the current URL being viewed.
     * @return the current URL
     */
    public String getCurrentPage() {
        return current.url;
    }
}


