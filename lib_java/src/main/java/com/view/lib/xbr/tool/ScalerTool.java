package com.view.lib.xbr.tool;

import com.view.lib.xbr.Xbrz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ScalerTool {

    public static BufferedImage scaleImage(BufferedImage source, int factor) {
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();
        int[] srcPixels = new int[srcWidth * srcHeight];
        boolean hasAlpha = source.getColorModel().hasAlpha();
        source.getRGB(0, 0, srcWidth, srcHeight, srcPixels, 0, srcWidth);

        int destWidth = srcWidth * factor;
        int destHeight = srcHeight * factor;
        int[] destPixels = new int[destWidth * destHeight];
        Xbrz.scaleImage(factor, hasAlpha, srcPixels, destPixels, srcWidth, srcHeight);

        BufferedImage dest = new BufferedImage(destWidth, destHeight,
                hasAlpha ? BufferedImage.TYPE_INT_ARGB
                        : BufferedImage.TYPE_INT_RGB);
        dest.setRGB(0, 0, destWidth, destHeight, destPixels, 0, destWidth);
        return dest;
    }

    public static String imageSource = "C:\\Users\\41798\\Desktop\\Alien.png";
    public static String imageTarget = "C:\\Users\\41798\\Desktop\\Alien_6.png";

    public static void main(String[] args) throws Exception {
        scaleImage(imageSource, imageTarget, 6);
    }

    public static void scaleImage(String imageSource, String imageOutput, int factor) throws IOException {
        BufferedImage source = ImageIO.read(new File(imageSource));
        BufferedImage scaled = scaleImage(source, factor);
        ImageIO.write(scaled, "png", new File(imageOutput));
    }

    private static void printUsage() {
        System.err.println("Usage: java -jar xbrz.jar <source> [scaling_factor]");
    }

}
