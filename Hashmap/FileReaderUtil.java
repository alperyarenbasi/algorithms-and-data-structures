import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileReaderUtil {
    /**
     * Leser en fil og returnerer innholdet som en streng med riktig tegnkoding.
     */
    public static String readFileAsString(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            byte[] data = fileInputStream.readAllBytes();
            // Konverterer byte-array til streng med riktig tegnsett
            String content = new String(data, StandardCharsets.UTF_8); // Bruk ISO_8859_1 hvis det passer bedre
            System.out.println("Fil lest som streng:");
            System.out.println(content);
            return content;
        } catch (FileNotFoundException e) {
            System.out.println("Filen ble ikke funnet. Sjekk filnavnet og plasseringen.");
        } catch (IOException e) {
            System.out.println("En feil oppstod: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String filePath = "forelesning1.txt"; // Erstatt med filnavnet du vil lese
        String fileContent = readFileAsString(filePath);
    }
}
