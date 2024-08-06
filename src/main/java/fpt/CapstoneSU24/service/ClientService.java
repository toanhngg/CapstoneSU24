package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.OTP;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService implements ClientRepository {
    private final EmailService mailService;
    private final RedisSortedSetService redisSortedSetService;
    private final OTPRepository otpResponsitory;
    private final ItemRepository itemRepository;
    private final OTPService otpService;
    private final LogService logService;
    private final ItemLogRepository itemLogRepository;


    @Autowired
    public ClientService(EmailService mailService, RedisSortedSetService redisSortedSetService,
                         OTPRepository otpResponsitory, ItemRepository itemRepository, OTPService otpService,
                         LogService logService,ItemLogRepository itemLogRepository) {
        this.mailService = mailService;
        this.redisSortedSetService = redisSortedSetService;
        this.otpResponsitory = otpResponsitory;
        this.itemRepository = itemRepository;
        this.otpService = otpService;
        this.logService =logService;
        this.itemLogRepository = itemLogRepository;

    }

    @Override
    public Boolean create(ClientSdi sdi) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            //đoc ở file const
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_REGISTER);

            Map<String, Object> props = new HashMap<>();
            props.put("name", sdi.getName());
            props.put("username", sdi.getUsername());
            String codeOTP = DataUtils.generateTempPwd(6);
            props.put("codeOTP", codeOTP);

            dataMail.setProps(props);
            Date dateOTP = new Date();
            // Lưu redis
            redisSortedSetService.saveHash("OTPCODE",sdi.getEmail(), codeOTP);
        // tam cmt de test cho do bi spam
            //mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_REGISTER);
            return true;
        } catch (Exception ex) {
            logService.logError(ex);
           // exp.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean createMailAndSaveSQL(ClientSdi sdi) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            //đoc ở file const
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_SENDOTP);

            Map<String, Object> props = new HashMap<>();
            props.put("name", sdi.getName());
            props.put("username", sdi.getUsername());
            String codeOTP = DataUtils.generateTempPwd(6);
            props.put("codeOTP", codeOTP);

            dataMail.setProps(props);
            Date expiryTime = otpService.calculateExpiryTime(2); // OTP sẽ hết hạn sau 2 phút

            OTP otpCheck = otpResponsitory.findOTPByEmail(sdi.getEmail());
           if(otpCheck == null) { // neu chua tung xac thuc bao h thi tao moi khong thi update
               otpCheck = new OTP(sdi.getEmail(), codeOTP, expiryTime);
               otpResponsitory.save(otpCheck);
           }else{
               otpService.updateOTPCode(sdi.getEmail(), codeOTP,expiryTime);
           }
            // tam cmt de test cho do bi spam
          mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_SENDOTP);
            return true;
        } catch (Exception ex) {
            logService.logError(ex);
           // exp.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean notification(ClientSdi sdi) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            //đoc ở file const
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_NOTIFICATION);

            Map<String, Object> props = new HashMap<>();
            props.put("name", sdi.getName());
            props.put("username", sdi.getUsername());
            dataMail.setProps(props);
            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_NOTIFICATION);
            return true;
        } catch (Exception ex) {
            logService.logError(ex);

            //exp.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean notificationEdit(ClientSdi sdi) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            //đoc ở file const
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_NOTIFICATION);

            Map<String, Object> props = new HashMap<>();
            props.put("name", sdi.getName());
            props.put("username", sdi.getUsername());
            dataMail.setProps(props);
            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_NOTIFICATION);
            return true;
        } catch (Exception ex) {
            logService.logError(ex);

            //exp.printStackTrace();
        }
        return false;
    }

//    @Override
//    public Boolean checkOTP(String email, String otp) {
//        try {
//           String otpRar = (redisSortedSetService.getHash("OTPCODE",email)).toString();
//           if(otpRar.equals(otp)) {
//               System.out.println("yuuuus");
//               return true;
//           }
//           else{
//               return false;
//           }
//        } catch (Exception ex) {
//            logService.logError(ex);
//            //exp.printStackTrace();
//        }
//        return false;
//    }
//    @Override
//    public Boolean checkOTPinSQL(String email, String otp) {
//        try {
//            return otpService.verifyOTP(email, otp);
//        } catch (Exception ex) {
//            logService.logError(ex);
//            //exp.printStackTrace();
//        }
//        return false;
//    }

    @Override
    public int checkOTP(String email, String otp, String productRecognition) {
        try {
            Item item = findByProductRecognition(productRecognition);
            List<ItemLog> list = itemLogRepository.getItemLogsByItemIdDescNotEdit(item.getItemId()); // tìm cái đầu tiên
            if (item.getStatus() == 0) return 0; // Sản phẩm đã bị cấm

            try {
                boolean check = otpService.verifyOTP(email, otp);
                if (!check) return 6; // OTP sai

                if (checkOwner(email, item.getCurrentOwner())) {
                    return 3;
                } else if (list != null && !list.isEmpty()) {
                    if (list.get(0).getAuthorized() != null &&
                            email.equals(list.get(0).getAuthorized().getAuthorizedEmail())) {
                        return 2;
                    } else if (list.size() > 1 && list.get(1).getAuthorized() != null &&
                            email.equals(list.get(1).getAuthorized().getAuthorizedEmail())) {
                        return 7;
                    }
                }

                if (checkParty(email, item.getItemId())) {
                    return 4;
                } else {
                    return 1;  // Không phải gì cả
                }
            } catch (Exception e) {
                logService.logError(e);
                return 5; // Exception
            }
        } catch (Exception ex) {
            logService.logError(ex);
            return 5; // Exception
        }
}

    public boolean checkParty(String email, int itemId) {
        List<ItemLog> itemLogs = itemLogRepository.checkParty(itemId, email);
        return itemLogs != null;
    }

    public boolean checkOwner(String email, String emailCurrentOwner) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (emailCurrentOwner == null || emailCurrentOwner.isEmpty()) {
            return false;
        }
        return email.equals(emailCurrentOwner);
    }

    public Item findByProductRecognition(String productRecognition) {
        if (productRecognition == null || productRecognition.isEmpty()) {
            throw new IllegalArgumentException("Product recognition cannot be null or empty");
        }
        return itemRepository.findByProductRecognition(productRecognition);
    }

}
