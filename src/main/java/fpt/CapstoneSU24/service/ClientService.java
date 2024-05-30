package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.repository.ClientResponsitory;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClientService implements ClientResponsitory {
    @Autowired
    private EmailService mailService;
    @Autowired
    private RedisSortedSetService redisSortedSetService;

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
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean checkOTP(String email, String otp) {
        try {
           String otpRar = (redisSortedSetService.getHash("OTPCODE",email)).toString();
           if(otpRar.equals(otp)) {
               System.out.println("yuuuus");
               return true;
           }
           else{
               return false;
           }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
    }
}
