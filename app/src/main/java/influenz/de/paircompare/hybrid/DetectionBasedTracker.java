package influenz.de.paircompare.hybrid;


import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public class DetectionBasedTracker {
 public DetectionBasedTracker(final String cascadeName, final int minFaceSize) {
  nativeObj = nativeCreateObject(cascadeName, minFaceSize);
 }

 public void start() {
  nativeStart(nativeObj);
 }

 public void stop() {
  nativeStop(nativeObj);
 }

 public void detect(Mat imageGray, MatOfRect faces) {
  nativeDetect(nativeObj, imageGray.getNativeObjAddr(), faces.getNativeObjAddr());
 }

 public void release() {
  nativeDestroyObject(nativeObj);
  nativeObj = 0;
 }

 public void setFaceSize(final int faceSize) {
  nativeSetFaceSize(nativeObj, faceSize);
 }

 private long nativeObj = 0;

 private static native long nativeCreateObject(final String cascadeName, final int minFaceSize);
 private static native void nativeDestroyObject(final long thiz);
 private static native void nativeStart(final long thiz);
 private static native void nativeStop(final long thiz);
 private static native void nativeSetFaceSize(final long thiz, final int size);
 private static native void nativeDetect(final long thiz, final long inputImage, final long faces);
}
