package fpt.CapstoneSU24.service;

import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class CustomTransactionManager extends RawTransactionManager {
        private final Web3j web3j;
        private final Credentials credentials;
        private final long chainId;

        public CustomTransactionManager(Web3j web3j, Credentials credentials, long chainId) {
            super(web3j, credentials, chainId);
            this.web3j = web3j;
            this.credentials = credentials;
            this.chainId = chainId;
        }

    @Override
    public EthSendTransaction signAndSend(RawTransaction rawTransaction) throws IOException {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return web3j.ethSendRawTransaction(hexValue).send();
    }
}