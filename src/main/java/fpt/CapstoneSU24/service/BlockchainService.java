package fpt.CapstoneSU24.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
@Service
public class BlockchainService {

//    private static final String CONTRACT_ADDRESS = "0xDA0bab807633f07f013f94DD0E6A4F96F8742B53";
//    private static final String PRIVATE_KEY = "0x382391BFB7961bA8c735c4B5290A4D8964a83be6";
//
//    private final Web3j web3j;
//    private final Credentials credentials;
//    private final SimpleStorage contract;
//
//    public BlockchainService() {
//        this.web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/d003be27f93f4bd58cb6c69dc640acf3"));
//        this.credentials = Credentials.create(PRIVATE_KEY);
//        this.contract = SimpleStorage.load(
//                CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
//    }
//
//    public String get() throws Exception {
//        return contract.executeRemoteCallSingleValueReturn("get", String.class).send();
//    }
//
//    public void set(String value) throws Exception {
//        contract.executeTransaction("set", value).send();
   // }
}