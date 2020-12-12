package com.company;

import javax.swing.*;
import java.awt.*;

public class PopupWindowYesNo {

    public static boolean showPopup(Frame frame,String title, String message) {

        int result = JOptionPane.showConfirmDialog(frame,message,title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION){
            return true;
        }else if (result == JOptionPane.NO_OPTION){
            return false;
        }else {
            return false;
        }
    }

}
