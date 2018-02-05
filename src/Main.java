import java.io.*;
import java.util.InputMismatchException;

/**
 * Created by Alvarez, Mary Michaelle; Famat, Ruffa Mae; and Serato, Jay Vince on February 05, 2018.
 */
public class Main {
    private static BufferedReader br;
    private static BufferedWriter wr;
    private static final File INPUT_FILE = new File("input.in");
    private static final File OUTPUT_FILE = new File("output.out");


    public static void main(String[] args) {
        int types = 0;

        try {
            // Putting the input and output files in a buffered reader and writer respectively.
            br = new BufferedReader(new FileReader(INPUT_FILE));
            wr = new BufferedWriter(new FileWriter(OUTPUT_FILE));

            // Identifying the number of types (found in the first line of the INPUT_FILE).
            try {
                types = Integer.parseInt(br.readLine());
            } catch (InputMismatchException e) {
                System.err.println(e.getMessage());
            }

            while (types-- > 0) {
                // TODO Checking its type whether its a variable declaration, a function declaration, or function definition.

            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            // Before terminating, close all buffered readers and writers.
            try {
                br.close();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
