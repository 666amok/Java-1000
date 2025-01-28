package bowling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Bowling {
    static String line1, line2;
    static Integer shotCount = null, shots = null;

    public static Scanner scan() throws FileNotFoundException{
        FileReader file = new FileReader("src/bowling/input.txt");
        Scanner scn = new Scanner(file);
        return scn;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = scan();
        if (scan.hasNext()) {
            line1 = scan.nextLine();
            if (scan().hasNext()){
                line2 = scan.nextLine();
            } else System.out.println("The input file should contain at least 2 lines.");
        } else System.out.println("The input file is empty!");

        //System.out.println(shotCount);
        //System.out.println(shots);
        try {
            shotCount = Integer.valueOf(line1);
        } catch (Exception e) {
            throw new IllegalArgumentException("It should be a number.",e);
        }
        System.out.println(shotCount);

        if (!line2.isEmpty()){
            int[] shots = new int [line2.split(" ").length];
            if (shots.length != shotCount){
                System.out.println("Something is wrong! Shots count is different to the shots.");
            }
        }




    }
}
