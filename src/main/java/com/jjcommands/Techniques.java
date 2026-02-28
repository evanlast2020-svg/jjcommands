package com.jjcommands;

import java.util.*;

public class Techniques {

    public record Technique(String name, double id, String tier, double power) {}

    public static final List<Technique> S = new ArrayList<>();
    public static final List<Technique> A = new ArrayList<>();
    public static final List<Technique> B = new ArrayList<>();
    public static final List<Technique> C = new ArrayList<>();
    public static final List<Technique> ALL = new ArrayList<>();

    static {
        reg("Mahoraga", 16, "S", 2000); reg("Gojo", 2, "S", 1800);
        reg("Sukuna", 1, "S", 2000);    reg("Geto", 18, "S", 1600);
        reg("Tsukumo", 9, "S", 1400);   reg("Yuta", 5, "S", 1600);
        reg("Maki", -1, "S", 1200);     reg("Hakari", 29, "S", 1400);
        reg("Megumi", 6, "S", 1400);    reg("Yuji", 21, "S", 1600);
        reg("Kashimo", 7, "S", 1400);

        reg("Higuruma", 27, "A", 1000); reg("Yaga", 33, "A", 1000);
        reg("Yorozu", 39, "A", 1000);   reg("Jogo", 4, "A", 1200);
        reg("Mahito", 15, "A", 1200);   reg("Todo", 20, "A", 1000);
        reg("Angel", 28, "A", 1000);    reg("Uraume", 24, "A", 1000);
        reg("Choso", 10, "A", 1000);    reg("Rozetsu", 43, "A", 1000);

        reg("Ino", 40, "B", 800);       reg("Ogi", 26, "B", 800);
        reg("Hanami", 14, "B", 800);    reg("Dagon", 8, "B", 800);
        reg("Jinichi", 22, "B", 800);   reg("Dhruv Lakdawalla", 37, "B", 800);
        reg("Nanami", 13, "B", 800);    reg("Naoya", 19, "B", 800);
        reg("Nobara", 34, "B", 800);    reg("Miguel", 30, "B", 800);
        reg("Uro", 38, "B", 800);       reg("Reggie", 44, "B", 800);

        reg("Inumaki", 3, "C", 600);    reg("Mei-Mei", 11, "C", 600);
        reg("Ishigori", 12, "C", 600);  reg("Smallpox", 25, "C", 600);
        reg("Takaba", 17, "C", 600);    reg("Kurourushi", 23, "C", 600);
        reg("Kusakabe", 31, "C", 600);  reg("Chojuro", 32, "C", 600);
        reg("Nishiyami", 36, "C", 600); reg("Junpei", 35, "C", 600);
        reg("Kaori", 41, "C", 600);     reg("Ranta", 46, "C", 600);
        reg("Hazenoki", 45, "C", 600);
    }

    private static void reg(String name, double id, String tier, double power) {
        Technique t = new Technique(name, id, tier, power);
        ALL.add(t);
        switch (tier) {
            case "S" -> S.add(t);
            case "A" -> A.add(t);
            case "B" -> B.add(t);
            case "C" -> C.add(t);
        }
    }

    public static List<Technique> getTier(String tier) {
        return switch (tier.toUpperCase()) {
            case "S" -> S; case "A" -> A; case "B" -> B; case "C" -> C;
            default -> null;
        };
    }

    public static Technique findByName(String name) {
        return ALL.stream().filter(t -> t.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static String color(String tier) {
        return switch (tier) {
            case "S" -> "§c"; case "A" -> "§6"; case "B" -> "§e"; default -> "§f";
        };
    }

    public static String randomTier() {
        double r = Math.random() * 100;
        if (r < 5) return "S";
        else if (r < 20) return "A";
        else if (r < 50) return "B";
        else return "C";
    }
}
