package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegisterForSupporterRequest {

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not a valid email")
    private String email;
    @NotBlank(message = "The password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$", message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;
    @NotBlank(message = "The firstName is required")
    @Pattern(regexp = "^[a-zA-Z\\p{L}\\p{M}\\s.-]+$",
            message = "firstName must not contain special characters")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z\\p{L}\\p{M}\\s.-]+$",
            message = "lastName must not contain special characters")
    @NotBlank(message = "The lastName is required")
    private String lastName;
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    @NotBlank(message = "The phone is required")
    private String phone;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
