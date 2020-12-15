package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

public class FelhasznaloKereses {

    public JFrame frame = new JFrame("Keresés");

    private JPanel panel;
    private JTable usersTable;
    private JTextField searchTextField;
    private JButton doneButton;
    private final JTextField name;
    private final JTextField phone;
    private final JTextField email;
    private final JTextField adress;

    public FelhasznaloKereses(JTextField name,JTextField phone, JTextField email, JTextField adress) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.adress = adress;
        frame.setContentPane(panel);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("rsc/icon.png").getImage());

    }

    private void createUIComponents() {
        doneButton = new JButton();
        DefaultTableModel tmodel = new DefaultTableModel();
        usersTable = new JTable(tmodel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tmodel.setColumnIdentifiers(new String[]{"ID","Név","Telefonszám","Email","Lakcím"});
        for(UserData u:DataManager.getUsers()){
            String[] sor = {String.valueOf(u.id),u.name,u.mobile_number,u.email,u.street};
            tmodel.addRow(sor);
        }
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        doneButton.addActionListener(e -> {
            if(usersTable.getSelectedRow() != -1) {
                name.setText(tmodel.getValueAt(usersTable.getSelectedRow(), 1).toString());
                phone.setText(tmodel.getValueAt(usersTable.getSelectedRow(), 2).toString());
                email.setText(tmodel.getValueAt(usersTable.getSelectedRow(), 3).toString());
                adress.setText(tmodel.getValueAt(usersTable.getSelectedRow(), 4).toString());
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        searchTextField = new JTextField();
        searchTextField.addActionListener(e -> System.out.println("ASD"));
        searchTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (tmodel.getRowCount() > 0) {
                    for (int i = tmodel.getRowCount() - 1; i > -1; i--) {
                        tmodel.removeRow(i);
                    }
                }

                for (UserData u : DataManager.getUsers()){
                    if(u.mobile_number.contains(searchTextField.getText())){
                        String[] sor = {String.valueOf(u.id),u.name,u.mobile_number,u.email,u.street};
                        tmodel.addRow(sor);
                    }
                }

            }
        });

    }
}
