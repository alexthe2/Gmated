package main;

import com.formdev.flatlaf.FlatDarculaLaf;

import controller.Controller;
import lombok.SneakyThrows;
import view.Application;

import java.io.File;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        FlatDarculaLaf.install();

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("music.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();

        new Controller();
        new Application("Save The Idiot King");
    }
}
