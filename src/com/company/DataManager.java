package com.company;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class DataManager {
    private static final int nyitva_innen = 9;
    private static final int nyitva_eddig = 22;
    private static final int asztalok_száma = 12;
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
            FileWriter myWriter = new FileWriter("save.txt",true);
            StringBuilder s = new StringBuilder(userId + ";" + date + ";" + time + ";" + count + ";");
            for(FoodItem fi:ll){
                String ss = fi.name + "," + fi.db + "," + fi.price + "$";
                s.append(ss);
            }
            if(getFileRowCount("save.txt") >= 1) myWriter.append("\n");
            myWriter.append(s.toString());
            myWriter.close();
        } catch (IOException e) {
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
            FileWriter myWriter = new FileWriter("users.txt",true);
            if(getFileRowCount("users.txt") >= 1) myWriter.append("\n");
            myWriter.append(s);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getUserIDfromPhone(String phone_number){
        try {
            File file = new File("users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] s = sc.nextLine().split(";");
                if(s[2].equalsIgnoreCase(phone_number)){
                    return Integer.parseInt(s[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
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
            e.printStackTrace();
        }
        return ll;
    }

    public static LinkedList<ReservationData> getReservations(){
        LinkedList<ReservationData> r_data = new LinkedList<>();
        try{
            File file = new File("save.txt");
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                ReservationData rd = new ReservationData();
                String[] r_string = sc.nextLine().split(";");
                rd.userID = Integer.parseInt(r_string[0]);
                rd.date = r_string[1];
                rd.time = Integer.parseInt(r_string[2]);
                rd.count = Integer.parseInt(r_string[3]);
                if(r_string.length >=5) {
                    String[] foods_string = r_string[4].split("[$]");
                    for (String s : foods_string) {
                        String[] f_item = s.split(",");
                        FoodItem fi = new FoodItem(
                                f_item[0],
                                Integer.parseInt(f_item[2])
                        );
                        fi.db = Integer.parseInt(f_item[1]);
                        fi.id = getFoodID(fi.name);
                        rd.foods_list.add(fi);
                    }
                }
                r_data.add(rd);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return r_data;
    }

    public static int getTableCount(int emberek_szama){
        double db2 = emberek_szama;
        db2=db2/4+0.75;
        return((int)db2);
    }

    public static int getFreeTableCount(String date, int time){
        LinkedList<ReservationData> rd = getReservations();
        int count = 0;
        for(ReservationData r:rd){
            if(r.date.equalsIgnoreCase(date) && r.time == time){
                count += getTableCount(r.count);
            }
        }
        count = asztalok_száma-count;
        return count;
    }

    public static int getFoodID(String name){
        for(FoodItem f:ételek_lista){
            if(f.name.equalsIgnoreCase(name)){
                return f.id;
            }
        }
        return -1;
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
