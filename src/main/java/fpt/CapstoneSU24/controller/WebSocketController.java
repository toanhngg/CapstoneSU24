//package fpt.CapstoneSU24.controller;
//
//import fpt.CapstoneSU24.dto.MessageHomePage;
//import fpt.CapstoneSU24.repository.ProductRepository;
//import fpt.CapstoneSU24.repository.UserRepository;
//import fpt.CapstoneSU24.service.ELKService;
//import fpt.CapstoneSU24.service.ProductService;
//import fpt.CapstoneSU24.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//import java.io.IOException;
//
//@Controller
//public class WebSocketController {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    ProductRepository productRepository;
//    @Autowired
//    ELKService elkService;
//
//    @MessageMapping("/send")
//    @SendTo("/topic/messages")
//    public String sendMessage() throws IOException {
//        MessageHomePage messageHomePage = new MessageHomePage();
//        messageHomePage.setNumberClient(userRepository.findAll().size());
//        messageHomePage.setNumberRegisterProduct(productRepository.findAll().size());
//        messageHomePage.setNumberTrace(elkService.getNumberTraceAllTime());
//        return messageHomePage.toString();
//    }
//}