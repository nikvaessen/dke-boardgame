package nl.dke.boardgame.util;

/**
 * Interface to attach objects to a class. Every object which gets attached should have an unique string
 * identifying it
 */
public interface Attachable<E>
{
    boolean detach(String name);

    void attach(E e);

    boolean has(String name);

    E getAttachable(String name);
}
