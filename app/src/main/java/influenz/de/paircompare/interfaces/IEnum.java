package influenz.de.paircompare.interfaces;

import org.opencv.core.Scalar;

public interface IEnum {

    final class FaceEnum
    {
        public static final int minFaceSize = 3;
        public static final int minFacesFound = 0;
    }
    final class ThicknessEnum
    {
        public static final int rectAngleFace = 3;
        public static final int rectAngleEyes = 2;
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

}
