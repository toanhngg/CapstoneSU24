package fpt.CapstoneSU24.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;

public class NetworkCheck {
    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/1d5f0de705a947e499290fa5b7d8fe5c"));

        // Kiểm tra phiên bản của client
        System.out.println("Client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // Lấy số khối hiện tại
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        System.out.println("Current block number: " + blockNumber.getBlockNumber());
    }
}
