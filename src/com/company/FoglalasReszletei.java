package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FoglalasReszletei {

    public final JFrame frame = new JFrame("Foglalás Megtekintése");

    private JPanel pane;
    private JTextField textField_name;
    private JTextField textField_phone;
    private JTextField textField_email;
    private JTextField textField_adress;
    private JTextField textField_date;
    private JTextField textField_time;
    private JTextField textField_count;
    private JTextArea textArea_foods;

    public FoglalasReszletei(Frame listener_frame, UserData ud, ReservationData rd) {
        frame.setContentPane(pane);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("rsc/icon.png").getImage());

        textField_name.setText(ud.name);
        textField_phone.setText(ud.mobile_number);
        textField_email.setText(ud.email);
        textField_adress.setText(ud.street);
        textField_date.setText(rd.date);
        textField_time.setText(rd.time + ":00 - " + (rd.time+1) + ":00");
        textField_count.setText(rd.count + " fő");
        int price = 0;
        for (FoodItem f:rd.foods_list){
            textArea_foods.append(f.name + ", " + f.db  + " darab, " + f.price  + "Ft\n");
            price+= f.price;
        }
        textArea_foods.append("\n>> Összesen fizetendő: " + price + "Ft <<");

        listener_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

}
