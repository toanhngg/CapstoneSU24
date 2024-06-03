package fpt.CapstoneSU24.dto;

public class ChangePasswordDto {
    private String email;

    private String password;

    private String oldPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String email, String password, String oldPassword) {
        this.email = email;
        this.password = password;
        this.oldPassword = oldPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
