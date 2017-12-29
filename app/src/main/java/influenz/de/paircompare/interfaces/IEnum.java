package influenz.de.paircompare.interfaces;

import org.opencv.core.Scalar;

public interface IEnum {

    final class FaceEnum
    {
        public static final int Face1Index = 0;
        public static final int Face2Index = 0;
        public static final int minFaceSize = 1;
        public static final int minFacesFound = 0; // starting with 0
    }
    final class ThicknessEnum
    {
        public static final int rectAngleFace = 3;
        public static final int rectAngleEyes = 2;
        public static final int strokeWidth = 1;
    }
    final class FontSizeEnum
    {
        public static final double faceCounter = 0.9;

    }
    final class ScalarEnum
    {
        public static final Scalar scalarFace = new Scalar(255, 255, 255);
        public static final Scalar scalarText = new Scalar(255, 255, 255,255);
        public static final Scalar scalarEyes = new Scalar(255, 0, 0, 255);
    }
    final class IntentKeyEnum
    {
        public static final String face1_key = "face1_bitmap";
        public static final String face2_key = "face2_bitmap";
    }
    final class RadiusEnum
    {
        public static final int canvasRadius = 3;
    }
    final class LandmarkCodesEnum
    {

        // Landmarks codes: Here's a good picture which describes it very well
        // ( https://ibug.doc.ic.ac.uk/media/uploads/images/annotpics/figure_68_markup.jpg
        // 0 - 16 shape of face
        // 17 - 21 - right eyebrow
        // 22 - 27 - left eyebrow
        // 27 - 30 - Nose length
        // 31 - 35 - Nose width
        // 36 - 41 - Right eye
        // 42 - 47 ... and so on
        public static final int startShapeIndex = 0;
        public static final int endShapeIndex = 16;
        public static final int startChinIndex = 6;
        public static final int endChinIndex = 11;
        public static final int startRightEyeBrowIndex = 17;
        public static final int endRightEyeBrowIndex = 22;
        public static final int startLeftEyeBrowIndex = 22;
        public static final int endLeftEyeBrowIndex = 27;
        public static final int startNoseLongitudeIndex = 27;
        public static final int endNoseLongitudeIndex = 31;
        public static final int startNoseLatitudeIndex = 31;
        public static final int endNoseLatitudeIndex = 36;
        public static final int startRightEyeIndex = 36;
        public static final int endRightEyeIndex = 42;
        public static final int startLeftEyeIndex = 42;
        public static final int endLeftEyeIndex = 48;
        public static final int startOuterLipIndex = 48;
        public static final int endOuterLipIndex = 60;
        public static final int startInnerLipIndex = 60;
        public static final int endInnerLipIndex = 68;



    }

}
