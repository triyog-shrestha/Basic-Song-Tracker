import java.util.Scanner;


public class App {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Features ob = new Features();
        int option = 0;
        do {
            option = ob.menu();
            if (option == 1) {
                ob.played();
            }
            else if (option == 3){
                ob.listSongs();
            }


        }while (option != 4 );
        sc.close();
    }
}
