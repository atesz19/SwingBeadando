package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    public static void addNewFood(String name, int price){
        FoodItem f = new FoodItem(name, price,ételek_lista.size()+1);
        ételek_lista.add(f);
    }

    public static LinkedList<FoodItem> getÉtelek_lista(){
        return ételek_lista;
    }

    public static int saveData(String s){
        int i = 0;
        try {
            FileWriter myWriter = new FileWriter("save.txt");
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            i=1;
        }
        return i;
    }

    public static FoodItem getFoodByID(int id){
        for(FoodItem f:ételek_lista){
            if(f.id == id){
                return f;
            }
        }
        return null;
    }

}
