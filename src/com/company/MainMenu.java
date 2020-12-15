package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

public class MainMenu {
    public final JFrame frame = new JFrame("Étterem App");
    private JPanel MainPane;
    private JButton button_menu_asztalfoglalas;
    private JButton button_menu_foglalasok;


    public MainMenu() {
        frame.setContentPane(MainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("rsc/icon.png").getImage());

        button_menu_asztalfoglalas.addActionListener(e -> {
            AsztalFoglalas asztalFoglalas = new AsztalFoglalas();
            frame.setVisible(false);
            asztalFoglalas.frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    frame.setVisible(true);
                    e.getWindow().dispose();
                }
            });
        });

        button_menu_foglalasok.addActionListener(e -> {
            FoglalasokMegtekintese foglalasokMegtekintese = new FoglalasokMegtekintese();
            frame.setVisible(false);
            foglalasokMegtekintese.frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                    frame.setVisible(true);
                }
            });
        });
    }

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();

        try {
            File myObj = new File("save.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("users.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}