package de.cherry.myLib.internal;

import de.cherry.myLib.interfaces.Main;

import java.util.Scanner;

/**
 * Created by mbaaxur on 14.11.17.
 */
public class AdminUi implements Main{




    public void main() {
        Scanner scanner = new Scanner(System.in);
        boolean laeft = true;
        while (laeft){
            System.out.println("Gib etwas ein:");
            String in = scanner.nextLine();
            if (in.equals("exit") | in.equals("e"))
                laeft = false;
            else
                System.out.println("Befehl wurde nicht gefunden!");

        }

    }
}
