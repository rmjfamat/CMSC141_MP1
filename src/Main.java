import java.io.*;
import java.util.ArrayList;
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
    private static final List<String> dataTypes = Arrays.asList("int", "short", "long", "float", "double", "char", "return"); // FIXME eliminate return and make some special function to handle this
    private static boolean valid = true;
    private static String readLine = "";

    public static void main(String[] args) {
        int types = 0;
        String sCurrLine;
        Type type;
        List<String> fDef_DeclVar;

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
            System.out.println(types);
            while (--types >= 0) {
                type = null;
                fDef_DeclVar = new ArrayList<>();
                valid = true;
                sCurrLine = "";
                boolean testCaseSearch = true;
                do {
                    if (readLine.length() == 0) {
                        sCurrLine = sCurrLine.concat(" ");
                        readLine = br.readLine();
                    }
                    System.out.println(readLine + " and " + sCurrLine + " where readline is " + readLine.length());

                    while (readLine.length() > 0) {
                        sCurrLine = sCurrLine.concat(readLine.charAt(0) + "");
                        if (readLine.charAt(0) == ';' || readLine.charAt(0) == '{') {
                            readLine = readLine.substring(1);
                            testCaseSearch = false;
                            break;
                        }
                        readLine = readLine.substring(1);
                    }

                } while (testCaseSearch);

                // FIXME eliminate debug
                wr.append(String.valueOf(types));
                System.out.println(types + "");
                wr.newLine();

                sCurrLine = sCurrLine.trim(); // Removing redundant spaces, if any are present
                System.out.println("TESTCASE: " + sCurrLine);
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
                            terminate();
                            wr.append(" DEFINITION");
                        } else {
                            wr.append(" DECLARATION");
                        }
                    } else {
                        wr.append("VARIABLE DECLARATION");
                    }
                }
                String returnType = "";
                int i;
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
                if (valid && type.toString().contains("FUNCTION")) {
                    if (returnType.equals("void")) {
                        validRetType = true;
                    }
                }

                if (!validRetType) {
                    wr.append("INVALID ").append(type.toString());
                    valid = false;
                }

                i++;
                /**
                 * The string is stored in the sCurrLine. [sCurrLine = "int x = 0;"]
                 * I already have checked the return type and the variable type. ["int"]
                 * The incrementation of i indicates the start of the variable name or function name.
                 * Example:
                 * int x = 0;
                 * i is pointing at x already.
                 *
                 */
                if (type == Type.VARIABLE_DECLARATION) { // TODO VARIABLE DECLARATION (Ruffa, start here)

                } else if (type == Type.FUNCTION_DECLARATION) { // TODO FUNCTION DECLARATION (MM, start here)

                } else { // TODO FUNCTION DEFINITION (Vince, start here)
                    // Inspecting function/variable name
                    for (; sCurrLine.charAt(i) == ' '; i++);
                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        terminate();
                        System.out.println("Naming deficiency at first letter.");
                    } else {
                        String funcDefName = sCurrLine.charAt(i) + "";
                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) {
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <='0')) {
                                funcDefName = funcDefName.concat(sCurrLine.charAt(i) + "");
                            } else {
                                System.out.println("Naming deficiency at next letters: " + sCurrLine.charAt(i));
                                terminate();
                                break;
                            }
                        }
                        if (valid) {
                            System.out.println("Function name cleared.");
                            for (; sCurrLine.charAt(i) != '('; i++){
                                if (i + 1 == sCurrLine.length()) {
                                    System.out.println("EOF");
                                    terminate();
                                    break;
                                }
                            }
                            if (valid) {
                                while (sCurrLine.charAt(i) != ')') {
                                    String param = "";
                                    for (i++; sCurrLine.charAt(i) != ',' && sCurrLine.charAt(i) != ')'; i++) {
                                        param = param.concat(sCurrLine.charAt(i) + "");
                                        System.out.println(sCurrLine.charAt(i) + " inserted");
                                    }
                                    param = param.trim();
                                    if (param.length() == 0) {
                                        System.out.println("Hmm. Something's not right.");
                                        terminate();
                                        break;
                                    } else {

                                        boolean param_type = false;
                                        String parameter_type = "";
                                        while (param.length() > 0 && param.charAt(0) != ' ') {
                                            parameter_type = parameter_type.concat(param.charAt(0) + "");
                                            param = param.substring(1);
                                        }
                                        parameter_type = parameter_type.trim();
                                        for (String s : dataTypes) {
                                            if (parameter_type.equals(s)) {
                                                param_type = true;
                                                break;
                                            }
                                        }
                                        if (!param_type) {
                                            System.out.println("Parameter type error: " + parameter_type);
                                            terminate();
                                            break;
                                        }
                                        boolean param_name = false;
                                        String parameter_name = "";
                                        while(param.length() > 0) {
                                            if ((param.charAt(0) >= 'a' && param.charAt(0) <= 'z') || (param.charAt(0) >= 'A' && param.charAt(0) <= 'Z') || (param.charAt(0) >= '0' && param.charAt(0) <='9') || param.charAt(0) == '_' || param.charAt(0) == ' '){
                                                parameter_name = parameter_name.concat(param.charAt(0) + "");
                                                param = param.substring(1);
                                            } else {
                                                param_name = true;
                                                break;
                                            }
                                        }
                                        parameter_name = parameter_name.trim();
                                        for (String s : fDef_DeclVar) {
                                            if (parameter_name.equals(s)) {
                                                param_name = true;
                                                break;
                                            }
                                        }
                                        if (parameter_name.length() > 0 && (param_name || parameter_name.contains(" ") || (parameter_name.charAt(0) >= '0' && parameter_name.charAt(0) <= '9'))) {
                                            System.out.println("Parameter name error");
                                            terminate();
                                            break;
                                        } else {
                                            if (!parameter_name.isEmpty()) {
                                                fDef_DeclVar.add(parameter_name);
                                            }
                                            System.out.println("Contents of variables declared: " );
                                            for (String s : fDef_DeclVar) {
                                                System.out.print(s + " ");
                                            }
                                        }

                                    }
                                }
                                if (valid) {
                                    for (i++; sCurrLine.charAt(i) != '{'; i++) {
                                        if (sCurrLine.charAt(i) != ' ') {
                                            terminate();
                                            System.out.println("Inability to find EOF");
                                            break;
                                        }
                                    }
                                }
                                if (valid) {
                                    System.out.println("Readline's content: " + readLine);
                                    while (readLine.charAt(0) != '}') {
                                        sCurrLine = "";
                                        readLine = readLine.trim();
                                        if (readLine.isEmpty()) {
                                            readLine = br.readLine();
                                        }
                                        for (i = 0; readLine.charAt(0) != ';'; i++) {
                                            sCurrLine = sCurrLine.concat(readLine.charAt(0) + "");
                                            readLine = readLine.substring(1);
                                            if (readLine.isEmpty()) {
                                                readLine = br.readLine();
                                            }
                                        }
                                        sCurrLine = sCurrLine.trim();
                                        sCurrLine = sCurrLine.concat(";");
                                        readLine = readLine.substring(1);
                                        if (sCurrLine.startsWith("return ")) {
                                            sCurrLine = sCurrLine.substring(7);
                                            // TODO insert fang's something here plus variable checking
                                        } else {
                                            // TODO insert fang's something here plus variable checking
                                        }
                                        readLine = readLine.trim();
                                        while (readLine.isEmpty()) {
                                            readLine = br.readLine();
                                        }
                                    }
                                    System.out.println("We found the ender!");
                                }
                            }
                        }
                    }
                }

                if (valid) {
                    wr.append("VALID ").append(type.toString());
                    System.out.println("VALID po");
                } else {
                    wr.append("INVALID ").append(type.toString());
                    System.out.println("INVALID po");
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

    private static void terminate() {
        try {
            valid = false;
            wr.append("INVALID FUNCTION DEFINITION");
            do {
                if (readLine.length() == 0) {
                    readLine = br.readLine();
                }
                readLine = readLine.substring(1);
                if (readLine.length() == 0) {
                    readLine = br.readLine();
                }
            } while (readLine != null && readLine.charAt(0) != '}');
        } catch (IOException e) {
            // do nothing
        }
    }

    private enum Type {
        FUNCTION_DECLARATION, FUNCTION_DEFINITION, VARIABLE_DECLARATION
    }
}