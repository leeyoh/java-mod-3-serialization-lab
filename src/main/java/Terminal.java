import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class Terminal {
    private static PersonDirectory pd;
    private static ScannerSingleton sc;
    private static LoggerSingleton log;
    private static FileManagerSingleton fm;
    private static String fileName = "list";
    private static final String path = "data/";
    private static String filePath = path + fileName;
    private static final String directory = "persons/";

    public static void main(String[] args) {
        initializeObjects();
        loadFile();
        chooseOptions();
        if(sc != null) {
            sc.close();
        }
    }
    public static void initializeObjects(){
        pd = new PersonDirectory();
        sc = ScannerSingleton.getInstance();
        log = LoggerSingleton.getInstance();
        fm = FileManagerSingleton.getInstance();
    }
    public static void chooseOptions(){
        log.log(" [ 1 ] Add a person to the list");
        log.log(" [ 2 ] List of current persons (Serialized) ");
        log.log(" [ 3 ] Exit the program");
        log.prompt(":");
        switch(sc.getInt(3)){
            case 1:
                //Add Person to List
                addPerson();
                chooseOptions();
                break;
            case 2:
                //List the current list of people, old + new
                loadPerson();
                chooseOptions();
                break;
            case 3:
                //Append List to file then close program
                pd.saveToFile(filePath + ".csv");
                break;
            default:
                chooseOptions();
                break;
        }
    }
    public static void loadPerson(){
        try{
            log.log("People in Directory:");
            List<String> files = fm.listFilesUsingJavaIO("persons");
            if(files.isEmpty()){
                log.log("No People");
            } else {
                log.log("Load Index to List");
                int i = 0;

                for(; i < files.size(); i++){
                    log.log(String.valueOf(i) + ": " + files.get(i).toString());
                }

                log.prompt("selected index ( 0 - " + String.valueOf(i-1) + " ): ");
                int index = sc.getInt(i);
                String filepath = files.get(index);
                Person tempPerson = pd.deserializePerson(filepath);
                pd.addPerson(tempPerson);
                pd.printDirectory();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static void printOption(){
        log.log(" [1] CSV");
        log.log(" [2] JSON");
        log.prompt(": ");
        switch(sc.getInt(2)){
            case 2:
                break;
            default:
                pd.printDirectory();
                break;
        }
    }
    /**
     * First Name ( String )
     * Last Name
     * Birth Year
     * Birth Month
     * Birth Day
     */
    public static void addPerson() {
        String firstName,lastName,year,month,day;

        log.prompt("First Name: ");
        firstName = sc.getNextLine();

        log.prompt("Last Name: ");
        lastName = sc.getNextLine();

        log.log("BirthDay");
        year = getYear();
        month = getMonth();
        day = getDay(year,month);

        Person p = pd.addPerson(firstName,lastName,
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day));
        try{
            pd.serializePerson(directory,p);
        }catch(Exception e){
            System.out.println(e);
        }

    }
    public static String getYear(){
        log.prompt("Year: ");
        int year = sc.getInt(9999);
        if(year < 1){
            getYear();
        }
        return String.valueOf(year);
    }

    public static String getMonth(){
        log.prompt("Month: ");
        String month = sc.getMonth();
        if(month == "0"){
            getMonth();
        }
        return month;
    }

    public static String getDay(String year, String month){
        log.prompt("Day: ");
        String day = String.valueOf(sc.getInt(31));

        if(!isValid(year + '-' + month + "-" + day)){
            log.error("Invalid Day");
            getDay(year,month);
        }
        return day;
    }
    //https://mkyong.com/java/how-to-check-if-date-is-valid-in-java/
    public static boolean isValid(final String date) {
        boolean valid = false;
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            //e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public static void loadPersonal(){
        try{
            pd.loadDirectory(filePath+ ".csv");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void loadFile(){
        try{
            log.log("Files in Directory:");
            List<String> files = fm.listFilesUsingJavaIO("data");
            if(files.isEmpty()){
                log.log("Created New File: " + filePath + ".csv");
                fm.createFile(filePath + ".csv");
            } else {
                fileName = files.get(0);
                log.log("Loaded " + filePath+ ".csv");
                loadPersonal();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static void clearScreen(){
        final int MAX_LINES = 50;
        for(int i = 0; i < MAX_LINES; i++){
            log.log("-");
        }
    }
}
