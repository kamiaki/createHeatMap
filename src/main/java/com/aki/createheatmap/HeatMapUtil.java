package com.aki.createheatmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class HeatMapUtil {


    /**
     * 渲染图片
     * @param image 图片流
     * @param x 操作的像素点x
     * @param y 操作的像素点y
     * @param count 渲染的像素点
     */
    public static void renderPictures(BufferedImage image, Double x, Double y, int count){
        //获取画笔对象
        int xValue = x.intValue();
        int yValue = y.intValue();
        int pixel = image.getRGB(xValue, yValue);
        //从pixel中获取rgba的值
        int b = (0xff & pixel);
        int g = (0xff & (pixel >> 8));
        int r = (0xff & (pixel >> 16));
        //α通道值
        int alpha = (0xff & (pixel >> 24));
        //颜色解析
        if (r == 0){
            if (g < 255){
                g = 255;
            }
        }
        int rgb = b + (g << 8) + (r << 16) + (255 << 24);
        int vy = image.getHeight() - yValue;
//        image.setRGB(xValue, vy, rgb);
//        image.setRGB(xValue, vy, rgb);
//        image.setRGB(xValue, vy, rgb);
//        image.setRGB(xValue, vy, rgb);
        for (int i = xValue - count; i< xValue + count; i++) {
            for (int j = vy - count; j< vy + count; j++) {
                if (i >= 0 && i < image.getWidth()) {
                    if (j >=0 && j < image.getHeight() ) {
                        image.setRGB(i, j, rgb);
                    }
                }
            }
        }
    }


    /**
     * 高斯模糊 设置色阶
     * @param blur
     * @param outPat
     */
    public static void gaussFuzzy(BufferedImage blur, String outPat) {
        try {
            blur = HeatMapUtil.blur(blur, 25);
            int width = blur.getWidth();
            int height = blur.getHeight();
            //获取像素点
            for (int i = 0; i< width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = blur.getRGB(i, j);
                    //从pixel中获取rgba的值
                    int a = (pixel >> 24) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    if (g == 255) {
                        //颜色分级
                        if (a > 0 && a <= 25){
                            blur.setRGB(i, j, new Color(0, 0, 255, 15).getRGB());
                        }
                        if (a > 25 && a <= 50){
                            blur.setRGB(i, j, new Color(0, 255, 0, 160).getRGB());
                        }
                        if (a > 50 && a <= 100){
                            blur.setRGB(i, j, new Color(255, 255, 0, 185).getRGB());
                        }
                        if (a > 100 && a <= 125){
                            blur.setRGB(i, j, new Color(255, 213, 0, 200).getRGB());
                        }
                        if (a > 125 && a <= 150){
                            blur.setRGB(i, j, new Color(255, 171, 0, 215).getRGB());
                        }
                        if (a > 125 && a <= 150){
                            blur.setRGB(i, j, new Color(255, 129, 0, 225).getRGB());
                        }
                        if (a > 150 && a <= 175){
                            blur.setRGB(i, j, new Color(255, 87, 0, 235).getRGB());
                        }
                        if (a > 175 && a <= 200){
                            blur.setRGB(i, j, new Color(255, 42, 0, 245).getRGB());
                        }
                        if (a > 200){
                            blur.setRGB(i, j, new Color(255, 0, 0, 255).getRGB());
                        }
                    }
                }
            }
            blur = HeatMapUtil.blur(blur, 10);
            //输出
            ImageIO.write(blur,"png",new File(outPat));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 高斯模糊
     * @param source 数据源
     * @param radius 模糊半径
     * @return
     */
    public static BufferedImage blur(BufferedImage source, int radius) {
        BufferedImage img = new BufferedImage(source.getWidth() + radius
                * 2, source.getHeight() + radius * 2,
                BufferedImage.TRANSLUCENT);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setColor(new Color(0,0,0,0));
        g2.fillRect(0, 0, source.getWidth() + radius * 2,
                source.getHeight() + radius * 2);
        g2.drawImage(source, radius, radius, null);
        g2.dispose();
        int square = radius * radius;
        float sum = 0;
        float[] matrix = new float[square];
        for (int i = 0; i < square; i++) {
            int dx = i % radius - radius / 2;
            int dy = i / radius - radius / 2;
            matrix[i] = (float) (radius - Math.sqrt(dx * dx + dy * dy));
            sum += matrix[i];
        }
        for (int i = 0; i < square; i++) {
            matrix[i] /= sum;
        }
        BufferedImageOp op = new ConvolveOp(new Kernel(radius, radius,
                matrix), ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage res = op.filter(img, null);
        BufferedImage out = new BufferedImage(source.getWidth(),
                source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g3 = (Graphics2D) out.getGraphics();
        g3.drawImage(res, -radius, -radius, null);
        g3.dispose();
        return out;
    }
}
