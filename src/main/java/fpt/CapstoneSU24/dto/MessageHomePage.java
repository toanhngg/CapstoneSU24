package fpt.CapstoneSU24.dto;

public class MessageHomePage {
    private int numberClient;
    private int numberTrace;
    private int numberRegisterProduct;

    public MessageHomePage() {

    }

    public MessageHomePage(int numberClient, int numberTrace, int numberRegisterProduct) {
        this.numberClient = numberClient;
        this.numberTrace = numberTrace;
        this.numberRegisterProduct = numberRegisterProduct;
    }

    public int getNumberClient() {
        return numberClient;
    }

    public void setNumberClient(int numberClient) {
        this.numberClient = numberClient;
    }

    public int getNumberTrace() {
        return numberTrace;
    }

    public void setNumberTrace(int numberTrace) {
        this.numberTrace = numberTrace;
    }

    public int getNumberRegisterProduct() {
        return numberRegisterProduct;
    }

    public void setNumberRegisterProduct(int numberRegisterProduct) {
        this.numberRegisterProduct = numberRegisterProduct;
    }
}
