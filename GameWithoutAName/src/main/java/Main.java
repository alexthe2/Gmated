import com.formdev.flatlaf.FlatDarculaLaf;

import controller.Controller;
import lombok.SneakyThrows;
import view.Application;

import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        FlatDarculaLaf.install();

        JOptionPane.showMessageDialog(null, "Welcome to STIK (Save the Idiot King), soon you will be greeted by a nice dialog in which you should choose your configuration File.\n" +
                "If the game happens to crash, remember 99% of bugs sit between monitor rand chair, so check your config File, or paypal the programmers a coffee and they might look into it", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("music.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();

        new Controller();
        new Application("Save The Idiot King");
    }
}
