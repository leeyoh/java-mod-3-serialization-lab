
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDirectory {
    private List<Person> personalList = new ArrayList<Person>();
    private List<Person> oldList = new ArrayList<Person>();

    private FileManagerSingleton fm;

    public PersonDirectory(){
        fm = FileManagerSingleton.getInstance();
    }
    public void saveToFile(String fileName){
        StringBuffer persons = new StringBuffer();
        personalList.forEach((person) ->{
            persons.append(person.formatAsCSV());
            persons.append("\n");
        });
        fm.appendToFile(fileName,persons);
    }
    public void serializePerson(String directory,Person person) throws IOException {
        ObjectOutputStream personObjectStream = null;
        try {
            FileOutputStream personFile = new FileOutputStream(directory + person.getFirstName()+ person.getLastName() +".dat");
            personObjectStream = new ObjectOutputStream(personFile);
            personObjectStream.writeObject(person);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (personObjectStream != null) {
                personObjectStream.flush();
                personObjectStream.close();
            }
        }
    }
    public Person deserializePerson(String filepath) throws IOException {
        ObjectInputStream personInputStream = null;
        Person newPerson = null;
        try {
            FileInputStream personFile = new FileInputStream(filepath);
            personInputStream = new ObjectInputStream(personFile);
            newPerson = (Person) personInputStream.readObject();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (personInputStream != null) {
                personInputStream.close();
            }
        }
        return newPerson;
    }
    public void printDirectory(){
        oldList.forEach((person) ->{
            System.out.println(person);
        });
        personalList.forEach((person) ->{
            System.out.println(person);
        });
    }
    public void loadDirectory(String fileName) throws IOException {
        try{
            List<String> tempList = new ArrayList<String>();
            tempList = fm.readFromFile(fileName);
            tempList.forEach((csvPerson) -> {
                oldList.add(new Person(csvPerson));
            });
        }catch(Exception e){
            throw e;
        }
    }
    public Person addPerson(String firstName, String lastName, int birthYear, int birthMonth, int birthDay){
        Person p = new Person(firstName,lastName,birthYear,birthMonth,birthDay);
        personalList.add(p);
        return p;
    }
    public void addPerson(String personCSV) {
        personalList.add(new Person(personCSV));
    }
    public void addPerson(Person person){
        personalList.add(person);
    }

}
