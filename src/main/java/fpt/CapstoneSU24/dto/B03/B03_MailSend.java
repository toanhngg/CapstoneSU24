package fpt.CapstoneSU24.dto.B03;

public class B03_MailSend {
    private String to;
    private String Subject;
    private String Text;

    public B03_MailSend() {
    }

    public B03_MailSend(String to, String subject, String text) {
        this.to = to;
        Subject = subject;
        Text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
