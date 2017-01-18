package nl.dke.boardgame.util;

/**
 * Created by nik on 1/18/17.
 */
public class IntegerCounter
    extends Counter
{
    private int i;

    public IntegerCounter(String name)
    {
        super(name);
        i = 0;
    }

    @Override
    public Number getValue()
    {
        return i;
    }

    @Override
    public <E extends Number> void increment(E e)
    {
        i += e.intValue();
    }
}
