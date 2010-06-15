package main;

/**
 * @author nils
 *
 * @version 1.0
 */
public class Pair<FST, SND> {

    public FST first;
    public SND second;

    public Pair(FST first, SND second) {
        this.first = first;
        this.second = second;
    }
}
