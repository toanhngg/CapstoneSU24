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

        // Phương pháp Newton-Raphson
      /*  public static double newtonRaphson(List<Point> points, double yTarget, double guess, double tolerance, int maxIterations) {
            double x = guess; // Bắt đầu với giá trị ước lượng ban đầu
            for (int i = 0; i < maxIterations; i++) {
                double y = lagrangeInterpolate(points, x); // Tính giá trị y tại x hiện tại
                double dy = lagrangeInterpolateDerivative(points, x); // Tính đạo hàm tại x hiện tại
                double xNext = x - (y - yTarget) / dy; // Cập nhật x theo công thức Newton-Raphson

                if (Math.abs(xNext - x) < tolerance) { // Kiểm tra xem thay đổi có nhỏ hơn độ chính xác mong muốn không
                    return xNext; // Nếu đúng, trả về giá trị x hiện tại
                }
                x = xNext; // Cập nhật x để tiếp tục lặp
            }
            throw new RuntimeException("Failed to converge"); // Ném ngoại lệ nếu không hội tụ trong số lần lặp tối đa
        } */

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

//        public static void main(String[] args) {
//            List<Point> points = List.of(
//                    new Point(1, 2),
//                    new Point(3, 6),
//                    new Point(5, 10)
//            );
//
//            // Tính giá trị y tại một giá trị x mới
//            double newX = 4;
//            double interpolatedY = lagrangeInterpolate(points, newX);
//            System.out.println("Interpolated Y at x = " + newX + " is " + interpolatedY);
//
//            // Kiểm tra xem một điểm có nằm trên đường cong nội suy không
//            Point p = new Point(4, 8);
//            boolean isOnCurve = isPointOnCurve(points, p);
//            System.out.println("Point (4, 8) is on curve: " + isOnCurve);
//        }
//    }


        // Ham ramdom ra 1 diem
//    public List<Point>  initializeData() {
//        Random random = new Random();
//        List<Point> points = new ArrayList<>();
//        // Tạo hai điểm ngẫu nhiên
//        points.add(new Point(random.nextInt(), random.nextInt()));
//        points.add(new Point(random.nextInt(), random.nextInt()));
//
//        return points;
//    }

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
        public double generateX() {
            Random random = new Random();
            double x = random.nextInt();
            return x;
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
