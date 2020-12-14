package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

public class AsztalFoglalas {

    public final JFrame frame = new JFrame("Asztalfoglalás");

    private int osszesOsszeg=0;
    private JPanel FoglalasPane;
    private JComboBox<String> idopont_comboBox;
    private JSpinner letszam_spinner;
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
    private JLabel errorSave_label;

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

        errorSave_label = new JLabel("");
        mentes_button = new JButton();
        mentes_button.addActionListener(e -> {
            if(!name_textField.getText().isEmpty()
                    && !phone_textField.getText().isEmpty()
                    && idopont_comboBox.getSelectedIndex() != -1
            ){
                errorSave_label.setText("");
                if(!DataManager.checkUserIsSaved(phone_textField.getText())){
                    DataManager.saveUser(name_textField.getText(),phone_textField.getText(),email_textField.getText(),lakcim_textField.getText());
                }
                String[] idopont_mentes = Objects.requireNonNull(idopont_comboBox.getSelectedItem()).toString().split(":");
                LinkedList<FoodItem> food_list = new LinkedList<>();
                for(int i = 0; i < etel_table.getRowCount(); i++){
                    FoodItem fi = new FoodItem(
                            etel_table.getModel().getValueAt(i,1).toString(),
                            Integer.parseInt(etel_table.getModel().getValueAt(i, 3).toString())
                    );
                    fi.db = Integer.parseInt(etel_table.getModel().getValueAt(i,2).toString());
                    fi.id = Integer.parseInt(etel_table.getModel().getValueAt(i,0).toString());
                    food_list.add(fi);
                }
                DataManager.saveData(
                        DataManager.getUserIDfromPhone(phone_textField.getText()),
                        date_selector.getEditor().getText(),
                        Integer.parseInt(idopont_mentes[0]),
                        (int) letszam_spinner.getValue(),
                        food_list
                );

                JOptionPane.showMessageDialog(frame,"Sikeresen létrehoztad a foglalást!");

                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));


            } else {
                errorSave_label.setText("Hiányzó adatok!");
            }
        });

        name_textField = new JTextField();
        phone_textField = new JTextField("+36");
        email_textField = new JTextField();
        lakcim_textField = new JTextField();

        date_selector = new JXDatePicker();
        date_selector.getEditor().setEditable(false);
        date_selector.getMonthView().setTodayBackground(Color.ORANGE);
        date_selector.getEditor().setValue(new Date(System.currentTimeMillis()));
        date_selector.addActionListener(e -> {
            letszam_spinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            idopont_comboBox.removeAllItems();
            for(int i = DataManager.getNyitva_innen(); i < DataManager.getNyitva_eddig(); i++){
                int freeTablesCount = DataManager.getFreeTableCount(date_selector.getEditor().getText(),i);
                if(freeTablesCount > 0){
                    idopont_comboBox.addItem(i + ":00 - " + (i+1) + ":00");
                }
            }
            idopont_comboBox.setSelectedIndex(-1);
            if(idopont_comboBox.getItemCount() == 0){
                errorSave_label.setText("A MAI NAP BETELT!");
                mentes_button.setEnabled(false);
            } else {
                errorSave_label.setText("");
                mentes_button.setEnabled(true);
            }
        });

        //Az ételek kezelésére szolgáló részek
        String[] fejlec = {"ID", "Étel neve", "Darabszám", "Ár"};

        DefaultTableModel tmodel = new DefaultTableModel();
        etel_table = new JTable(tmodel){
            public boolean isCellEditable(int row, int column) {
                return false;
            }

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

        ArrayList<String> etelek_listaja = new ArrayList<>();
        for (FoodItem foodItem: DataManager.getÉtelek_lista()) {
            etelek_listaja.add(foodItem.id + ", " + foodItem.name + ", " + foodItem.price + " Ft");
        }
        etel_comboBox = new JComboBox(etelek_listaja.toArray());

        etel_hozzaad_button = new JButton();
        etel_hozzaad_button.addActionListener(e -> {
            FoodItem food = new FoodItem("",0);
            String[] s = Objects.requireNonNull(etel_comboBox.getSelectedItem()).toString().split(",");
            int id = Integer.parseInt(s[0]);
            for(FoodItem f: DataManager.getÉtelek_lista()){
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

        });

        etel_torles_button = new JButton();
        etel_torles_button.addActionListener(e -> {
            try{
                osszesOsszeg -= Integer.parseInt(tmodel.getValueAt(etel_table.getSelectedRow(),3).toString());
                fizetendo_osszeg_label_int.setText(String.valueOf(osszesOsszeg));
                tmodel.removeRow(etel_table.getSelectedRow());
            } catch (Exception ignored){

            }

        });

        //Foglalás dátum választása
        idopont_comboBox = new JComboBox<String>();
        for(int i = DataManager.getNyitva_innen(); i < DataManager.getNyitva_eddig(); i++){
            int freeTablesCount = DataManager.getFreeTableCount(date_selector.getEditor().getText(),i);
            if(freeTablesCount > 0){
                idopont_comboBox.addItem(i + ":00 - " + (i+1) + ":00");
            }
        }
        idopont_comboBox.setSelectedIndex(-1);
        if(idopont_comboBox.getItemCount() == 0){
            errorSave_label.setText("A MAI NAP BETELT!");
            mentes_button.setEnabled(false);
        } else {
            errorSave_label.setText("");
            mentes_button.setEnabled(true);
        }
        idopont_comboBox.addItemListener(e -> {
            String[] s = e.getItem().toString().split(":");
            letszam_spinner.setModel(new SpinnerNumberModel(1, 1, DataManager.getFreeTableCount(date_selector.getEditor().getText(),Integer.parseInt(s[0]))*4, 1));
            ((JSpinner.DefaultEditor) letszam_spinner.getEditor()).getTextField().setEditable(false);
        });

    }

    private void setUIcomponents() {

        letszam_spinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
        ((JSpinner.DefaultEditor) letszam_spinner.getEditor()).getTextField().setEditable(false);

        letszam_spinner.addChangeListener(e -> {
            label_szukseges_asztalok_int.setText(DataManager.getTableCount((Integer)letszam_spinner.getValue()) + " db");
        });

        etel_spinner.setModel(new SpinnerNumberModel(1, 1, 20, 1));
        ((JSpinner.DefaultEditor) etel_spinner.getEditor()).getTextField().setEditable(false);

        megsem_button.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));


    }

    private void addFoodItems(){
        DataManager.addNewFood("Húsleves",600);
        DataManager.addNewFood("Gyümölcsleves", 850);
        DataManager.addNewFood("Rántotthús rízzsel",1350);
        DataManager.addNewFood("Sajttál", 1200);
        DataManager.addNewFood("Fánk (3db)", 650);
        DataManager.addNewFood("Palacsinta", 550);
    }


}