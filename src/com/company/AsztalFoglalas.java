package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

public class AsztalFoglalas {

    public final JFrame frame = new JFrame("Asztalfoglalás");

    private JPanel FoglalasPane;
    private JComboBox<String> idopont_comboBox;
    private JSpinner letszam_spinner;
    private JLabel label_idopont;
    private JLabel label_letszam;
    private JLabel label_szukseges_asztalok_int;
    private JXDatePicker date_selector;
    private JTextField name_textField;
    private JTextField phone_textField;
    private JTextField lakcim_textField;
    private JList<String> etel_list;
    private JComboBox etel_comboBox;
    private JSpinner etel_spinner;
    private JButton etel_hozzaad_button;
    private JButton etel_torles_button;
    private JButton mentes_button;
    private JButton megsem_button;
    private JLabel fizetendo_osszeg_label_int;
    private JTextField email_textField;
    private JButton search_data_button;
    DefaultListModel<String> model = new DefaultListModel<>();

    public AsztalFoglalas() {
        eventsManager();
        setUIprops();
        createUIComponents();
        frame.setContentPane(FoglalasPane);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        String[] s = {""};
        etel_list = new JList<>(s);

        ArrayList<String> etelek_listaja = new ArrayList<String>();
        for (String name : ProgramDataManager.getÉtelek().keySet()) {
            int price = ProgramDataManager.getÉtelek().get(name);
            etelek_listaja.add(name + ", " + price + " Ft");
        }
        etel_comboBox = new JComboBox(etelek_listaja.toArray());

        ArrayList<String> foglalasok_listaja = new ArrayList<String>();
        for(int i = ProgramDataManager.getNyitva_innen();i < ProgramDataManager.getNyitva_eddig(); i++){
            foglalasok_listaja.add(i + ":00 - " + (i+1) + ":00");
        }
        idopont_comboBox = new JComboBox(foglalasok_listaja.toArray());
    }

    private void setUIprops() {
        date_selector.getEditor().setEditable(false);
        date_selector.getMonthView().setTodayBackground(Color.ORANGE);
        date_selector.getEditor().setValue(new Date(System.currentTimeMillis()));
        letszam_spinner.setModel(new SpinnerNumberModel(1, 1, ProgramDataManager.getAsztalok_száma() * 4, 1));
        ((JSpinner.DefaultEditor) letszam_spinner.getEditor()).getTextField().setEditable(false);
        etel_spinner.setModel(new SpinnerNumberModel(1, 1, 20, 1));
        ((JSpinner.DefaultEditor) etel_spinner.getEditor()).getTextField().setEditable(false);
    }

    private void eventsManager() {

        letszam_spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int db = (Integer)letszam_spinner.getValue();
                double db2 = db;
                db2=db2/4+0.75;
                label_szukseges_asztalok_int.setText((int)db2 + " db");
            }
        });

        megsem_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        etel_hozzaad_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> model = new DefaultListModel<>();

                System.out.println(String.valueOf(etel_comboBox.getSelectedIndex()));

            }
        });


    }


}