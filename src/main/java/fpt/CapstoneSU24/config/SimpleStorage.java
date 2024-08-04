package fpt.CapstoneSU24.config;

import fpt.CapstoneSU24.service.CustomTransactionManager;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import org.web3j.crypto.Credentials;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;


public class SimpleStorage extends Contract {

    private static final String BINARY = "608060405234801561000f575f80fd5b506101718061001d5f395ff3fe608060405234801561000f575f80fd5b506004361061003f575f3560e01c806360fe47b1146100435780636d4ce63c1461005f57806373d4a13a1461007d575b5f80fd5b61005d600480360381019061005891906100e8565b61009b565b005b6100676100a4565b6040516100749190610122565b60405180910390f35b6100856100ac565b6040516100929190610122565b60405180910390f35b805f8190555050565b5f8054905090565b5f5481565b5f80fd5b5f819050919050565b6100c7816100b5565b81146100d1575f80fd5b50565b5f813590506100e2816100be565b92915050565b5f602082840312156100fd576100fc6100b1565b5b5f61010a848285016100d4565b91505092915050565b61011c816100b5565b82525050565b5f6020820190506101355f830184610113565b9291505056fea264697066735822122042b17bb863f4d5b68a1efa635997555a4adc4ff09af3cc9db932c270ee72d0f464736f6c63430008150033";
    private static final String ABI = "[{\"inputs\":[],\"name\":\"data\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_data\",\"type\":\"uint256\"}],\"name\":\"set\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";

    protected SimpleStorage(String contractAddress, Web3j web3j, TransactionManager credentials, ContractGasProvider gasProvider) {
        super(ABI, contractAddress, web3j, credentials, gasProvider);
    }

    public static SimpleStorage load(String contractAddress, Web3j web3j, TransactionManager credentials, ContractGasProvider gasProvider) {
        return new SimpleStorage(contractAddress, web3j, credentials, gasProvider);
    }


//    public static SimpleStorage deploy(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) throws Exception {
//        return deployRemoteCall(SimpleStorage.class, web3j, credentials, gasProvider, BINARY, "").send();
//    }
    public static RemoteCall<SimpleStorage> deploy(Web3j web3j, TransactionManager credentials, ContractGasProvider gasProvider) {
        return deployRemoteCall(SimpleStorage.class, web3j, credentials, gasProvider, BINARY, "");
    }



    public RemoteFunctionCall<TransactionReceipt> set(BigInteger x) {
        final Function function = new Function(
                "set",
                Arrays.asList(new Uint256(x)),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> get() {
        final Function function = new Function(
                "get",
                Collections.emptyList(),
                Arrays.asList(new TypeReference<Uint256>() {})
        );
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
}
