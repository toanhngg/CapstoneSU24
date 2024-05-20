//package fpt.CapstoneSU24;
//
//import fpt.CapstoneSU24.model.*;
//import fpt.CapstoneSU24.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@SpringBootApplication
//public class DummyData implements CommandLineRunner {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private OriginRepository originRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ActorRepository actorRepository;
//    @Autowired
//    private ItemLogRepository productLogRepository;
//
//    public static void main(String[] args) {
//        SpringApplication.run(DummyData.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        /*dummy data cho Role*/
//        List<Role> roleList = Stream.of(
//                new Role(0, "Admin"),
//                new Role(0, "Manufacturer"),
//                new Role(0, "Customer")
//
//        ).collect(Collectors.toList());
//        roleRepository.saveAll(roleList);
//
//        /*dummy data cho Actor*/
//        List<Actor> actorList = Stream.of(
//                new Actor(0, "Nguyen Chi Thanh", "", "09619000", "","")
//        ).collect(Collectors.toList());
//        actorRepository.saveAll(actorList);
//
//        /*dummy data cho Category*/
//        List<Category> categoryList = Stream.of(
//                new Category(0, "lọ hoa", "phần mô tả"),
//                new Category(0, "bát", "phần mô tả"),
//                new Category(0, "bình trà", "phần mô tả")
//        ).collect(Collectors.toList());
//        categoryRepository.saveAll(categoryList);
//
//        /*dummy data cho User*/
//        List<User> userList = Stream.of(
//                new User(0, "thanhnc@gmail.com", "o/NhoNP+F6OwaF3dn9i5hw==", roleRepository.findOneByRoleId(1), "thanh", "nguyen", "", "Tay Ho", "HN", "0987654321", 21, ""),
//                new User(0, "haidp@gmail.com", "o/NhoNP+F6OwaF3dn9i5hw==", roleRepository.findOneByRoleId(2), "hai", "dang", "", "Tay Ho", "HN", "0987654321", 21, ""),
//                new User(0, "manhdt@gmail.com", "o/NhoNP+F6OwaF3dn9i5hw==", roleRepository.findOneByRoleId(3), "manh", "duong", "", "Tay Ho", "HN", "0987654321", 21, "")
//
//        ).collect(Collectors.toList());
//        userRepository.saveAll(userList);
//
//        /*dummy data cho Origin*/
//        List<Origin> originList = Stream.of(
//                new Origin(0, userRepository.findOneByUserId(2), 0, "", "")
//        ).collect(Collectors.toList());
//        originRepository.saveAll(originList);
//
//        /*dummy data cho Product*/
//        List<Product> productList = Stream.of(
//                new Product(0, "bát gốm", categoryRepository.findOneByCategoryId(2), originRepository.findOneByOriginId(1), userRepository.findOneByUserId(3), "1000$", 0, "100x50x12", "đồ gốm", "", "")
//        ).collect(Collectors.toList());
//        productRepository.saveAll(productList);
//
//        /*dummy data cho ProductLog*/
//        List<ProductLog> productLogList = Stream.of(
//                new ProductLog(0, productRepository.findOneByProductId(1), "FPTU", actorRepository.findOneByActorId(1), 0, "", "delivery", 0)
//        ).collect(Collectors.toList());
//        productLogRepository.saveAll(productLogList);
//
//        /*stop running*/
//        System.exit(0);
//    }
//}
