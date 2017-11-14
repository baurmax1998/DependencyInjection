package de.cherry.test;


import de.cherry.myLib.annotations.Inject;
import de.cherry.myLib.interfaces.*;

/**
 * Created by mbaaxur on 10.11.17.
 */
public class TestToFinde implements Main {

    @Inject
    private Person person;

    @Inject
    private SomeInterface testWorker;


    public void main() {
        System.out.println("hallo");

        System.out.println(testWorker.getHallo());

        System.out.println(person.getName());
    }
}
