import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

class Kinect extends J4KSDK
{
    private KinectHelper kinectHelper;
    private boolean rightHandIsPushed = false;
    private boolean isInitialised = false;
    private int oldRightHandZ;

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

        float rightHandX = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[0];
        float rightHandY = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float rightHandZ = skeleton.get3DJointZ(Skeleton.HAND_RIGHT);

        if (!isInitialised)
        {
            oldRightHandZ = (int) rightHandZ;
            isInitialised = true;
        }

        if (rightHandZ < oldRightHandZ && oldRightHandZ - rightHandZ > 0.2)
        {
            rightHandIsPushed = true;
            kinectHelper.onRightHandPushed(true);
        }

        if (oldRightHandZ - rightHandZ < 0.2 && rightHandIsPushed)
        {
            rightHandIsPushed = false;
            kinectHelper.onRightHandPushed(false);
        }


        kinectHelper.onRightHandMoved((int) rightHandX, (int) rightHandY);
    }
}