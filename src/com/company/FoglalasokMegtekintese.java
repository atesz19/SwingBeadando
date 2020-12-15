package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class FoglalasokMegtekintese {
    public final JFrame frame = new JFrame("Foglalások megtekintése");

    private JTable reservationTable;
    private JXDatePicker date_selector;
    private JButton show_selected_button;
    private JPanel pane;


    public FoglalasokMegtekintese(){
        frame.setContentPane(pane);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("rsc/icon.png").getImage());
    }


    private void createUIComponents() {
        DefaultTableModel tmodel = new DefaultTableModel();
        reservationTable = new JTable(tmodel){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable.setAutoCreateRowSorter(true);

        date_selector = new JXDatePicker();
        date_selector.getEditor().setEditable(false);
        date_selector.getMonthView().setTodayBackground(Color.ORANGE);
        date_selector.getEditor().setValue(new Date(System.currentTimeMillis()));
        date_selector.addActionListener(e -> {
            for(int i = tmodel.getRowCount()-1; i>=0;i--){
                tmodel.removeRow(i);
            }
            LinkedList<ReservationData> resdata = DataManager.getReservations();
            for(ReservationData r:resdata){
                if(r.date.equalsIgnoreCase(date_selector.getEditor().getText())){
                    UserData userData = DataManager.getUserfromID(r.userID);
                    if (userData.id != -1){
                        String time = String.valueOf(r.time);
                        if(time.length() == 1) time = 0+time;
                        String[] sor = {time +":00 - " + (r.time+1) + ":00", userData.name,userData.mobile_number, String.valueOf(r.count)};
                        tmodel.addRow(sor);
                    }

                }
            }

        });

        tmodel.setColumnIdentifiers(new String[]{"Időpont", "Foglaló neve","Telefonszáma", "Létszám"});

        LinkedList<ReservationData> resdata = DataManager.getReservations();
        for(ReservationData r:resdata){
            if(r.date.equalsIgnoreCase(date_selector.getEditor().getText())){
                UserData userData = DataManager.getUserfromID(r.userID);
                if (userData.id != -1){
                    String time = String.valueOf(r.time);
                    if(time.length() == 1) time = 0+time;
                    String[] sor = {time +":00 - " + (r.time+1) + ":00", userData.name,userData.mobile_number, String.valueOf(r.count)};
                    tmodel.addRow(sor);
                }

            }
        }
        show_selected_button = new JButton();
        show_selected_button.addActionListener(e -> {
            if(reservationTable.getSelectedRow() != -1){
                String phone = tmodel.getValueAt(reservationTable.getSelectedRow(),2).toString();
                ReservationData rd = new ReservationData();
                for(ReservationData r:DataManager.getReservations()){
                    int time;
                    String[] s = tmodel.getValueAt(reservationTable.getSelectedRow(),0).toString().split(":");
                    if(s[0].charAt(0) == '0') s[0]= String.valueOf(s[0].charAt(1));
                    time = Integer.parseInt(s[0]);
                    if(
                            r.date.equalsIgnoreCase(date_selector.getEditor().getText())
                            && r.time == time
                            && r.userID == DataManager.getUserIDfromPhone(phone)
                            && r.count == Integer.parseInt(tmodel.getValueAt(reservationTable.getSelectedRow(),3).toString())
                    ){
                        rd = r;
                    }
                }
                FoglalasReszletei foglalasReszletei = new FoglalasReszletei(frame,DataManager.getUserfromID(DataManager.getUserIDfromPhone(phone)), rd);
            }
        });


    }
}
