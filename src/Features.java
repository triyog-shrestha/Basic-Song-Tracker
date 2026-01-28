import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Features {
    String song;
    String album;
    String artist;
    String status = null;
    Scanner sc = new Scanner(System.in);

    void playSong(){
        System.out.printf("You are now playing %s by %s from %s\n",song,artist,album);
        status = "Playing";
    }
    void stopSong(){
        System.out.printf("%s by %s from %s has been stopped\n",song,artist,album);
        status = "Stopped";
    }
    void fileUpdate(){
        String file = "tracker.csv";
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file, true))) {
            this.playSong();
            String data = song + "," + artist + "," + album + "," + status;
            fw.write(data);
            fw.newLine();

        } catch (IOException e) {
            System.out.println("Error writing to file!");
        }
    }

    void listSongs(){
        String file = "tracker.csv";
        boolean foundPlaying = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 1;
            System.out.println("\n=== Currently Playing Songs ===");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 4 && parts[3].trim().equals("Playing")) {
                    System.out.printf("%d. Song: %s | Artist: %s | Album: %s\n",
                            index, parts[0], parts[1], parts[2]);
                    foundPlaying = true;
                    index++;
                }
            }
            System.out.println("==============================");
            if (!foundPlaying) {
                System.out.println("No songs are currently playing.\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file or file not found!");
        }
    }

    int menu(){
        System.out.println("1. Play a song");
        System.out.println("2. Stop a song");
        System.out.println("3. View all currently playing");
        System.out.println("4. Exit");
        System.out.println("Select your operation : ");
        int ch = sc.nextInt();
        sc.nextLine();
        return ch;
    }

    void played() {
        System.out.println("Which song do you want to play? : ");
        song = sc.nextLine();

        System.out.println("Who is the artist behind the song? : ");
        artist = sc.nextLine();

        System.out.println("Which album does the song belong to? : ");
        album = sc.nextLine();
        fileUpdate();
    }

}


