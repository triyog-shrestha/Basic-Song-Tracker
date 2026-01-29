import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Features {
    String song;
    String album;
    String artist;
    String vibe;
    Scanner sc = new Scanner(System.in);

    void fileUpdate() {
        String file = "tracker.csv";
        ArrayList<String> lines = new ArrayList<>();
        boolean songExists = false;
        int updatedLineIndex = -1;

        // Step 1: Read existing file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            // File doesn't exist yet, will be created
        }

        // Step 2: Check if song already exists (compare song, artist, album)
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");

            if (parts.length >= 3) {
                String existingSong = parts[0].trim();
                String existingArtist = parts[1].trim();
                String existingAlbum = parts[2].trim();

                // Compare all three attributes
                if (existingSong.equalsIgnoreCase(song) &&
                        existingArtist.equalsIgnoreCase(artist) &&
                        existingAlbum.equalsIgnoreCase(album)) {

                    songExists = true;
                    updatedLineIndex = i;

                    // Increment play count
                    int currentCount = parts.length >= 5 ? Integer.parseInt(parts[4].trim()) : 1;
                    int newCount = currentCount + 1;

                    // Update the line with new count
                    String updatedLine = song + "," + artist + "," + album + "," + vibe + "," + newCount;
                    lines.set(i, updatedLine);

                    System.out.printf("Play count updated! %s has been played %d times.\n", song, newCount);
                    break;
                }
            }
        }

        // Step 3: If song doesn't exist, add new entry with count = 1
        if (!songExists) {
            String newLine = song + "," + artist + "," + album + "," + vibe + ",1";
            lines.add(newLine);
            System.out.printf("New song added! %s is now in your library.\n", song);
        }

        // Step 4: Write all lines back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file!");
        }
    }

    void listSongs() {
        String file = "tracker.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 1;
            System.out.println("\n=== All Songs in Library ===");
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    System.out.printf("%d. Song: %s | Artist: %s | Album: %s | Vibe: %s | Plays: %s\n",
                            index, parts[0], parts[1], parts[2], parts[3], parts[4]);
                    index++;
                }
            }
            System.out.println("============================\n");

            if (index == 1) {
                System.out.println("No songs in library.\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file or file not found!");
        }
    }


    int menu(){
        System.out.println("1. Play a song");
        System.out.println("2. View Rankings");
        System.out.println("3. View Library");
        System.out.println("4. Exit");
        System.out.println("Select your operation : ");
        int ch = sc.nextInt();
        sc.nextLine();
        return ch;
    }

    void playSong() {
        System.out.println("Which song do you want to play? : ");
        song = sc.nextLine();

        System.out.println("Who is the artist behind the song? : ");
        artist = sc.nextLine();

        System.out.println("Which album does the song belong to? : ");
        album = sc.nextLine();
        do {
            System.out.println("What vibe does the song give? \t1. Happy \t2. Chill \t3. Hype \t4. Cloudy : ");
            vibe = sc.nextLine();
            if (vibe.equals("1")) {
                vibe = "Happy";
            } else if (vibe.equals("2")) {
                vibe = "Chill";
            } else if (vibe.equals("3")) {
                vibe = "Hype";
            } else if (vibe.equals("4")) {
                vibe = "Cloudy";
            } else{
                vibe = null;
                System.out.println("Error, try again.");
            }

        }while (vibe==null);
        System.out.printf("You are now playing %s by %s from %s\n",song,artist,album);
        fileUpdate();
    }

    void viewRanking() {
        String file = "tracker.csv";
        ArrayList<SongData> songs = new ArrayList<>();

        // Step 1: Read file and extract song data
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String songName = parts[0].trim();
                    String artistName = parts[1].trim();
                    String albumName = parts[2].trim();
                    String vibeName = parts[3].trim();
                    int playCount = Integer.parseInt(parts[4].trim());

                    songs.add(new SongData(songName, artistName, albumName, vibeName, playCount));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file or file not found!");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Error parsing play count!");
            return;
        }
        if (songs.isEmpty()) {
            System.out.println("No songs found in tracker.");
            return;
        }

        // Step 2: Sort by play count (descending order)
        songs.sort((a, b) -> Integer.compare(b.playCount, a.playCount));

        // Step 3: Display ranked songs
        System.out.println("\n========== SONG RANKINGS (Most Played) ==========");
        int rank = 1;

        for (SongData songData : songs) {
            System.out.printf("%d. %s - %s (%d play%s)\n",
                    rank,
                    songData.song,
                    songData.artist,
                    songData.playCount,
                    songData.playCount == 1 ? "" : "s");
            System.out.printf("   Album: %s | Vibe: %s\n",
                    songData.album,
                    songData.vibe);
            System.out.println();
            rank++;
        }
        System.out.println("================================================\n");
    }

    // Helper class to store song data
    static class SongData {
        String song;
        String artist;
        String album;
        String vibe;
        int playCount;

        SongData(String song, String artist, String album, String vibe, int playCount) {
            this.song = song;
            this.artist = artist;
            this.album = album;
            this.vibe = vibe;
            this.playCount = playCount;
        }
    }

}


