package com.company;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FoglalasokMegtekintese {
    private JPanel panel1;
    private JTable table1;
    private JXDatePicker date_selector;
    private JButton kereses_gomb;

    public FoglalasokMegtekintese(){
        date_selector.getEditor().setEditable(false);
        date_selector.getMonthView().setTodayBackground(Color.ORANGE);
        date_selector.getEditor().setValue(new Date(System.currentTimeMillis()));
    }


}
