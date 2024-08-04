package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.config.SimpleStorage;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ContractDeployer {

    private static final long CHAIN_ID = 137; // Chain ID cho Ganache

    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/1d5f0de705a947e499290fa5b7d8fe5c")); // Hoặc URL RPC khác
        Credentials credentials = Credentials.create("0x382391BFB7961bA8c735c4B5290A4D8964a83be6");
        // Kiểm tra số dư tài khoản
        EthGetBalance balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger wei = balance.getBalance();
        BigDecimal matic = new BigDecimal(wei).divide(BigDecimal.TEN.pow(18), BigDecimal.ROUND_HALF_UP); // Chia cho 10^18 để chuyển đổi từ wei sang MATIC
        System.out.println("Balance: " + matic + " MATIC");

        // Kiểm tra giá gas hiện tại
        EthGasPrice gasPriceResponse = web3j.ethGasPrice().send();
        BigInteger gasPrice = gasPriceResponse.getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(3000000); // Giới hạn gas cho hợp đồng

        System.out.println("Current gas price: " + gasPrice);

        // Tính toán chi phí giao dịch
        BigDecimal gasPriceMatic = new BigDecimal(gasPrice).divide(BigDecimal.TEN.pow(18), BigDecimal.ROUND_HALF_UP);
        BigDecimal txCost = gasPriceMatic.multiply(new BigDecimal(gasLimit));
        System.out.println("Estimated transaction cost: " + txCost + " MATIC");

        System.out.println("Current gas price: " + gasPrice);

        // Sử dụng CustomTransactionManager
        CustomTransactionManager transactionManager = new CustomTransactionManager(web3j, credentials, CHAIN_ID);

        // Triển khai hợp đồng
        SimpleStorage contract = SimpleStorage.deploy(
                web3j,
                transactionManager,
                new StaticGasProvider(gasPrice, gasLimit)
        ).send();

        System.out.println("Contract deployed at address: " + contract.getContractAddress());
    }
}