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
                    while (readLine.length() == 0) {
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
                     System.out.println("Variable dec here-->" + sCurrLine);
                    String var_type = null;
                    String var_name = null;
                    String value = null;
                    boolean extracted_type = false;
                    boolean extracted_var = false;
                    boolean extracted_value = false;
                    boolean valid_type = true;
                    boolean valid_name = true;
                    boolean valid_value = true;
                    boolean declaration = false;
                    int new_index = 0;

                    if(sCurrLine.contains("=")){
                        if(sCurrLine.contains(",")){
                            String [] array = new String[20];
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        int cntr = 0;
                                        System.out.println(sCurrLine.charAt(new_index));
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                array[cntr] = var_name;
                                                extracted_var = true;
                                            }else{
                                                if(sCurrLine.charAt(new_index) == ','){
                                                    array[cntr] = var_name;
                                                    cntr++;
                                                    store_index = new_index+1;
                                                    new_index++;
                                                }else{
                                                    var_name = sCurrLine.substring(store_index, new_index +1);
                                                    new_index++;
                                                }


                                            }
                                        }
                                        if(extracted_var){
                                            boolean separate = false;
                                            int cntr2 = 0;
                                            String value2 = null;
                                            for(int a = 0; a <= cntr; a++){
                                                var_name = array[a];
                                                System.out.println("Variable name --->" + var_name);
                                                if(var_name.contains("=")){
                                                    if(var_name.charAt(cntr2) == '='){
                                                        //value2 = var_name.substring()
                                                        System.out.println(var_name);

                                                        break;
                                                    }else{
                                                        var_name = var_name.substring(0, cntr+1);
                                                        cntr++;
                                                    }
                                                }
                                                valid_name = checkNameIfValid(var_name);
                                                if(valid_name){
                                                    declaration = true;
                                                }else{
                                                    declaration = false;
                                                }

                                            }

                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                    }


                                }
                            }

                        }else{
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    int store_index = 0;
                                    valid_type = checkTypeIfValid(var_type);
                                    if(valid_type){
                                        store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == '=' ){
                                                extracted_var = true;
                                            }else{
                                                var_name = sCurrLine.substring(store_index, new_index +1);
                                                new_index++;
                                            }
                                        }
                                        if(extracted_var){
                                            System.out.println("Variable name --->" + var_name);
                                            valid_name = checkNameIfValid(var_name);
                                            if(valid_name){
                                                System.out.println(new_index);
                                                store_index = new_index+1;
                                                new_index = store_index;
                                                while(!extracted_value){
                                                    if(sCurrLine.charAt(new_index) == ';'){
                                                        extracted_value = true;
                                                    }else{
                                                        value = sCurrLine.substring(store_index, new_index +1);
                                                        new_index++;
                                                    }

                                                }
                                                if(extracted_value){
                                                    System.out.println("Variable value --->" + value);
                                                    valid_value = checkValueIfValid(var_type, value);

                                                    if(valid_value){

                                                        declaration = true;

                                                    }else{
                                                        declaration = false;
                                                    }
                                                }

                                            }else{
                                                declaration = false;
                                            }
                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                        break;
                                    }
                                }

                            }
                        }

                    }else{

                        String[] array = new String[20];
                        if(sCurrLine.contains(",")){
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        int cntr = 0;
                                        System.out.println(sCurrLine.charAt(new_index));
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                array[cntr] = var_name;
                                                extracted_var = true;
                                            }else{
                                                if(sCurrLine.charAt(new_index) == ','){
                                                    array[cntr] = var_name;
                                                    cntr++;
                                                    store_index = new_index+1;
                                                    new_index++;
                                                }else{
                                                    var_name = sCurrLine.substring(store_index, new_index +1);
                                                    new_index++;
                                                }


                                            }
                                        }
                                        if(extracted_var){
                                            for(int a = 0; a <= cntr; a++){
                                                var_name = array[a];
                                                System.out.println("Variable name --->" + var_name);
                                                valid_name = checkNameIfValid(var_name);
                                                if(valid_name){
                                                    declaration = true;
                                                }else{
                                                    declaration = false;
                                                }

                                            }


                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                    }


                                }
                            }


                        }else{
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                extracted_var = true;
                                            }else{
                                                var_name = sCurrLine.substring(store_index, new_index +1);
                                                new_index++;
                                            }
                                        }
                                        if(extracted_var){
                                            System.out.println("Variable name --->" + var_name);
                                            valid_name = checkNameIfValid(var_name);
                                            if(valid_name){
                                                declaration = true;
                                            }else{
                                                declaration = false;
                                            }
                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    if (declaration) {
                        valid = true;
                    } else { valid = false;
                    }
                } else if (type == Type.FUNCTION_DECLARATION) { // TODO FUNCTION DECLARATION (MM, start here)

                } else { // TODO FUNCTION DEFINITION (Vince, start here)
                    System.out.println("Staring");
                    // Inspecting function/variable name
                    for (; sCurrLine.charAt(i) == ' '; i++);
                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        terminate();
                        System.out.println("Naming deficiency at first letter.");
                    } else {
                        String funcDefName = sCurrLine.charAt(i) + "";
                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) {
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <= '9')) {
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
                                } else if (sCurrLine.charAt(i) != ' ') {
                                    System.out.println("Invalid function name.");
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
                                    if (param.length() == 0 && sCurrLine.charAt(i) != ')') {
                                        System.out.println("Hmm. Something's not right.");
                                        terminate();
                                        break;
                                    } else if (sCurrLine.charAt(i) != ')' || param.length() > 0){

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
                                                for (String s : dataTypes) {
                                                    if (s.equals(parameter_name)) {
                                                        valid = false;
                                                        break;
                                                    }
                                                }
                                                if (!valid || parameter_name.equals("void")) {
                                                    System.out.println("Invalid name. Should not be a data type.");
                                                    terminate();
                                                } else {
                                                    fDef_DeclVar.add(parameter_name);
                                                }
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
                                    while (readLine.isEmpty()) {
                                        readLine = br.readLine();
                                    }
                                    System.out.println("Readline's content: " + readLine);
                                    readLine = readLine.trim();
                                    while (valid && readLine.charAt(0) != '}') {
                                        sCurrLine = "";
                                        readLine = readLine.trim();
                                        while (readLine.isEmpty()) {
                                            readLine = br.readLine();
                                        }
                                        for (i = 0; readLine.charAt(0) != ';'; i++) {
                                            sCurrLine = sCurrLine.concat(readLine.charAt(0) + "");
                                            readLine = readLine.substring(1);
                                            while (readLine.isEmpty()) {
                                                //System.out.println("Empty. Finding next.");
                                                readLine = br.readLine();
                                            }
                                        }
                                        sCurrLine = sCurrLine.trim();
                                        sCurrLine = sCurrLine.concat(";");
                                        System.out.println("sCurrLine's content: " + sCurrLine);
                                        readLine = readLine.substring(1);
                                        if (sCurrLine.startsWith("return ")) {
                                            System.out.println("A return value");
                                            sCurrLine = sCurrLine.substring(7);
                                            System.out.println(sCurrLine);

                                        } else {
                                            String identifier = "";
                                            System.out.println("sCurrLine: " + sCurrLine);
                                            while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != '=') {
                                                System.out.println("Character is " + sCurrLine.charAt(0));
                                                if (sCurrLine.charAt(0) == ';') {
                                                    System.out.println("Seriously dude. It's plain wrong.");
                                                    terminate();
                                                    break;
                                                }
                                                identifier = identifier.concat(sCurrLine.charAt(0) + "");
                                                sCurrLine = sCurrLine.substring(1);
                                            }
                                            if (valid) {
                                                boolean varDec = false;
                                                for (String s : dataTypes) {
                                                    if (s.equals(identifier)) {
                                                        varDec = true;
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                if (varDec) {
                                                    System.out.println("A variable declaration");
                                                    if (sCurrLine.charAt(0) == '=') {
                                                        System.out.println("Invalid variable name (which is null).");
                                                        terminate();
                                                        break;
                                                    }
                                                    identifier = "";
                                                    while (sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '=') {
                                                        identifier = identifier.concat(sCurrLine.charAt(0) + "");
                                                        sCurrLine = sCurrLine.substring(1);
                                                    }
                                                    sCurrLine = sCurrLine.trim();
                                                    identifier = identifier.trim();
                                                    if (identifier.contains(" ")) {
                                                        System.out.println("Invalid variable name");
                                                        terminate();
                                                        break;
                                                    }
                                                    if (!(identifier.charAt(0) == '_' || (identifier.charAt(0) >= 'A' && identifier.charAt(0) <= 'Z') || (identifier.charAt(0) >= 'a' && identifier.charAt(0) <= 'z'))) {
                                                        terminate();
                                                        System.out.println("Naming deficiency at first letter.");
                                                        break;
                                                    }
                                                    for (int j = 1; j < identifier.length(); j++) {
                                                        if (!(identifier.charAt(j) == '_' || (identifier.charAt(j) >= 'A' && identifier.charAt(j) <= 'Z') || (identifier.charAt(j) >= 'a' && identifier.charAt(j) <= 'z') || (identifier.charAt(j) >= '0' && identifier.charAt(j) <= '9'))) {
                                                            System.out.println("Naming deficiency at next letters: " + sCurrLine.charAt(j));
                                                            terminate();
                                                            break;
                                                        }
                                                    }

                                                    boolean varDeclared = false;
                                                    for (String s : fDef_DeclVar) {
                                                        if (s.equals(identifier)) {
                                                            varDeclared = true;
                                                        }
                                                    }
                                                    if (varDeclared) {
                                                        System.out.println("Redeclaration of variable");
                                                        terminate();
                                                        break;
                                                    } else {
                                                        for (String s : dataTypes) {
                                                            if (s.equals(identifier)) {
                                                                valid = false;
                                                                break;
                                                            }
                                                        }
                                                        if (!valid || identifier.equals("void")) {
                                                            System.out.println("Invalid name. Should not be a data type.");
                                                            terminate();
                                                        } else {
                                                            fDef_DeclVar.add(identifier);
                                                        }
                                                    }
                                                }
                                                if (!((varDec && sCurrLine.charAt(0) == ';') || sCurrLine.charAt(0) == '=')) {
                                                    System.out.println("haha dude are you trying to make me laugh?");
                                                    terminate();
                                                    break;
                                                } else if (sCurrLine.charAt(0) == '=') {
                                                    sCurrLine = sCurrLine.substring(1);
                                                }
                                            }
                                        }
                                        if (valid) {
                                            int parentheses = 0;
                                            String tempSCurrLine = "";
                                            for (int j = 0; j < sCurrLine.length(); j++) {
                                                System.out.println("s " + tempSCurrLine);
                                                if (sCurrLine.charAt(j) == '(') {
                                                    parentheses++;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                    if (sCurrLine.charAt(j + 1) != '(') {
                                                        j++;
                                                        tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                    }
                                                } else if (sCurrLine.charAt(j) == ')') {
                                                    parentheses--;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                } else {
                                                    tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                }
                                                System.out.println(tempSCurrLine + " is temp");
                                            }
                                            sCurrLine = tempSCurrLine;
                                            System.out.println(sCurrLine);
                                            if (parentheses != 0) {
                                                System.out.println("Expected parenthesis.");
                                                terminate();
                                                break;
                                            }
                                            System.out.println("Parenthesis cleared");
                                            boolean operationDetected = false;
                                            System.out.println("Valid: " + valid);
                                            System.out.println("scurrlayn: " + sCurrLine);
                                            while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                System.out.println("scurrline: " + sCurrLine);
                                                String returning = "";
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println("sCurrLine's content for god's sake: " + sCurrLine);
                                                while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                    System.out.println("Sending " + sCurrLine.charAt(0));
                                                    sCurrLine = sCurrLine.substring(1);
                                                }
                                                try {
                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                    System.out.println(sCurrLine + "(!)");
                                                    operationDetected = false;
                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                    if (returning.contains("'")) { // Might be a character...
                                                        if (returning.length() <= 2 || returning.length() >= 5) {
                                                            System.out.println("Too much or too less character.");
                                                            terminate();
                                                            break;
                                                        }
                                                        int quoteCtr = 0;
                                                        for (int j = 0; j < returning.length(); j++) {
                                                            if (returning.charAt(j) == '\'') {
                                                                quoteCtr++;
                                                            }
                                                        }
                                                        if (quoteCtr != 2) {
                                                            System.out.println("Quotes must be two.");
                                                            terminate();
                                                            break;
                                                        }
                                                        if (returning.length() == 4 && !returning.contains("\\")) {
                                                            System.out.println("Special case is not met.");
                                                            terminate();
                                                            break;
                                                        }
                                                        operationDetected = false;
                                                    } else {
                                                        boolean variableExists = false;
                                                        for (String s : fDef_DeclVar) {
                                                            if (s.equals(returning)) {
                                                                variableExists = true;
                                                            }
                                                        }
                                                        if (!variableExists) {
                                                            System.out.println("Return value is non-existent: " + returning);
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!returning.isEmpty()) {
                                                            operationDetected = false;
                                                        }
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println(sCurrLine + "(!)");
                                                if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                        operationDetected = true;
                                                        sCurrLine = sCurrLine.substring(1);
                                                        if (returning.isEmpty()) {
                                                            System.out.println("Expected token.");
                                                            terminate();
                                                            break;
                                                        }
                                                    } else {
                                                        System.out.println("Whoa man. We expect an operation");
                                                        terminate();
                                                        break;
                                                    }
                                                }
                                                System.out.println(sCurrLine + "(!)");

                                                System.out.println(sCurrLine + "(!)");
                                            }
                                            System.out.println("readline is " + readLine);
                                            if (valid) {
                                                readLine = readLine.trim();
                                                while (readLine.isEmpty()) {
                                                    readLine = br.readLine();
                                                    readLine = readLine.trim();
                                                }
                                                System.out.println("readline is " + readLine);
                                            }
                                        }
                                    }
                                    System.out.println("We found the ender!");
                                    readLine = readLine.substring(1);
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
    //check if the declared value is valid
    private static boolean checkValueIfValid(String type, String value){
        boolean isValid = false;
        if(value == null){
            isValid = false;
        }else {
            boolean not_empty = false;
            for(int i = 0; i < value.length(); i++){
                if(value.charAt(i)!= ' '){
                    not_empty = true;
                }
            }
            if(not_empty){
                int s = 0;
                if (value.charAt(0) == ' ') {
                    while (value.charAt(s) == ' ') {
                        s++;
                    }
                    value = value.substring(s, value.length());
                }
                System.out.println("New value-->" + value);
                int z = value.length() - 1;
                if (value.charAt(z) == ' ') {
                    while (value.charAt(z) == ' ') {
                        z--;
                    }
                    value = value.substring(0, z + 1);

                }

                if (type.equals("int")) {
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        if(value.length() == 1){
                            if(value.charAt(0) == '-'){
                                isValid = false;
                            }else{
                                if(value.charAt(0) >= '0' && value.charAt(0) <= '9'){
                                    isValid = true;
                                }else{
                                    isValid = false;
                                }
                            }

                        }else{

                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if(value.charAt(j) >= '0' && value.charAt(j) <= '9'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else{
                                for(int i = 0; i < value.length(); i++){
                                    if(value.charAt(i) >= '0' && value.charAt(i) <= '9'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } else if (type.equals("float")) {
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        int cnt = 0;
                        for (int i = 0; i < value.length(); i++) {
                            if (value.charAt(i) == '.') {
                                cnt++;
                            }
                        }

                        if (cnt > 1) {
                            return false;
                        } else {
                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if((value.charAt(j) >= '0' && value.charAt(j) <= '9') || value.charAt(j) == '.'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else {
                                for (int i = 0; i < value.length(); i++) {
                                    if ((value.charAt(i) >= '0' && value.charAt(i) <= '9') || value.charAt(i) == '.') {
                                        isValid = true;
                                    } else {
                                        isValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                } else if (type.equals("char")) {
                    if (value.contains("'")){
                        if((value.length() == 3) && (value.charAt(0) == '\'')){
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            } else{
                                isValid = false;
                            }
                        }else{
                            isValid = false;
                        }
                    }else{
                        for(int i = 0; i < value.length(); i++){
                            if(value.charAt(i) >= '0' && value.charAt(i) <= '9'){
                                isValid = true;
                            }else{
                                isValid = false;
                                break;
                            }
                        }
                    }

                } else { //means it's a double type
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        int cnt = 0;
                        for (int i = 0; i < value.length(); i++) {
                            if (value.charAt(i) == '.') {
                                cnt++;
                            }
                        }

                        if (cnt > 1) {
                            return false;
                        } else {
                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if((value.charAt(j) >= '0' && value.charAt(j) <= '9') || value.charAt(j) == '.'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else {
                                for (int i = 0; i < value.length(); i++) {
                                    if ((value.charAt(i) >= '0' && value.charAt(i) <= '9') || value.charAt(i) == '.') {
                                        isValid = true;
                                    } else {
                                        isValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                }
            }else{
                isValid = false;
            }


        }
        System.out.println("New value-->" + value);
        if(!(type.equals("char"))){
            if(isValid){
                isValid = checkRangeIfValid(type, value);
            }
        }

        return isValid;
    }
    private static boolean checkRangeIfValid(String type, String value){
        int isInteger;
        float isFloat;
        double isDouble;
        //try catch
        if (value.contains("'")) {
            if (value.length() == 3 && value.charAt(0) == '\'') {
                if(value.charAt(2) == '\''){
                    return true;
                }else{
                    return false;
                }
            } else {
                return false;
            }
        }
        else{
            if(type.equals("int")){
                try {
                    isInteger = Integer.parseInt(value);
                    return true;
                }catch  (Exception e){
                    System.out.println("Error: " + e);
                    return false;
                }

            }else if(type.equals("float")){
                try {
                    isFloat = Float.parseFloat(value);
                    return true;
                }catch  (Exception e){
                    System.out.println("Error: " + e);
                    return false;
                }
            }else { //double
                try {
                    isDouble = Double.parseDouble(value);
                    return true;
                }catch  (Exception e){
                    System.out.println("Error: " + e);
                    return false;
                }
            }
        }


    }
    //check if the declared type is valid
    private static boolean checkTypeIfValid(String type){
        if(type.equals("int")){
            return true;
        }else if(type.equals("float")){
            return true;
        }else if(type.equals("char")){
            return true;
        }else if(type.equals("double")){
            return true;
        }else{
            return false;
        }
    }

    //check if the declared variable name is valid
    private static boolean checkNameIfValid(String name){
        boolean isValid = false;
        if(name == null){
            isValid = false;
        }else {
            boolean not_empty = false;
            for(int i = 0; i < name.length(); i++){
                if(name.charAt(i)!= ' '){
                    not_empty = true;
                }
            }if(not_empty){
                int s = 0;

                if (name.charAt(0) == ' ') {
                    while (name.charAt(s) == ' ') {
                        s++;
                    }
                    name = name.substring(s, name.length());
                }
                System.out.println("New var name-->" + name);
                int z = name.length() - 1;
                if (name.charAt(z) == ' ') {
                    while (name.charAt(z) == ' ') {
                        z--;
                    }
                    name = name.substring(0, z + 1);

                }
                System.out.println("New var name-->" + name);
                if(!((name.equals("int")) || (name.equals("float")) || (name.equals("double") || (name.equals("char"))))){
                    for (int i = 0; i < name.length(); i++) {
                        if (i == 0) {
                            if ((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') || (name.charAt(i) == '_')) {
                                isValid = true;
                            } else {
                                isValid = false;
                                break;
                            }
                        } else {
                            if ((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') || (name.charAt(i) == '_') || (name.charAt(i) >= '0' && name.charAt(i) <= '9')) {
                                isValid = true;
                            } else {
                                isValid = false;
                                break;
                            }
                        }

                    }
                }else{
                    isValid = false;
                }

            }else{
                isValid = false;
            }
        }
        return isValid;
    }

    private static void terminate() {
        try {
            valid = false;
            wr.append("INVALID FUNCTION DEFINITION");
            System.out.println(readLine + " is readline");
            while (readLine.length() == 0) {
                readLine = br.readLine();
            }
            while (readLine.charAt(0) != '}') {
                readLine = readLine.substring(1);
                while (readLine.length() == 0) {
                    readLine = br.readLine();
                }
            }
        } catch (IOException e) {
            // do nothing
        }
    }

    private enum Type {
        FUNCTION_DECLARATION, FUNCTION_DEFINITION, VARIABLE_DECLARATION
    }
}
