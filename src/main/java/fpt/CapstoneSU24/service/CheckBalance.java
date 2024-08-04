package fpt.CapstoneSU24.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CheckBalance {
//    public static void main(String[] args) throws Exception {
//        // Khởi tạo kết nối với Ethereum
//        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/d003be27f93f4bd58cb6c69dc640acf3"));
//        String contractAddress = "0xBa430b53B6b9736bC9Bc91676025655DdEfd9Fe3"; // Địa chỉ hợp đồng của bạn
//
//        // Kiểm tra định dạng địa chỉ hợp đồng
//        if (!contractAddress.startsWith("0x") || contractAddress.length() != 42) {
//            System.out.println("Invalid address format");
//            return;
//        }
//
////        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID"));
////        String walletAddress = "YOUR_WALLET_ADDRESS";
//
//        EthGetBalance balance = web3j.ethGetBalance(contractAddress, DefaultBlockParameterName.LATEST).send();
//        System.out.println("Wallet Balance: " + balance.getBalance());
//
//    }

        public static void main(String[] args) throws Exception {
            Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/1d5f0de705a947e499290fa5b7d8fe5c")); // Hoặc URL RPC khác
            String address = "0x382391BFB7961bA8c735c4B5290A4D8964a83be6"; // Thay thế bằng địa chỉ ví của bạn

            EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger wei = balance.getBalance();
            System.out.println("Balance: " + wei.toString() + " wei");
        }
//public static void main(String[] args) {
//    try {
//        Web3j web3j = Web3j.build(new HttpService("https://polygon-rpc.com/"));
//        String address = "0xBa430b53B6b9736bC9Bc91676025655DdEfd9Fe3"; // Thay thế bằng địa chỉ ví của bạn
//
//        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
//
//        // In toàn bộ phản hồi để kiểm tra
//        System.out.println("Response: " + balance.getRawResponse());
//        System.out.println("Balance (hex format): " + balance.getBalance().toString());
//
//        // Chuyển đổi số dư từ hex sang decimal
//        java.math.BigInteger balanceWei = balance.getBalance();
//        System.out.println("Balance (in wei): " + balanceWei.toString());
//
//        // Chuyển đổi từ wei sang ether (ETH)
//        java.math.BigDecimal balanceEther = new java.math.BigDecimal(balanceWei).divide(java.math.BigDecimal.valueOf(1_000_000_000_000_000_000L));
//        System.out.println("Balance (in ETH): " + balanceEther.toPlainString());
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
}
