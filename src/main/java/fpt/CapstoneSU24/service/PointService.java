package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointService {
    // Ham tinh ra diem tiep theo
    public static double interpolate(List<Point> points, double x) {
        int n = points.size();
        double result = 0;

        for (int i = 0; i < n; i++) {
            double term = points.get(i).getY();
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (x - points.get(j).getX()) / (points.get(i).getX() - points.get(j).getX());
                }
            }
            result += term;
        }

        return result;
    }
    // Ham kiem tra co nam tren 1 do thi khong
    public static boolean isPointOnCurve(List<Point> points, Point p) {
        double interpolatedY = interpolate(points, p.getX());
        return Math.abs(interpolatedY - p.getY()) < 1e-6; // Kiểm tra với sai số nhỏ
    }
    // Ham ramdom ra 1 diem
    public List<Point>  initializeData() {
        Random random = new Random();
        List<Point> points = new ArrayList<>();

        // Tạo hai điểm ngẫu nhiên
        points.add(new Point(random.nextInt(), random.nextInt()));
        points.add(new Point(random.nextInt(), random.nextInt()));

        return points;
    }
    public Point  randomPoint() {
        Random random = new Random();
        return new Point(random.nextInt(), random.nextInt());
    }
    //ham random ra 1 diem
    public double  generatedoubleX() {
        Random random = new Random();
        return random.nextInt();
    }
    public static Point getPoint(String point) {
        point = point.substring(1, point.length() - 1);
        String[] po = point.split(";");

        double x = Double.parseDouble(po[0]);
        double y = Double.parseDouble(po[1]);
        return new Point(x, y);
    }

}
