package bowling;

import java.io.*;

public class Bowling {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/bowling/input.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/bowling/output.txt"));

        int throwsCount = Integer.parseInt(reader.readLine());
        String[] parts = reader.readLine().split(" ");
        int[] throwsResults = new int[throwsCount];
        for (int i = 0; i < throwsCount; i++) {
            throwsResults[i] = Integer.parseInt(parts[i]);
        }

        int totalScore = 0;
        int currentThrow = 0;

        for (int frame = 0; frame < 10; frame++) {
            if (currentThrow >= throwsCount) break;

            if (throwsResults[currentThrow] == 10) { // Strike
                totalScore += 10;
                if (currentThrow + 1 < throwsCount) totalScore += throwsResults[currentThrow + 1];
                if (currentThrow + 2 < throwsCount) totalScore += throwsResults[currentThrow + 2];
                currentThrow++;
            } else {
                int firstThrow = throwsResults[currentThrow];
                int secondThrow = (currentThrow + 1 < throwsCount) ? throwsResults[currentThrow + 1] : 0;
                totalScore += firstThrow + secondThrow;

                if (firstThrow + secondThrow == 10) { // Spare
                    if (currentThrow + 2 < throwsCount) totalScore += throwsResults[currentThrow + 2];
                }
                currentThrow += 2;
            }
        }

        writer.write(Integer.toString(totalScore));
        reader.close();
        writer.close();
    }
}