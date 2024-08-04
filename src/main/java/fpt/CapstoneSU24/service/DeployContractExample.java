package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.config.SimpleStorage;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class DeployContractExample {
//    public static void main(String[] args) {
//        try {
//            // Khóa riêng hợp lệ (không bao gồm 0x)
//            String privateKey = "Ba430b53B6b9736bC9Bc91676025655DdEfd9Fe3";
//            Credentials credentials = Credentials.create(privateKey);
//
//            // URL RPC của Polygon Mainnet từ Infura
//            Web3j web3j = Web3j.build(new HttpService("https://polygon-amoy.infura.io/v3/1d5f0de705a947e499290fa5b7d8fe5c"));
//
//            // Triển khai hợp đồng
//            SimpleStorage contract = SimpleStorage.deploy(
//                    web3j,
//                    credentials,
//                    new DefaultGasProvider()
//            ).send();
//
//            System.out.println("Contract deployed to address: " + contract.getContractAddress());
//
//            // Tải hợp đồng đã triển khai nếu bạn có địa chỉ hợp đồng
//            String contractAddress = "0x..."; // Thay thế bằng địa chỉ hợp đồng của bạn
//            SimpleStorage loadedContract = SimpleStorage.load(
//                    contractAddress,
//                    web3j,
//                    credentials,
//                    new DefaultGasProvider()
//            );
//
//            // Gọi các phương thức hợp đồng ở đây
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//public static void main(String[] args) {
//    // Kết nối với Polygon Mainnet
//    Web3j web3j = Web3j.build(new HttpService("https://polygon-rpc.com/"));
//
//    // Hoặc nếu sử dụng Polygon Mumbai Testnet
//    // Web3j web3j = Web3j.build(new HttpService("https://rpc-mumbai.maticvigil.com/"));
//
//    try {
//        // Khóa riêng hợp lệ (không bao gồm 0x)
//        String privateKey = "DA0bab807633f07f013f94DD0E6A4F96F8742B53";
//        Credentials credentials = Credentials.create(privateKey);
//
//        // Triển khai hợp đồng
//        SimpleStorage contract = SimpleStorage.deploy(
//                web3j,
//                credentials,
//                new DefaultGasProvider()
//        ).send();
//
//        System.out.println("Contract deployed to address: " + contract.getContractAddress());
//
//        // Tải hợp đồng đã triển khai nếu bạn có địa chỉ hợp đồng
//        String contractAddress = "0x..."; // Thay thế bằng địa chỉ hợp đồng của bạn
//        SimpleStorage loadedContract = SimpleStorage.load(
//                contractAddress,
//                web3j,
//                credentials,
//                new DefaultGasProvider()
//        );
//
//        // Gọi các phương thức hợp đồng ở đây
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
}
