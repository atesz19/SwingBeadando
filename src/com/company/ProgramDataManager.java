package com.company;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ProgramDataManager {
    private static int nyitva_innen = 9;
    private static int nyitva_eddig = 22;
    private static int asztalok_száma = 12;
    private static LinkedList<FoodItem> ételek_lista = new LinkedList<>();

    public static int getNyitva_innen() {
        return nyitva_innen;
    }

    public static int getNyitva_eddig() {
        return nyitva_eddig;
    }

    public static int getAsztalok_száma() {
        return asztalok_száma;
    }

    public static void addNewFood(String name, int price) {
        FoodItem f = new FoodItem(name, price);
        f.id = ételek_lista.size() + 1;
        ételek_lista.add(f);
    }

    public static LinkedList<FoodItem> getÉtelek_lista() {
        return ételek_lista;
    }

    public static FoodItem getFoodByID(int id) {
        for (FoodItem f : ételek_lista) {
            if (f.id == id) {
                return f;
            }
        }
        return null;
    }

    public static void saveData(int userId, String date, int time, int count, LinkedList<FoodItem> ll) {
        try {
            FileWriter myWriter = new FileWriter("save.txt");
            StringBuilder s = new StringBuilder(userId + ";" + date + ";" + time + ";" + count + ";");
            for(FoodItem fi:ll){
                String ss = fi.name + "," + fi.db + "," + fi.price + "$";
                s.append(ss);
            }
            if(getFileRowCount("save.txt") > 1) myWriter.append("\n");
            myWriter.append(s.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void saveUser(String name, String phone, String email, String adress) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
            String s = (lines + 1) + ";" + name + ";" + phone + ";" + email + ";" + adress;
            FileWriter myWriter = new FileWriter("users.txt");
            if(getFileRowCount("users.txt") > 1) myWriter.append("\n");
            myWriter.append(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int getUserIDfromName(String name){
        int i = -1;
        try {
            File file = new File("users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] s = sc.nextLine().split(";");
                if(s[1].equalsIgnoreCase(name)){
                    return Integer.parseInt(s[0]);
                }
            }
        } catch (IOException e) {
            System.out.println("Hiba...");
            e.printStackTrace();
        }
        return i;
    }

    public static LinkedList<UserData> getUsers() {
        LinkedList<UserData> ll = new LinkedList<>();
        try {
            File file = new File("users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] s = sc.nextLine().split(";");
                UserData ud = new UserData(Integer.parseInt(s[0]), s[1], s[2]);
                if(s.length >= 4) ud.email = s[3];
                if(s.length >= 5) ud.street = s[4];
                ll.add(ud);
            }
        } catch (IOException e) {
            System.out.println("Hiba...");
            e.printStackTrace();
        }
        return ll;
    }

    public static boolean checkUserIsSaved(String phone_number){
        LinkedList<UserData> ud = getUsers();
        for(UserData u:ud){
            if (u.mobile_number.equalsIgnoreCase(phone_number)){
                return true;
            }
        }
        return false;
    }

    public static int getFileRowCount(String filename){
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return lines;
    }

}
