package com.company;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainMenu {
    public final JFrame frame = new JFrame("Étterem App");
    private JPanel MainPane;
    private JButton button_menu_asztalfoglalas;
    private JButton button_menu_foglalasok;
    private JLabel label_main_cim;


    public MainMenu() {
        frame.setContentPane(MainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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
    }


}