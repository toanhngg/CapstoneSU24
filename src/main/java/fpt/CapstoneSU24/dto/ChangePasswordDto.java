package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ChangePasswordDto {
    @NotBlank(message = "The password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$", message = "Password must be 8 characters long and a combination of uppercase letters, lowercase letters, numbers, and special characters.")
    private String password;

    @NotBlank(message = "The confirm password is required")
    private String confirmPassword;

    @NotBlank(message = "The old password is required")
    private String oldPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String password, String confirmPassword, String oldPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}