package nl.dke.boardgame.util;

/**
 * Created by nik on 1/18/17.
 */
public abstract class Counter
{
    private String name;

    public Counter(String name)
    {
        this.name = name;
    }

    public abstract Number getValue();

    public abstract <E extends Number> void increment(E e);

    public String toString()
    {
        return name;
    }
}
