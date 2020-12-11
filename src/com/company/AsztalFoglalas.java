package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AsztalFoglalas {

    public final JFrame frame = new JFrame("Asztalfoglalás");

    private int osszesOsszeg=0;
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
    private JComboBox<String> etel_comboBox;
    private JSpinner etel_spinner;
    private JButton etel_hozzaad_button;
    private JButton etel_torles_button;
    private JButton mentes_button;
    private JButton megsem_button;
    private JLabel fizetendo_osszeg_label_int;
    private JTextField email_textField;
    private JButton search_data_button;
    private JScrollPane scrollpane;

    public AsztalFoglalas() {
        setUIprops();
        createUIComponents();
        eventsManager();
        frame.setContentPane(FoglalasPane);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        DefaultListModel<String> model = new DefaultListModel<>();
        etel_list = new JList<>(model);

        ArrayList<String> etelek_listaja = new ArrayList<String>();
        for (String name : ProgramDataManager.getÉtelek().keySet()) {
            int price = ProgramDataManager.getÉtelek().get(name);
            etelek_listaja.add(name + ", " + price + " Ft");
        }
        etel_comboBox = new JComboBox(etelek_listaja.toArray());
        etel_comboBox.setSelectedIndex(0);
        etel_comboBox.setEnabled(true);
        etel_comboBox.setEditable(false);

        etel_comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String item = e.getItem().toString();
                for(int i = 0; i < etel_comboBox.getItemCount();i++){
                    if(item.equalsIgnoreCase(etel_comboBox.getItemAt(i).toString())){
                        etel_comboBox.setSelectedIndex(i);
                    }
                }
            }
        });

        ArrayList<String> foglalasok_listaja = new ArrayList<String>();
        for(int i = ProgramDataManager.getNyitva_innen();i < ProgramDataManager.getNyitva_eddig(); i++){
            foglalasok_listaja.add(i + ":00 - " + (i+1) + ":00");
        }
        idopont_comboBox = new JComboBox(foglalasok_listaja.toArray());
        idopont_comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String item = e.getItem().toString();
                for(int i = 0; i < idopont_comboBox.getItemCount();i++){
                    if(item.equalsIgnoreCase(idopont_comboBox.getItemAt(i).toString())){
                        idopont_comboBox.setSelectedIndex(i);
                    }
                }
            }
        });

        etel_hozzaad_button = new JButton();
        etel_hozzaad_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ar = 0;
                String[] getar = etel_comboBox.getSelectedItem().toString().split(",");
                for (String name : ProgramDataManager.getÉtelek().keySet()) {
                    if(name.equalsIgnoreCase(getar[0])) ar = ProgramDataManager.getÉtelek().get(name);
                }
                osszesOsszeg += ((Integer.parseInt(etel_spinner.getValue().toString())) * ar);
                fizetendo_osszeg_label_int.setText(String.valueOf(osszesOsszeg));
                model.addElement("> "+ getar[0] + "   -   " + etel_spinner.getValue() + "db    ==>>    " + ((Integer.parseInt(etel_spinner.getValue().toString())) * ar) + "Ft");
            }
        });
        scrollpane = new JScrollPane();
        scrollpane.setViewportView(etel_list);
        etel_list.setLayoutOrientation(JList.VERTICAL);

        etel_list.setSelectionModel(new DefaultListSelectionModel());


        etel_torles_button = new JButton();
        etel_torles_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(etel_list.getSelectedValue());
            }
        });
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



    }


}