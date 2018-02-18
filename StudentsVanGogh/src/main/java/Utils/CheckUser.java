package Utils;

import java.io.*;

public class CheckUser {
    public  static void deleteUser(int IDstudent) throws IOException {
        File inputFile = new File("users.txt");
        File tempFile = new File("usersTemp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = "student" + IDstudent;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.contains(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);




    }

    public static void writeUser(int IDstudent, String email) {
        String fileName = "users.txt";

        String line = null;
        try {
            FileWriter fileWriter = new FileWriter(fileName,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("student" + IDstudent + "," + "passwd" + IDstudent + "," + email + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEmail(String username) {
        String fileName = "users.txt";

        String line = null;
        String[] elems;
        String fileUsername;
        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                elems = line.split(",");
                fileUsername = elems[0];
                if (fileUsername.equals(username)) {
                    bufferedReader.close();
                    return elems[2];
                }
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return null;
    }

    public static boolean checkUsername(String username) {
        String fileName = "users.txt";

        String line = null;
        String[] elems;
        String fileUsername;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                elems = line.split(",");
                fileUsername = elems[0];
                if (fileUsername.equals(username)) {
                    bufferedReader.close();
                    return true;
                }
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return false;
    }

    public static String getPasswd(String username) {
        String fileName = "users.txt";

        String line = null;
        String[] elems;
        String fileUsername;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                elems = line.split(",");
                fileUsername = elems[0];
                if (fileUsername.equals(username)) {
                    bufferedReader.close();
                    return elems[1];
                }
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return null;
    }

    public static boolean checkUser(String username, String password) {
        String fileName = "users.txt";

        String line = null;
        String[] elems;
        String fileUsername;
        String filePassword;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                elems = line.split(",");
                fileUsername = elems[0];
                filePassword = elems[1];
                if (fileUsername.equals(username)) {
                    if (filePassword.equals(password))
                        return true;
                }
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return false;
    }
}
