package fpt.CapstoneSU24.dto.B02;

import fpt.CapstoneSU24.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class B02_GetDetailReport {
    private Date createOn;
    private Date updateOn;
    private String code;
    private String title;
    private int type;
    private int status;
    private int priority;
    private String createBy;
    private HashMap<Integer, String> reportTo = new HashMap<>();
    private int component;
}
