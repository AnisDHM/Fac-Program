package model.dao;
import model.entities.*;
import java.util.*;
import java.io.*;

public class DataManager {
    private static DataManager instance;

    private Map<String , User> users;
    private Map<String ,model.entities.Module> modules;
    private List<Grade> grades;
    private List<Absence> absences;
    private List<Inscription> inscriptions;

    private DataManager(){
        users = new HashMap<>();
        modules = new HashMap<>();
        grades = new ArrayList<>();
        absences = new ArrayList<>();
        inscriptions = new ArrayList<>();
        initializeDefaultData();
    }
    
    public static synchronized DataManager getInstance(){
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }
    private void initializeDefaultData(){
        addUser(new Student("12345678", "pass123", "DAHMANI", "Mohamed Anis"));
        addUser(new Student("13456789", "pass132", "SELLALI", "Mohamed"));
        addUser(new Professor("23456789", "prof123", "Dr. BENALI", "Ahmed"));
        addUser(new ViceDean("34567890", "dean123", "Dr. Sarah", "Amrani"));
        addModule(new model.entities.Module("GL", "Génie Logiciel", 6));
        addModule(new model.entities.Module("IA", "Intelligence Artificielle", 6));
        addModule(new model.entities.Module("BD", "Base de Données", 5));
    }   

    public void addUser(User user){
        users.put(user.getCode() , user);
    }

    public User getUser(String code){
        return users.get(code);
    }


    public void addModule(model.entities.Module module){
        modules.put(module.getCode() , module);
    }


    public List<model.entities.Module> getAllModules(){
        return new ArrayList<>(modules.values());
    }
    public model.entities.Module getModule(String code) {
        return modules.get(code);
    }

    public void addGrade(Grade grade){
        grades.add(grade);
    }

    public List<Grade> getStudentGrades (String studentCode){
        List<Grade> studentGrades = new ArrayList<>();
        for(Grade grade : grades){
            if(grade.getStudentCode().equals(studentCode)){
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }

    public void addAbsence(Absence absence){
        absences.add(absence);
    }

    public List<Absence> getStudentAbsences(String studentCode){
        List<Absence> studentAbsences = new ArrayList<>();
        for(Absence absence : absences){
            if(absence.getStudentCode().equals(studentCode)){
                studentAbsences.add(absence);
            }
        }
        return studentAbsences;

    }

    public void addInscription(Inscription inscription){
        inscriptions.add(inscription);
    }

    public List<Student> getAllStudents(){
        List<Student> students = new ArrayList<>();
        for(User user : users.values()){
            if(user instanceof Student){
                students.add((Student) user);
            }
        }
        return students;
    }
    public List<Professor> getAllProfessors() {
        List<Professor> professors = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Professor) {
                professors.add((Professor) user);
            }
        }
        return professors;
    }

    public List<ViceDean> getAllViceDeans() {
        List<ViceDean> viceDeans = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof ViceDean) {
                viceDeans.add((ViceDean) user);
            }
        }
        return viceDeans;
    }

    public List<Inscription> getStudentInscriptions(String studentCode) {
        List<Inscription> studentInscriptions = new ArrayList<>();
        for (Inscription inscription : inscriptions) {
            if (inscription.getStudentCode().equals(studentCode)) {
                studentInscriptions.add(inscription);
            }
        }
        return studentInscriptions;
    }

    public List<Student> getStudentsInModule(String moduleCode) {
        List<Student> students = new ArrayList<>();
        for (Inscription inscription : inscriptions) {
            if (inscription.getModuleCode().equals(moduleCode)) {
                User user = users.get(inscription.getStudentCode());
                if (user instanceof Student) {
                    students.add((Student) user);
                }
            }
        }
        return students;
    }
    public List<model.entities.Module> getStudentModules(String studentCode) {
        List<model.entities.Module> studentModules = new ArrayList<>();
        for (Inscription inscription : inscriptions) {
            if (inscription.getStudentCode().equals(studentCode)) {
                model.entities.Module module = modules.get(inscription.getModuleCode());
                if (module != null) {
                    studentModules.add(module);
                }
            }
        }
        return studentModules;
    }
    
    public void saveToFile(String filename){
        try(ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(filename))){
                oos.writeObject(users);
                oos.writeObject(modules);
                oos.writeObject(grades);
                oos.writeObject(absences);
                oos.writeObject(inscriptions);
            }catch(IOException e){
                e.printStackTrace();
            }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            users = (Map<String, User>) ois.readObject();
            modules = (Map<String, model.entities.Module>) ois.readObject();
            grades = (List<Grade>) ois.readObject();
            absences = (List<Absence>) ois.readObject();
            inscriptions = (List<Inscription>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Fichier n'existe pas ou erreur, garder les données par défaut
            System.out.println("Chargement des données par défaut");
        }
    }


}
