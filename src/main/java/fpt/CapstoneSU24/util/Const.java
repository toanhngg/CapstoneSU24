package fpt.CapstoneSU24.util;

public class Const {
    public final static class SEND_MAIL_SUBJECT {
        public final static String CLIENT_REGISTER = "XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG";
        public final static String CLIENT_NOTIFICATION = "THÔNG BÁO ỦY QUYỀN NGƯỜI DÙNG";
        public final static String CLIENT_SENDOTP = "THÔNG BÁO OTP XÁC THỰC NGƯỜI DÙNG";
        public final static String VERIFY_EMAIL = "THÔNG BÁO XÁC THỰC TÀI KHOẢN";
        public final static String SUBJECT_LOCKUSER = "THÔNG BÁO KHÓA TÀI KHOẢN NGƯỜI DÙNG";
        public final static String SUBJECT_UNLOCKUSER = "THÔNG BÁO MỞ KHÓA TÀI KHOẢN NGƯỜI DÙNG";
        public final static String SUBJECT_CHANGEPASS = "THÔNG BÁO THAY ĐỔI MẬT KHẨU";
        public final static String SUBJECT_REPLY_USER = "THÔNG BÁO VỀ PHÊ DUYỆT CHỨNG CHỈ DOANH NGHIỆP";
        public final static String SUBJECT_REPLY_SUPPORT_USER = "GIẢI ĐÁP VỀ VẤN ĐỀ HỆ THỐNG";

    }

    public final static class TEMPLATE_FILE_NAME {
        public final static String CLIENT_REGISTER = "client";
        public final static String CLIENT_NOTIFICATION = "clientNoti";
        public final  static  String CLIENT_SENDOTP = "sendOTP";
        public final  static  String CERTIFICATE = "certificate";
        public final static String LOCKUSER_DETAIL = "lockUserMail";
        public final static String UNLOCKUSER_DETAIL = "UnLockUserMail";
        public final static String CHANGEPASSWORD = "forgotPassword";
        public final static String VERIFY_EMAIL = "verifyEmail";
        public final static String ANNOUCE_NEW_ISSUE = "announceNewIssue";
        public final static String REPLY_ISSUE = "reply-issue-notification";
        public final static String REPLY_USER = "notiAccept";
        public final static String REPLY_REPLY_SUPPORT_USER = "notiSupport";


    }

    public final static class ClientServer{
        public final static String DeployServer = "https://trace-origin.netlify.app/";
        public final static String LocalServer = "http://localhost:3000/";
    }


    public final static class TEMPLATE_FILE_NAME_eSgin{
        public final static String ESGIN = "eSgin";
    }


}
