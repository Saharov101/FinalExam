package observer.subject;

import observer.observers.InventoryObserver;

/**
 * Observer pattern - Subject interface.
 */
public interface InventorySubject {

    void attach(InventoryObserver observer);

    void detach(InventoryObserver observer);
}
