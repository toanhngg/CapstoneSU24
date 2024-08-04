package fpt.CapstoneSU24.service;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class CustomGasProvider implements ContractGasProvider {
    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return BigInteger.valueOf(20000000000L); // 20 Gwei
    }

    @Override
    public BigInteger getGasPrice() {
        return BigInteger.valueOf(20000000000L); // 20 Gwei
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return BigInteger.valueOf(3000000); // 3,000,000 gas
    }

    @Override
    public BigInteger getGasLimit() {
        return BigInteger.valueOf(3000000); // 3,000,000 gas
    }
}