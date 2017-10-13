package org.nutc.thesis.development_identifypalmacupoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ColorBlobDetector {
	// Lower and Upper bounds for range checking in HSV color space
    /*private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);*/
    // Minimum contour area in percent for contours filtering
    private static double mMinContourArea = 0.1;
    // Color radius for range checking in HSV color space
    private Scalar mColorRadius = new Scalar(25,50,50,0);
    private Mat mSpectrum = new Mat();
    private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();

    // Cache
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat dstTemp1 = new Mat();
    Mat dstTemp2 = new Mat();
    Mat mMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();

    public void setColorRadius(Scalar radius) {
        mColorRadius = radius;
    }

    public void setHsvColor(Scalar hsvColor) {
        double minH = (hsvColor.val[0] >= mColorRadius.val[0]) ? hsvColor.val[0]-mColorRadius.val[0] : 0;
        double maxH = (hsvColor.val[0]+mColorRadius.val[0] <= 255) ? hsvColor.val[0]+mColorRadius.val[0] : 255;

        /*mLowerBound.val[0] = minH;
        mUpperBound.val[0] = maxH;

        mLowerBound.val[1] = hsvColor.val[1] - mColorRadius.val[1];
        mUpperBound.val[1] = hsvColor.val[1] + mColorRadius.val[1];

        mLowerBound.val[2] = hsvColor.val[2] - mColorRadius.val[2];
        mUpperBound.val[2] = hsvColor.val[2] + mColorRadius.val[2];

        mLowerBound.val[3] = 0;
        mUpperBound.val[3] = 255;*/

        Mat spectrumHsv = new Mat(1, (int)(maxH-minH), CvType.CV_8UC3);

        for (int j = 0; j < maxH-minH; j++) {
            byte[] tmp = {(byte)(minH+j), (byte)255, (byte)255};
            spectrumHsv.put(0, j, tmp);
        }

        //Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_HSV2RGB_FULL, 4);
        Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_YCrCb2RGB, 4);
    }

    public Mat getSpectrum() {
        return mSpectrum;
    }

    public void setMinContourArea(double area) {
        mMinContourArea = area;
    }

    public void process(Mat rgbaImage) {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

        //Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2YCrCb);

        //Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask); //HSV 觸碰點顏色平均範圍
        //Core.inRange(mHsvMat, new Scalar(0, 58, 89), new Scalar(25, 173, 229), mMask); //HSV 膚色範圍
        //Core.inRange(mHsvMat, new Scalar(0, 133, 77), new Scalar(255, 177, 127), mMask); //YCbCr 膚色範圍
        
        //YCbCr 膚色範圍
        Core.inRange(mHsvMat, new Scalar(0, 133, 0), new Scalar(256, 173, 256), dstTemp1);
        Core.inRange(mHsvMat, new Scalar(0, 0, 77), new Scalar(256, 256, 127), dstTemp2);
        Core.bitwise_and(dstTemp1, dstTemp2, mMask); //進行 "AND" 邏輯運算
        
        //形態學 - 開啟運算 MORPH_OPEN
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(13, 13)); //new Size(X, Y), X&&Y <15
        Imgproc.morphologyEx(mMask, mDilatedMask, Imgproc.MORPH_OPEN, kernel);
        /*Imgproc.dilate(mMask, mDilatedMask, new Mat(),new Point(-1, -1), 1); //膨脹運算
        Imgproc.erode(mMask, mDilatedMask, new Mat(),new Point(-1, -1), 1); //侵蝕運算*/

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        //Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

        // Find max contour area
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            if (area > maxArea)
                maxArea = area;
        }

        // Filter contours by area and resize to fit the original image size
        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);
            }
        }
    }

    public List<MatOfPoint> getContours() {
        return mContours;
    }
}
