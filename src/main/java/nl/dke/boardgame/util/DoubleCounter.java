package nl.dke.boardgame.util;

/**
 * Created by nik on 1/18/17.
 */
public class DoubleCounter
    extends Counter
{
    private double d;

    public DoubleCounter(String name)
    {
        super(name);
        d = 0;
    }

    @Override
    public Number getValue()
    {
        return d;
    }

    @Override
    public <E extends Number> void increment(E e)
    {
        d += e.doubleValue();
    }
}
