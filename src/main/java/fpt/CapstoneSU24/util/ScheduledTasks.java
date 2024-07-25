//package fpt.CapstoneSU24.util;
//
//import fpt.CapstoneSU24.dto.MessageHomePage;
//import fpt.CapstoneSU24.repository.ProductRepository;
//import fpt.CapstoneSU24.repository.UserRepository;
//import fpt.CapstoneSU24.service.ELKService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class ScheduledTasks {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    ProductRepository productRepository;
//    @Autowired
//    ELKService elkService;
//    @Autowired
//    private SimpMessagingTemplate template;
//
//    @Scheduled(fixedRate = 10000)
//    public void sendPeriodicMessages() throws IOException {
//        MessageHomePage messageHomePage = new MessageHomePage();
//        messageHomePage.setNumberClient(userRepository.findAll().size());
//        messageHomePage.setNumberRegisterProduct(productRepository.findAll().size());
//        messageHomePage.setNumberTrace(elkService.getNumberTraceAllTime());
//        template.convertAndSend("/topic/messages",messageHomePage);
//    }
//}