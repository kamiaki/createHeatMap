package com.aki.createheatmap;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransUtil {

    /**
     * 获取两点连线与y轴夹角
     *
     * @param p1 点1
     * @param p2 点2
     * @return 与y轴夹角(角度)
     */
    public static double getAngle(Point p1, Point p2) {
        double angle = Math.atan2(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        return angle * (180 / Math.PI);
    }

    /**
     * 获取缩放比例
     *
     * @param p1 源点1
     * @param b1 目标点1
     * @param p2 源点2
     * @param b2 目标点2
     * @return 缩放比例
     */
    public static double getScale(Point p1, Point b1, Point p2, Point b2) {
        return getLength(b1, b2) / getLength(p1, p2);
    }

    /**
     * 获取两点之间连线的长度
     *
     * @param p1 点1
     * @param p2 点2
     * @return 长度
     */
    public static double getLength(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    /**
     * X方向偏移距离参数
     *
     * @param p1       源点1
     * @param b1       目标点1
     * @param rotation 旋转角度
     * @param scale    缩放比例
     * @return X方向偏移
     */
    public static double getXTranslation(Point p1, Point b1, double rotation, double scale) {
        return (b1.getX() - scale * (p1.getX() * Math.cos(rotation) - p1.getY() * Math.sin(rotation)));
    }

    /**
     * Y方向偏移距离参数
     *
     * @param p1       源点1
     * @param b1       目标点1
     * @param rotation 旋转角度
     * @param scale    缩放比例
     * @return Y方向偏移
     */
    public static double getYTranslation(Point p1, Point b1, double rotation, double scale) {
        return (b1.getY() - scale * (p1.getX() * Math.sin(rotation) + p1.getY() * Math.cos(rotation)));
    }

    /**
     * 转换操作
     *
     * @param gp       源点
     * @param rotation 旋转角度
     * @param scale    缩放比例
     * @param dx       X方向偏移
     * @param dy       Y方向偏移
     * @return 目标点
     */
    public static Point transformBoePoint(Point gp, double rotation, double scale, double dx, double dy) {
        double A = scale * Math.cos(rotation);
        double B = scale * Math.sin(rotation);
        return new Point(retain6(A * gp.getX() - B * gp.getY() + dx), retain6(B * gp.getX() + A * gp.getY() + dy), 0.0);
    }


    /**
     * 保留小数点后六位
     *
     * @param num
     * @return
     */
    public static double retain6(double num) {
        String result = String.format("%.6f", num);
        return Double.valueOf(result);
    }

}
