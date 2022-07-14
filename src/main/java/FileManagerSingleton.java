import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManagerSingleton {
    private static FileManagerSingleton fileManager = null;
    private FileManagerSingleton(){

    }
    public static FileManagerSingleton getInstance(){
        if(fileManager == null){
            fileManager = new FileManagerSingleton();
        }
        return fileManager;
    }

    /**
     * Returns a list of Strings if the file exists.
     * If the file doesn't exist, throws a doesn't exist Exception. \
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<String> readFromFile(String fileName) throws IOException {
        //https://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        return lines;
    }

    /**
     * Lists all the Files in a Directory
     * @param dir
     * @throws IOException
     */
    public List<String> listFilesUsingJavaIO(String dir) throws IOException {
        List<String> fileNames = new ArrayList<String>();
        //https://zetcode.com/java/listdirectory/
        Files.list(new File(dir).toPath())
                .limit(10)
                .forEach(path -> {
                    System.out.println(path);
                    fileNames.add(String.valueOf(path));
                });
        return fileNames;
    }

    public void createFile(String fileName){
        FileWriter myObj;
        try {
            myObj = new FileWriter(fileName);
            System.out.println("File created ");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void appendToFile(String fileName, StringBuffer values) {
        System.out.println(fileName);
        try(FileWriter myObj = new FileWriter(fileName,true)){
            myObj.append(values);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }


}
