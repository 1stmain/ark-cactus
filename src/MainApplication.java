import javafx.application.Application;
import javafx.stage.Stage;

class MainApplication extends Application implements KinectHelper
{
    public void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {

    }

    @Override
    public void onRightHandPushed(boolean isRightHandPushed)
    {

    }

    @Override
    public void onRightHandMoved(int rightHandX, int rightHandY)
    {

    }
}