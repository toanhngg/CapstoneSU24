package fpt.CapstoneSU24.dto.B02;

public class ReportComponent {
    private int code;
    private String name;

    public ReportComponent() {
    }

    public ReportComponent(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
