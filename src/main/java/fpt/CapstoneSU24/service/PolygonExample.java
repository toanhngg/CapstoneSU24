package fpt.CapstoneSU24.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class PolygonExample {
    public static void main(String[] args) {
        // Kết nối với Polygon Mainnet
        Web3j web3j = Web3j.build(new HttpService("https://polygon-rpc.com/"));

        // Hoặc nếu sử dụng Polygon Mumbai Testnet
        // Web3j web3j = Web3j.build(new HttpService("https://rpc-mumbai.maticvigil.com/"));

        try {
            // Lấy thông tin block hiện tại
            String latestBlockNumber = web3j.ethBlockNumber().send().getBlockNumber().toString();
            System.out.println("Latest Block Number: " + latestBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}