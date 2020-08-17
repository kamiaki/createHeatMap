package com.aki.createheatmap;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AppCreateHeatMapTest {
    public static void main(String[] args) {
        //最终获取的点
        List<Point> heatMapDataList = new ArrayList<>();

        //图片相关
        int imageWidth = 100;
        int imageHeight = 100;
        Point imgPoint1 = new Point();
        imgPoint1.setX(0.0D);
        imgPoint1.setY(0.0D);
        Point imgPoint2 = new Point();
        imgPoint2.setX(imageWidth);
        imgPoint2.setY(imageHeight);

        //地图坐标
        Point mapPoint1 = new Point();
        mapPoint1.setX(0.0D);
        mapPoint1.setY(0.0D);
        Point mapPoint2 = new Point();
        mapPoint2.setX(imageWidth);
        mapPoint2.setY(imageHeight);

        //随机测试用地图上的点
        List<Point> mapPointList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Point point = new Point();
            point.setX(Math.random() * (90 - 20 + 1) + 20);
            point.setY(Math.random() * (90 - 20 + 1) + 20);
            point.setZ((int)(Math.random() * (5 - 1 + 1)) + 1);
            mapPointList.add(point);
        }

        //坐标转换
        mapPointList.forEach(o -> {
            double rotation = Math.toRadians(Math.abs(TransUtil.getAngle(imgPoint1, imgPoint2) - TransUtil.getAngle(mapPoint1, mapPoint2)));
            double scale = TransUtil.getScale(mapPoint1, imgPoint1, mapPoint2, imgPoint2);
            double tx = TransUtil.getXTranslation(mapPoint1, imgPoint1, rotation, scale);
            double ty = TransUtil.getYTranslation(mapPoint1, imgPoint1, rotation, scale);
            Point boePoint = TransUtil.transformBoePoint(new Point(o.getX(), o.getY(), 0.0), rotation, scale, tx, ty);
            //去吹掉超出图片范围的点
            //若转换的坐标超出了图片范围舍弃
            Double x = boePoint.getX();
            Double y = boePoint.getY();
            if (!(x < 0 || y < 0 || x > imageWidth || y > imageHeight)) {
                Point heatMapData = new Point();
                heatMapData.setX(x).setY(y).setZ(o.getZ());
                heatMapDataList.add(heatMapData);
            }
        });

        System.out.println(mapPointList);
        System.out.println(heatMapDataList);

        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        heatMapDataList.forEach(x -> HeatMapUtil.renderPictures(bufferedImage, x.getX(), x.getY(), Double.valueOf(x.getZ()).intValue()));

        HeatMapUtil.gaussFuzzy(bufferedImage, "aaa.png");
    }
}
