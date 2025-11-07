package org.lld.browserhistory;

/**
 * Demo class to test BrowserHistory functionality with the provided example.
 */
public class BrowserHistoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Browser History Demo ===");
        
        // Initialize with homepage
        BrowserHistory bh = new BrowserHistory("youtube.com");
        System.out.println("Initial page: " + bh.getCurrentPage());
        
        // Visit new pages
        bh.visit("google.com");
        System.out.println("After visiting google.com: " + bh.getCurrentPage());
        
        bh.visit("github.com");
        System.out.println("After visiting github.com: " + bh.getCurrentPage());
        
        String backResult = bh.back();
        System.out.println("After back(): " + backResult + " (current: " + bh.getCurrentPage() + ")");
        
        String forwardResult = bh.forward();
        System.out.println("After forward(): " + forwardResult + " (current: " + bh.getCurrentPage() + ")");
        
        // Visit new page (clears forward history)
        bh.visit("linkedin.com");
        System.out.println("After visiting linkedin.com: " + bh.getCurrentPage());
        
        // Test forward when no forward history
        String forwardResult2 = bh.forward();
        System.out.println("After forward() (no forward history): " + forwardResult2 + " (current: " + bh.getCurrentPage() + ")");
        
        // Test back navigation
        String backResult2 = bh.back();
        System.out.println("After back(): " + backResult2 + " (current: " + bh.getCurrentPage() + ")");
        
        // Get current page
        System.out.println("Current page: " + bh.getCurrentPage());

    }

}


