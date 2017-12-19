interface KinectHelper
{
    void onRightHandPushed(boolean isRightHandPushed);

    void onRightHandMoved(int rightHandX, int rightHandY);

    void onRightHandSwipedRight();

    void onRightHandSwipedLeft();

    void onZoomInDetected();

    void onZoomOutDetected();
}