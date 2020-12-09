package com.company;

import java.util.HashMap;

public class ProgramDataManager {
    private static int nyitva_innen = 10;
    private static int nyitva_eddig = 22;
    private static int asztalok_száma = 12;
    private static HashMap<String,Integer> ételek = new HashMap<String,Integer>();

    public static int getNyitva_innen() {
        return nyitva_innen;
    }

    public static int getNyitva_eddig() {
        return nyitva_eddig;
    }

    public static int getAsztalok_száma() {
        return asztalok_száma;
    }

    public static HashMap<String, Integer> getÉtelek() {
        ételek.put("Húsleves",800);
        ételek.put("Bableves", 990);
        ételek.put("Palacsinta",1100);
        ételek.put("Spagetti", 890);
        ételek.put("Desszert", 650);
        return ételek;
    }
}
