package uz.mediasolutions.jurabeklabbackend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransliteratorService {

    private static final Map<Character, String> cyrillicToLatinMap = new HashMap<>();

    static {
        // Kirillcha harflarni lotinchaga xaritalash
        cyrillicToLatinMap.put('А', "A");   cyrillicToLatinMap.put('а', "a");
        cyrillicToLatinMap.put('Б', "B");   cyrillicToLatinMap.put('б', "b");
        cyrillicToLatinMap.put('В', "V");   cyrillicToLatinMap.put('в', "v");
        cyrillicToLatinMap.put('Г', "G");   cyrillicToLatinMap.put('г', "g");
        cyrillicToLatinMap.put('Д', "D");   cyrillicToLatinMap.put('д', "d");
        cyrillicToLatinMap.put('Е', "E");   cyrillicToLatinMap.put('е', "e");
        cyrillicToLatinMap.put('Ё', "Yo");  cyrillicToLatinMap.put('ё', "yo");
        cyrillicToLatinMap.put('Ж', "J");   cyrillicToLatinMap.put('ж', "j");
        cyrillicToLatinMap.put('З', "Z");   cyrillicToLatinMap.put('з', "z");
        cyrillicToLatinMap.put('И', "I");   cyrillicToLatinMap.put('и', "i");
        cyrillicToLatinMap.put('Й', "Y");   cyrillicToLatinMap.put('й', "y");
        cyrillicToLatinMap.put('К', "K");   cyrillicToLatinMap.put('к', "k");
        cyrillicToLatinMap.put('Л', "L");   cyrillicToLatinMap.put('л', "l");
        cyrillicToLatinMap.put('М', "M");   cyrillicToLatinMap.put('м', "m");
        cyrillicToLatinMap.put('Н', "N");   cyrillicToLatinMap.put('н', "n");
        cyrillicToLatinMap.put('О', "O");   cyrillicToLatinMap.put('о', "o");
        cyrillicToLatinMap.put('П', "P");   cyrillicToLatinMap.put('п', "p");
        cyrillicToLatinMap.put('Р', "R");   cyrillicToLatinMap.put('р', "r");
        cyrillicToLatinMap.put('С', "S");   cyrillicToLatinMap.put('с', "s");
        cyrillicToLatinMap.put('Т', "T");   cyrillicToLatinMap.put('т', "t");
        cyrillicToLatinMap.put('У', "U");   cyrillicToLatinMap.put('у', "u");
        cyrillicToLatinMap.put('Ф', "F");   cyrillicToLatinMap.put('ф', "f");
        cyrillicToLatinMap.put('Х', "X");   cyrillicToLatinMap.put('х', "x");
        cyrillicToLatinMap.put('Ц', "Ts");  cyrillicToLatinMap.put('ц', "ts");
        cyrillicToLatinMap.put('Ч', "Ch");  cyrillicToLatinMap.put('ч', "ch");
        cyrillicToLatinMap.put('Ш', "Sh");  cyrillicToLatinMap.put('ш', "sh");
        cyrillicToLatinMap.put('Щ', "Shch");cyrillicToLatinMap.put('щ', "shch");
        cyrillicToLatinMap.put('Ы', "Y");   cyrillicToLatinMap.put('ы', "y");
        cyrillicToLatinMap.put('Э', "E");   cyrillicToLatinMap.put('э', "e");
        cyrillicToLatinMap.put('Ю', "Yu");  cyrillicToLatinMap.put('ю', "yu");
        cyrillicToLatinMap.put('Я', "Ya");  cyrillicToLatinMap.put('я', "ya");
        // Hamma xarflarni to'liq kiritdik
    }

    public String transliterate(String cyrillicText) {
        StringBuilder latinText = new StringBuilder();

        for (char c : cyrillicText.toCharArray()) {
            String latinChar = cyrillicToLatinMap.getOrDefault(c, String.valueOf(c));
            latinText.append(latinChar);
        }

        return latinText.toString();
    }

}
