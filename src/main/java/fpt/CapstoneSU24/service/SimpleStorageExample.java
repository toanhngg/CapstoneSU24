package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.config.SimpleStorage;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

public class SimpleStorageExample {
    public static void main(String[] args) throws Exception {
        // Khởi tạo Web3j, Credentials và CustomTransactionManager
        Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/1d5f0de705a947e499290fa5b7d8fe5c"));
        Credentials credentials = Credentials.create("0x382391BFB7961bA8c735c4B5290A4D8964a83be6");

        long chainId = 137; // Chain ID của Polygon Mainnet

        CustomTransactionManager transactionManager = new CustomTransactionManager(web3j, credentials, chainId);

        ContractGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(20000000000L), BigInteger.valueOf(4700000));

       // RemoteCall<SimpleStorage> deployRemoteCall = SimpleStorage.deploy(web3j, transactionManager, gasProvider);

        // Triển khai hợp đồng
      //  SimpleStorage simpleStorage = deployRemoteCall.send();
        //System.out.println("Contract deployed at address: " + simpleStorage.getContractAddress());
    }

}
