package main;

import com.formdev.flatlaf.FlatDarculaLaf;

import controller.Controller;
import view.Application;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.install();

        new Controller();
        new Application("Fun");
    }
}
