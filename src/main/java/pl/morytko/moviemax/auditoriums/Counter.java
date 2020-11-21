package pl.morytko.moviemax.auditoriums;

public class Counter {
    private int count;

    public Counter()
    {
        count = 0;
    }

    public Counter(int init)
    {
        count = init;
    }

    public int get()
    {
        return count;
    }

    public void clear()
    {
        count = 0;
    }

    public int getAndIncrement()
    {
        return count++;
    }

    public String toString()
    {
        return ""+count;
    }
}
