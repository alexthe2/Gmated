package main;

import com.formdev.flatlaf.FlatDarculaLaf;

import view.Application;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.install();

        new Application("Fun");
    }
}
