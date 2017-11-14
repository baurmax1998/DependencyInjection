package de.cherry.myLib;

/**
 * Created by mbaaxur on 14.11.17.
 */
public class DefaultMain {
    public static void main(String[] args) throws ClassNotFoundException {
        App app = new App(args[0]);
        app.init();
        app.start(false);

    }
}
