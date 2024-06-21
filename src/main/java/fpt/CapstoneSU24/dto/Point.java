package fpt.CapstoneSU24.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Lớp biểu diễn một điểm với tọa độ x và y
public class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
