package models;

public class user {
    private int id;
    private String name;
    private String email;
    private String password;
    private String profile_picture;
    private String roles;
    private String resetToken;
    private boolean blocked;





    public user(){}

    public user( String name, String email, String password, String profile_picture, String roles, String resetToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.roles = roles;
        this.resetToken = resetToken;
    }

    public user(Long id, String name, String email, String password, String profile_picture, String roles, String resetToken) {
        this.id = Math.toIntExact(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.roles = roles;
        this.resetToken = resetToken;
    }

    public user(String name, String email, String password, String profile_picture, String roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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
        return profile_picture;
    }

    public void setImageProfile(String profile_picture) {
        this.profile_picture = profile_picture;
    }
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }



    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                '}';
    }
}
