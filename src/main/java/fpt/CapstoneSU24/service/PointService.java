package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.Point;
import fpt.CapstoneSU24.model.ItemLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PointService {
        // Hàm nội suy Lagrange
        public  double lagrangeInterpolate(List<Point> points, double x) {
            int n = points.size();
            double y = 0;
            for (int i = 0; i < n; i++) {
                double term = points.get(i).getY();
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        term *= (x - points.get(j).getX()) / (points.get(i).getX() - points.get(j).getX());
                    }
                }
                y += term;
            }

            return y;
        }

        // Hàm tính đạo hàm của nội suy Lagrange
        public static double lagrangeInterpolateDerivative(List<Point> points, double x) {
            int n = points.size();
            double dy = 0;

            for (int i = 0; i < n; i++) {
                double term = points.get(i).getY();
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        double product = 1;
                        for (int k = 0; k < n; k++) {
                            if (k != i && k != j) {
                                product *= (x - points.get(k).getX()) / (points.get(i).getX() - points.get(k).getX());
                            }
                        }
                        sum += product / (points.get(i).getX() - points.get(j).getX());
                    }
                }
                dy += term * sum;
            }

            return dy;
        }

        // Hàm kiểm tra xem một điểm có nằm trên đường cong nội suy hay không
        public boolean isPointOnCurve(List<Point> points, Point p) {
            double x = p.getX();  // Giá trị x của điểm cần kiểm tra
            double yTarget = p.getY();  // Giá trị y của điểm cần kiểm tra
            double tolerance = 1e-6;  // Độ chính xác mong muốn

            double interpolatedY = lagrangeInterpolate(points, x);  // Nội suy giá trị y tại x

            // Kiểm tra xem giá trị nội suy có gần bằng giá trị y của điểm không
            if (Math.abs(interpolatedY - yTarget) < tolerance) {
                System.out.println("Interpolated Y: " + interpolatedY + ", Actual Y: " + yTarget); // Debug log
                return true;
            } else {
                System.out.println("Interpolated Y: " + interpolatedY + ", Actual Y: " + yTarget); // Debug log
                return false;
            }
        }

    public Point randomPoint(int maxX, int maxY) {
            Random random = new Random();
                int x = random.nextInt(maxX + 1); // Generates a random integer from 0 to maxX
                int y = random.nextInt(maxY + 1); // Generates a random integer from 0 to maxY
                return new Point(x, y);
        }

        private double roundToThreeDecimalPlaces(double value) {
            return Math.round(value * 1000.0) / 1000.0;
        }

        //ham random ra 1 toa do X
//        public double generateX() {
//            int maxX = 3000;
//            Random random = new Random();
//            double x = random.nextDouble() * maxX;
//            return x;
//        }
    public double generateX() {
        double minX = -1000;
        double maxX = 1000;
        Random random = new Random();
        double x = minX + (maxX - minX) * random.nextDouble();
        return roundToThreeDecimalPlaces(x);
    }




    public Point getPoint(String point) {
            point = point.substring(1, point.length() - 1);
            String[] po = point.split(";");

            double x = Double.parseDouble(po[0]);
            double y = Double.parseDouble(po[1]);
            return new Point(x, y);
        }

        public boolean arePointsOnCurve(List<ItemLog> pointLogs) {
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < pointLogs.size(); i++) {
                Point point = getPoint(pointLogs.get(i).getPoint());
                points.add(point);
            }

            Point startPoint = getPoint(pointLogs.get(0).getPoint());
            return isPointOnCurve(points, startPoint);
        }

        public List<Point> getPointList(List<ItemLog> pointLogs) {
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < pointLogs.size(); i++) {
                Point point = getPoint(pointLogs.get(i).getPoint());
                points.add(point);
            }
            return points;
        }
    }
