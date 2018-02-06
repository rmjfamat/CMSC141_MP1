import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by Alvarez, Mary Michaelle; Famat, Ruffa Mae; and Serato, Jay Vince on February 05, 2018.
 */
public class Main {
    private static BufferedReader br;
    private static BufferedWriter wr;
    private static final File INPUT_FILE = new File("mpa1.in");
    private static final File OUTPUT_FILE = new File("mpa1.out");
    private static final List<String> dataTypes = Arrays.asList("void", "int", "short", "long", "float", "double", "char", "return");


    public static void main(String[] args) {
        int types = 0;
        String sCurrLine;

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

            sCurrLine = br.readLine();
            while (--types >= 0) {
                Type type = null;
                boolean valid = true;
                boolean specialInvalidity = false;

                wr.append(String.valueOf(types));
                System.out.println(types + "");
                wr.newLine();

                while (sCurrLine != null) {
                    int i;
                    String returnType = "";

                    sCurrLine = sCurrLine.trim(); // Removing redundant spaces
                    System.out.println(sCurrLine);
                    if ( type != Type.FUNCTION_DEFINITION) {
                        // Checking its type whether its a variable declaration, a function declaration, or function definition.
                        if (sCurrLine.charAt(sCurrLine.length() - 1) == ';') { // The type is either a variable or a function declaration.
                            if (sCurrLine.contains("(") && sCurrLine.contains(")")) { // The type is likely a function declaration.
                                type = Type.FUNCTION_DECLARATION;
                                System.out.println("The type is a valid function declaration.");
                            } else { // The type is likely a variable declaration.
                                type = Type.VARIABLE_DECLARATION;
                                System.out.println("The type is a valid variable declaration.");
                            }
                        } else if (sCurrLine.contains("(") && sCurrLine.contains(")") && sCurrLine.charAt(sCurrLine.length() - 1) == '{') { // The type is likely a function declaration.
                            type = Type.FUNCTION_DEFINITION;
                            System.out.println("The type is a valid function definition.");
                        } else { // Preliminary test: INVALID
                            valid = false;
                            wr.append("INVALID ");
                            if (sCurrLine.contains("(") || sCurrLine.contains(")")) { // An invalid function
                                wr.append("FUNCTION");
                                if (sCurrLine.contains("{")) {
                                    wr.append(" DEFINITION");
                                } else {
                                    wr.append(" DECLARATION");
                                }
                            } else {
                                wr.append("VARIABLE DECLARATION");
                                while ((sCurrLine = br.readLine()).charAt(sCurrLine.length() - 1) == ';') ;
                                specialInvalidity = true;
                            }
                            System.out.println("Breaking");
                            break;
                        }
                    }

                    // Obtaining and checking the validity of the return type
                    for (i = 0; sCurrLine.charAt(i) != ' '; i++) {
                        returnType = returnType.concat(sCurrLine.charAt(i) + "");
                    }
                    System.out.println(returnType);
                    boolean validRetType = false;
                    for (String s : dataTypes) {
                        if (returnType.equals(s)) {
                            validRetType = true;
                            break;
                        }
                    }
                    if (!validRetType) {
                        wr.append("INVALID ").append(type.toString());
                        valid = false;
                        break;
                    }

                    // If this is not a function definition, end the process here.
                    if (type != Type.FUNCTION_DEFINITION) {
                        break;
                    } else {
                        if ((sCurrLine = br.readLine()).equals("}")) {
                            break;
                        }
                    }
                }
                if (valid) {
                    wr.append("VALID ").append(type.toString());
                }
                if (!specialInvalidity) {
                    sCurrLine = br.readLine();
                }
                wr.newLine();
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

    private enum Type {
        FUNCTION_DECLARATION, FUNCTION_DEFINITION, VARIABLE_DECLARATION
    }
}
