import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

class Kinect extends J4KSDK
{
    private KinectHelper kinectHelper;
    private boolean rightHandIsPushed = false;
    private boolean isInitialised = false;
    private float oldRightHandX = 0;
    private float oldRightHandZ = 0;
    private float oldLeftHandX = 0;
    private int swipeLeftFrameCount = 0;
    private int swipeRightFrameCount = 0;

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
        //float leftHandY = skeleton.get2DJoint(Skeleton.HAND_LEFT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float leftHandZ = skeleton.get3DJointZ(Skeleton.HAND_LEFT);

        // Initialise some variables
        if (!isInitialised)
        {
            oldRightHandZ = rightHandZ;
            oldRightHandX = rightHandX;
            oldLeftHandX = leftHandX;
            isInitialised = true;
        }

        kinectHelper.onRightHandMoved((int) rightHandX, (int) rightHandY);

        // Detect the right hand being pushed forward
        if (rightHandZ < oldRightHandZ && oldRightHandZ - rightHandZ > 0.3)
        {
            rightHandIsPushed = true;
            kinectHelper.onRightHandPushed(true);
        }

        // Detect the right hand not being pushed forward
        else if (oldRightHandZ - rightHandZ < 0.3 && rightHandIsPushed)
        {
            rightHandIsPushed = false;
            kinectHelper.onRightHandPushed(false);
        }

        // Detect right hand being swiped to the right
        if ((rightHandZ < oldRightHandZ && oldRightHandZ - rightHandZ > 0.3) &&
                (oldRightHandX < rightHandX && rightHandX - oldRightHandX >= 10))
        {
            swipeRightFrameCount++;

            if (swipeRightFrameCount > 4)
            {
                kinectHelper.onRightHandSwipedRight();
            }
        }

        // Detect right hand being swiped to the left
        else if ((rightHandZ < oldRightHandZ && oldRightHandZ - rightHandZ > 0.3) &&
                (oldRightHandX > rightHandX && oldRightHandX - rightHandX >= 10))
        {
            swipeLeftFrameCount++;

            if (swipeLeftFrameCount > 4)
            {
                kinectHelper.onRightHandSwipedLeft();
            }
        }
        else
        {
            swipeLeftFrameCount = 0;
            swipeRightFrameCount = 0;
        }

        // Detect right hand and left hand being swiped away from each other
        /*if ((oldRightHandX < rightHandX && rightHandX - oldRightHandX >= 50) &&
                (oldLeftHandX > leftHandX && oldLeftHandX - leftHandX >= 50))
        {
            if (leftHandZ < rightHandZ && rightHandZ - leftHandZ < 0.2)
            {
                kinectHelper.onZoomInDetected();
            }
            else if (leftHandZ > rightHandZ && leftHandZ - rightHandZ < 0.2)
            {
                kinectHelper.onZoomInDetected();
            }
        }

        // Detect right hand and left hand being swiped towards each other
        else if ((oldRightHandX > rightHandX && oldRightHandX - rightHandX >= 50) &&
                (oldLeftHandX < leftHandX && leftHandX - oldLeftHandX >= 50))
        {
            if (leftHandZ < rightHandZ && rightHandZ - leftHandZ < 0.2)
            {
                kinectHelper.onZoomOutDetected();
            }
            else if (leftHandZ > rightHandZ && leftHandZ - rightHandZ < 0.2)
            {
                kinectHelper.onZoomOutDetected();
            }
        }*/

        oldRightHandX = rightHandX;
        oldLeftHandX = leftHandX;
    }
}