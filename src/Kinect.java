import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

import java.util.ArrayList;

class Kinect extends J4KSDK
{
    private KinectHelper kinectHelper;

    // Right hand window variables
    private ArrayList<Double> rightHandXWindow = new ArrayList<>();
    private ArrayList<Double> rightHandYWindow = new ArrayList<>();
    private ArrayList<Double> rightHandZWindow = new ArrayList<>();

    // Left hand window variables
    private ArrayList<Double> leftHandXWindow = new ArrayList<>();
    private ArrayList<Double> leftHandYWindow = new ArrayList<>();
    private ArrayList<Double> leftHandZWindow = new ArrayList<>();

    Kinect(KinectHelper aKinectHelper)
    {
        if (!System.getProperty("os.arch").toLowerCase().contains("64"))
        {
            System.out.println("WARNING: You are running a 32 bit version of Java.");
            System.out.println("Doing so may significantly reduce the performance of this application.");
        }

        kinectHelper = aKinectHelper;
    }

    @Override
    public void onDepthFrameEvent(short[] shorts, byte[] bytes, float[] floats, float[] floats1)
    {
        // This method is never used
    }

    @Override
    public void onColorFrameEvent(byte[] bytes)
    {
        // This method is never used
    }

    @Override
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] floats, float[] floats1, byte[] bytes)
    {
        int skeletonNumber = 0;

        for (boolean isSkeletonTracked : skeleton_tracked)
        {
            if (isSkeletonTracked)
            {
                break;
            }
            skeletonNumber++;
        }

        Skeleton skeleton = getSkeletons()[skeletonNumber];

        // Right hand coordinates
        float rightHandX = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[0];
        float rightHandY = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float rightHandZ = skeleton.get3DJointZ(Skeleton.HAND_RIGHT);

        // Left hand coordinates
        float leftHandX = skeleton.get2DJoint(Skeleton.HAND_LEFT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[0];
        float leftHandY = skeleton.get2DJoint(Skeleton.HAND_LEFT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float leftHandZ = skeleton.get3DJointZ(Skeleton.HAND_LEFT);

        // Torso coordinates
        float torsoY = skeleton.get2DJoint(Skeleton.SPINE_MID, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];

        rightHandXWindow.add((double) rightHandX);
        rightHandYWindow.add((double) rightHandY);
        rightHandZWindow.add((double) rightHandZ);

        leftHandXWindow.add((double) leftHandX);
        leftHandYWindow.add((double) leftHandY);
        leftHandZWindow.add((double) leftHandZ);

        if (rightHandXWindow.size() > 30)
        {
            rightHandXWindow.remove(0);
            rightHandYWindow.remove(0);
            rightHandZWindow.remove(0);

            leftHandXWindow.remove(0);
            leftHandYWindow.remove(0);
            leftHandZWindow.remove(0);
        }
        else
        {
            return;
        }

        kinectHelper.onRightHandMoved((int) rightHandX, (int) rightHandY);

        // Detect the right hand being pushed forward
        if ((rightHandY > torsoY) && (rightHandZWindow.get(0) > rightHandZWindow.get(29)) &&
                (rightHandZWindow.get(0) - rightHandZWindow.get(29) >= 0.2))
        {
            kinectHelper.onRightHandPushed(true);
        }

        // Detect the right hand not being pushed forward
        else if ((rightHandY > torsoY) &&
                (rightHandZWindow.get(0) < rightHandZWindow.get(29)) &&
                (rightHandZWindow.get(29) - rightHandZWindow.get(0) >= 0.2))
        {
            kinectHelper.onRightHandPushed(false);
        }

        // Detect right hand being swiped to the right
        if ((rightHandXWindow.get(0) < rightHandXWindow.get(29) &&
                rightHandXWindow.get(29) - rightHandXWindow.get(0) >= 400))
        {
            kinectHelper.onRightHandSwipedRight();
        }

        // Detect right hand being swiped to the left
        else if ((rightHandXWindow.get(0) > rightHandXWindow.get(29) &&
                rightHandXWindow.get(0) - rightHandXWindow.get(29) >= 400))
        {
            kinectHelper.onRightHandSwipedLeft();
        }

        // Detect right hand and left hand being swiped away from each other
        /*if ((oldRightHandX < rightHandX && rightHandX - oldRightHandX >= 20) &&
                (oldLeftHandX > leftHandX && oldLeftHandX - leftHandX >= 20))
        {
            if (leftHandZ < rightHandZ && rightHandZ - leftHandZ < 0.1)
            {
                if (zoomFrameCount > 29)
                {
                    kinectHelper.onZoomInDetected();
                    zoomFrameCount = 0;
                }
            }
            else if (leftHandZ > rightHandZ && leftHandZ - rightHandZ < 0.1)
            {
                if (zoomFrameCount > 29)
                {
                    kinectHelper.onZoomInDetected();
                    zoomFrameCount = 0;
                }
            }
        }

        // Detect right hand and left hand being swiped towards each other
        else if ((oldRightHandX > rightHandX && oldRightHandX - rightHandX >= 20) &&
                (oldLeftHandX < leftHandX && leftHandX - oldLeftHandX >= 20))
        {
            if (leftHandZ < rightHandZ && rightHandZ - leftHandZ < 0.1)
            {
                if (zoomFrameCount > 29)
                {
                    kinectHelper.onZoomOutDetected();
                    zoomFrameCount = 0;
                }
            }
            else if (leftHandZ > rightHandZ && leftHandZ - rightHandZ < 0.1)
            {
                if (zoomFrameCount > 29)
                {
                    kinectHelper.onZoomOutDetected();
                    zoomFrameCount = 0;
                }
            }
        }

        oldRightHandX = rightHandX;
        oldLeftHandX = leftHandX;*/
    }
}