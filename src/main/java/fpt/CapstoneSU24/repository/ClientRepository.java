package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.sdi.ClientSdi;

public interface ClientRepository {
    Boolean create(ClientSdi sdi);

    Boolean notification(ClientSdi sdi);
    Boolean notificationEdit(ClientSdi sdi);

    Boolean checkOTP(String email, String otp);

    Boolean createMailAndSaveSQL(ClientSdi sdi);
    Boolean checkOTPinSQL(String email, String otp);
    int checkOTPinSQL2(String email, String productRecognition, String otp);
}
