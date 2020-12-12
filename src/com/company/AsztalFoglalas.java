package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
    private JComboBox<String> etel_comboBox;
    private JSpinner etel_spinner;
    private JButton etel_hozzaad_button;
    private JButton etel_torles_button;
    private JButton mentes_button;
    private JButton megsem_button;
    private JLabel fizetendo_osszeg_label_int;
    private JTextField email_textField;
    private JButton search_data_button;
    private JTable etel_table;
    private JScrollPane table_scrollpane;

    public AsztalFoglalas() {
        setUIcomponents();
        frame.setContentPane(FoglalasPane);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        //Ételek hozzáadása
        addFoodItems();

        //Az ételek kezelésére szolgáló részek
        String[] fejlec = {"ID", "Étel neve", "Darabszám", "Ár"};

        DefaultTableModel tmodel = new DefaultTableModel();
        etel_table = new JTable(tmodel){
            public boolean isCellEditable(int row, int column) {
                return false;
            };

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2 || column == 3) {
                    return Integer.class;
                }
                return super.getColumnClass(column);
            }
        };

        tmodel.setColumnIdentifiers(fejlec);

        etel_table.getColumn("ID").setMaxWidth(30);
        etel_table.getColumn("ID").setMinWidth(30);
        etel_table.getColumn("Darabszám").setMaxWidth(110);
        etel_table.getColumn("Darabszám").setMinWidth(80);
        etel_table.getColumn("Étel neve").setMinWidth(200);
        etel_table.getColumn("Ár").setMinWidth(120);
        etel_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        etel_table.setShowVerticalLines(false);
        table_scrollpane = new JScrollPane();

        ArrayList<String> etelek_listaja = new ArrayList<String>();
        for (FoodItem foodItem: ProgramDataManager.getÉtelek_lista()) {
            etelek_listaja.add(foodItem.id + ", " + foodItem.name + ", " + foodItem.price + " Ft");
        }
        etel_comboBox = new JComboBox(etelek_listaja.toArray());

        etel_hozzaad_button = new JButton();
        etel_hozzaad_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FoodItem food = new FoodItem("",0,-1);
                String[] s = etel_comboBox.getSelectedItem().toString().split(",");
                int id = Integer.parseInt(s[0]);
                for(FoodItem f:ProgramDataManager.getÉtelek_lista()){
                    if(id == f.id) food=f;
                }

                ArrayList<Integer> checkExistingID = new ArrayList<>();
                for (int count = 0; count < tmodel.getRowCount(); count++){
                    checkExistingID.add(Integer.parseInt(tmodel.getValueAt(count,0).toString()));
                }

                if(checkExistingID.contains(food.id)){
                    boolean popup = PopupWindowYesNo.showPopup(frame,"FIGYELEM!","Ilyen tétel már szerepel a listán! \nFelül szeretnéd írni?");
                    if(popup){
                        osszesOsszeg -= Integer.parseInt(tmodel.getValueAt(checkExistingID.indexOf(food.id),3).toString());
                        tmodel.removeRow(checkExistingID.indexOf(food.id));
                        osszesOsszeg += ((Integer.parseInt(etel_spinner.getValue().toString())) * food.price);
                        fizetendo_osszeg_label_int.setText(String.valueOf(osszesOsszeg));
                        String[] ujsor = {food.id+"",food.name,etel_spinner.getValue().toString(),((Integer.parseInt(etel_spinner.getValue().toString())) * food.price)+""};
                        tmodel.addRow(ujsor);
                    }
                } else {
                    osszesOsszeg += ((Integer.parseInt(etel_spinner.getValue().toString())) * food.price);
                    fizetendo_osszeg_label_int.setText(String.valueOf(osszesOsszeg));
                    String[] ujsor = {food.id+"",food.name,etel_spinner.getValue().toString(),((Integer.parseInt(etel_spinner.getValue().toString())) * food.price)+""};
                    tmodel.addRow(ujsor);
                }

            }
        });

        etel_torles_button = new JButton();
        etel_torles_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    osszesOsszeg -= Integer.parseInt(tmodel.getValueAt(etel_table.getSelectedRow(),3).toString());
                    fizetendo_osszeg_label_int.setText(String.valueOf(osszesOsszeg));
                    tmodel.removeRow(etel_table.getSelectedRow());
                } catch (Exception ignored){

                }

            }
        });

        //Foglalás dátum választása
        ArrayList<String> foglalasok_listaja = new ArrayList<String>();
        for(int i = ProgramDataManager.getNyitva_innen();i < ProgramDataManager.getNyitva_eddig(); i++){
            foglalasok_listaja.add(i + ":00 - " + (i+1) + ":00");
        }
        idopont_comboBox = new JComboBox(foglalasok_listaja.toArray());
    }

    private void setUIcomponents() {
        date_selector.getEditor().setEditable(false);
        date_selector.getMonthView().setTodayBackground(Color.ORANGE);
        date_selector.getEditor().setValue(new Date(System.currentTimeMillis()));

        letszam_spinner.setModel(new SpinnerNumberModel(1, 1, ProgramDataManager.getAsztalok_száma() * 4, 1));
        ((JSpinner.DefaultEditor) letszam_spinner.getEditor()).getTextField().setEditable(false);

        letszam_spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int db = (Integer)letszam_spinner.getValue();
                double db2 = db;
                db2=db2/4+0.75;
                label_szukseges_asztalok_int.setText((int)db2 + " db");
            }
        });

        etel_spinner.setModel(new SpinnerNumberModel(1, 1, 20, 1));
        ((JSpinner.DefaultEditor) etel_spinner.getEditor()).getTextField().setEditable(false);

        megsem_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });


    }

    private void addFoodItems(){
        ProgramDataManager.addNewFood("Húsleves",600);
        ProgramDataManager.addNewFood("Gyümölcsleves", 850);
        ProgramDataManager.addNewFood("Rántotthús rízzsel",1350);
        ProgramDataManager.addNewFood("Sajttál", 1200);
        ProgramDataManager.addNewFood("Fánk (3db)", 650);
        ProgramDataManager.addNewFood("Palacsinta", 550);
    }


}