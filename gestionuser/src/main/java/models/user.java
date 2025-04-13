package models;

public class user {
    private int id;
    private String name;
    private String email;
    private String password;
    private String imageProfile;



    public user(){}

    public user( String name, String email, String password, String imageProfile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageProfile = imageProfile;
    }

    public user(Long id, String name, String email, String password, String imageProfile) {
        this.id = Math.toIntExact(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageProfile = imageProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imageProfile='" + imageProfile + '\'' +
                '}';
    }
}
