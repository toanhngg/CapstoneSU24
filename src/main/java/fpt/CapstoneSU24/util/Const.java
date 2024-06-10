package fpt.CapstoneSU24.util;

public class Const {
    public final static class SEND_MAIL_SUBJECT {
        public final static String CLIENT_REGISTER = "XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG";
        public final static String CLIENT_NOTIFICATION = "THÔNG BÁO TRÌNH ỦY QUYỀN NGUƯỜI DÙNG";
        public final static String CLIENT_SENDOTP = "XÁC NHẬN ỦY QUYỀN NGƯỜI DÙNG";


    }

    public final static class TEMPLATE_FILE_NAME {
        public final static String CLIENT_REGISTER = "client";
        public final static String CLIENT_NOTIFICATION = "clientNoti";

        public final  static  String CLIENT_SENDOTP = "sendOTP";

    }

    public final static class SEND_MAIL_SUBJECTLockUser {
        public final static String SUBJECT_LOCKUSER = "THÔNG BÁO KHÓA TÀI KHOẢN NGƯỜI DÙNG";
    }

    public final static class TEMPLATE_FILE_NAME_LOCKUSER {
        public final static String LOCKUSER_DETAIL = "lockUserMail";
    }

    public final static class SEND_MAIL_SUBJECTUnLockUser {
        public final static String SUBJECT_UNLOCKUSER = "THÔNG BÁO MỞ KHÓA TÀI KHOẢN NGƯỜI DÙNG";
    }

    public final static class TEMPLATE_FILE_NAME_UNLOCKUSER {
        public final static String UNLOCKUSER_DETAIL = "UnLockUserMail";
    }
}
