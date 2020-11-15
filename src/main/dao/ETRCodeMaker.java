package main.dao;

import java.util.HashMap;
import java.util.Map;

public abstract class ETRCodeMaker {
    private static final Map<Character, Character> makeAscii = new HashMap<>();
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        makeAscii.put('Ö', 'O');
        makeAscii.put('Ő', 'O');
        makeAscii.put('Ó', 'O');
        makeAscii.put('Ü', 'U');
        makeAscii.put('Ű', 'U');
        makeAscii.put('Ú', 'U');
        makeAscii.put('É', 'E');
        makeAscii.put('Á', 'A');
    }

    public static String createEtrCode(String firstName, String lastName, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeAscii.containsKey(firstName.toUpperCase().charAt(0)) ?
            makeAscii.get(firstName.toUpperCase().charAt(0)) :
            firstName.toUpperCase().charAt(0));
        sb.append(makeAscii.containsKey(firstName.toUpperCase().charAt(1)) ?
            makeAscii.get(firstName.toUpperCase().charAt(1)) :
            firstName.toUpperCase().charAt(1));
        sb.append(makeAscii.containsKey(lastName.toUpperCase().charAt(0)) ?
            makeAscii.get(lastName.toUpperCase().charAt(0)) :
            lastName.toUpperCase().charAt(0));
        int q = count / 26;
        int q2 = q / 26;
        int q3 = q2 / 26;
        sb.append(alphabet.charAt((q2 + 24) % 26));
        sb.append(alphabet.charAt(q % 26));
        sb.append(alphabet.charAt(count % 26));
        sb.append(alphabet.charAt((q3 + 19) % 26));
        sb.append(".SZE");
        return sb.toString();
    }

    public static String etrCodePrefix(String firstName, String lastName) {
        String prefix = "";
        prefix += makeAscii.containsKey(firstName.toUpperCase().charAt(0)) ?
            makeAscii.get(firstName.toUpperCase().charAt(0)) :
            firstName.toUpperCase().charAt(0);
        prefix += makeAscii.containsKey(firstName.toUpperCase().charAt(1)) ?
            makeAscii.get(firstName.toUpperCase().charAt(1)) :
            firstName.toUpperCase().charAt(1);
        prefix += makeAscii.containsKey(lastName.toUpperCase().charAt(0)) ?
            makeAscii.get(lastName.toUpperCase().charAt(0)) :
            lastName.toUpperCase().charAt(0);
        return prefix;
    }
}
