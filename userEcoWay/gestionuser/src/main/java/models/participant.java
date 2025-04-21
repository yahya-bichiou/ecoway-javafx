package models;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;*/

//@Entity

public class participant {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private int id;
    private String nom;
    private int age;
   // @Column(unique = true)
    private String email;
   // @Column(unique = true)
    private String telephone;
    private int env_id;




    public participant() {}

    public participant(String nom, int age, String email, String telephone, int env_id) {
        this.nom = nom;
        this.age = age;
        this.email = email;
        this.telephone = telephone;
        this.env_id = env_id;
    }

    public participant(String nom, int age, String email, String telephone, int env_id, int id) {
        this.nom = nom;
        this.age = age;
        this.email = email;
        this.telephone = telephone;
        this.env_id = env_id;
        this.id = id;
    }

    // Getters et setters restent les mêmes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEnv_id() {
        return env_id;
    }

    public void setEnv_id(int env_id) {
        this.env_id = env_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}