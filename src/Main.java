import java.util.Scanner;
import javax.sound.sampled.*;


public class Main {
    public static void main(String args[]) {
        // Morse and English alphabet
        String[] english = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                ",", ".", "?" };

        String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
                ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
                "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
                "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
                "-----", "--..--", ".-.-.-", "..--.." };

        // Taking input
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter message you wish to convert: ");
        String message = sc.nextLine();
        String morseMsg = "";
        int characterPos = 0;

        // Going through each letter of msg and converting to morse
        for(char c: message.toCharArray()) {
            for(int i = 0; i < english.length; i++) {
                if (c == ' ') {
                    characterPos = -1;
                    i = english.length;
                } else if (c == english[i].charAt(0)) {
                    characterPos = i;
                    i = english.length;
                }
            }

            if (characterPos == -1) {
                morseMsg += " ";
            } else {
                morseMsg += morse[characterPos];
            }
        }

        // Print morse message and play audio for morse message
        System.out.println(morseMsg);

        for(char c: morseMsg.toCharArray()) {
            if (c == '.') {
                try {
                    tone(600, 100, 1.0);
                } catch (LineUnavailableException e) {}
            } else if (c == '-') {
                try {
                    tone(600, 300, 1.0);
                } catch (LineUnavailableException e) {}
            } else if (c == ' ') {
                try {
                    tone(600, 700, 0.0);
                } catch (LineUnavailableException e) {}
            }
        }
    }

    // Method to play tone for dash or dot
    public static void tone(int hz, int msecs, double vol) throws LineUnavailableException{
        float SAMPLE_RATE = 8000f;
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
