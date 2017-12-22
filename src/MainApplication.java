import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainApplication extends Application implements KinectHelper
{
    private GraphicsContext cursorGraphicsContext;
    private GraphicsContext imageGraphicsContext;
    private Cursor cursor;
    private int oldRightHandX;
    private int oldRightHandY;
    private ScrollPane scrollPane;
    private ArrayList<PictureButton> pictureButtons = new ArrayList<>();
    private PictureButton currentlySelectedPictureButton;
    private boolean isZomedIn = false;
    private Double canvasAreaHeight;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle(Constants.STAGE_TITLE);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        setImages();
        setBlackShadowToAllButtons();

        StackPane rootNode = new StackPane();
        VBox vBox = new VBox();

        HBox hBox = new HBox(32);
        hBox.setPadding(new Insets(16, 16, 16, 16));

        for (PictureButton pictureButton : pictureButtons)
        {
            hBox.getChildren().add(pictureButton.getButtonImage());
        }

        scrollPane = new ScrollPane(hBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //get the visible screen resolution
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        // canvas area for displaying the selected image is a % of the complete screen
        canvasAreaHeight = Constants.CANVAS_AREA*primaryScreenBounds.getHeight();
        Canvas imageCanvas = new Canvas(primaryScreenBounds.getWidth(), canvasAreaHeight);
        imageGraphicsContext = imageCanvas.getGraphicsContext2D();

        currentlySelectedPictureButton = pictureButtons.get(0);

        DropShadow dropShadowRed = new DropShadow(16, Color.RED);
        currentlySelectedPictureButton.getImageView().setEffect(dropShadowRed);

        placeImageOnCanvas(currentlySelectedPictureButton.getImage());

        Canvas cursorCanvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        cursorGraphicsContext = cursorCanvas.getGraphicsContext2D();

        vBox.getChildren().addAll(imageCanvas, scrollPane);

        rootNode.getChildren().addAll(vBox, cursorCanvas);

        Scene primaryScene = new Scene(rootNode);

        primaryStage.setScene(primaryScene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        initialiseKinect();

        isZomedIn = false;
    }

    private void initialiseKinect()
    {
        Kinect kinect = new Kinect(this);
        kinect.start(J4KSDK.SKELETON);
    }

    private void setImages()
    {
        try
        {
            cursor = new Cursor();
            cursor.setImage(new FileInputStream("images\\hand.png"));

            PictureButton pictureButton1 = setPictureButton("images\\image-01.jpg",12);
            PictureButton pictureButton2 = setPictureButton("images\\image-02.jpg",12);
            PictureButton pictureButton3 = setPictureButton("images\\image-03.jpg",12);
            PictureButton pictureButton4 = setPictureButton("images\\image-04.jpg",12);
            PictureButton pictureButton5 = setPictureButton("images\\image-05.jpg",12);
            PictureButton pictureButton6 = setPictureButton("images\\image-06.jpg",12);
            PictureButton pictureButton7 = setPictureButton("images\\image-07.jpg",12);
            PictureButton pictureButton8 = setPictureButton("images\\image-08.jpg",12);
            PictureButton pictureButton9 = setPictureButton("images\\image-09.jpg",12);
            PictureButton pictureButton10 = setPictureButton("images\\image-10.jpg",12);
            PictureButton pictureButton11 = setPictureButton("images\\image-11.jpg",12);
            PictureButton pictureButton12 = setPictureButton("images\\image-12.jpg",12);

            pictureButtons.add(pictureButton1);
            pictureButtons.add(pictureButton2);
            pictureButtons.add(pictureButton3);
            pictureButtons.add(pictureButton4);
            pictureButtons.add(pictureButton5);
            pictureButtons.add(pictureButton6);
            pictureButtons.add(pictureButton7);
            pictureButtons.add(pictureButton8);
            pictureButtons.add(pictureButton9);
            pictureButtons.add(pictureButton10);
            pictureButtons.add(pictureButton11);
            pictureButtons.add(pictureButton12);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private PictureButton setPictureButton(String imgPath, int id) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(imgPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        PictureButton pictureButton = new PictureButton(imageView, image);
        pictureButton.setId(id);
        return pictureButton;
    }

    private void placeImageOnCanvas(Image placeImg) {
        double imgWidth = canvasAreaHeight/placeImg.getHeight()*placeImg.getWidth();
        imageGraphicsContext.drawImage(placeImg,
                (Constants.STAGE_WIDTH-imgWidth)/2, 0,
                canvasAreaHeight/placeImg.getHeight()*placeImg.getWidth(), canvasAreaHeight );
    }

    @Override
    public void onRightHandPushed(boolean isRightHandPushed)
    {
        if (isRightHandPushed)
        {
            for (PictureButton pictureButton : pictureButtons)
            {
                if (pictureButton.intersects(cursor))
                {
                    setBlackShadowToAllButtons();

                    Image image = pictureButton.getImage();

                    imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, canvasAreaHeight);
                    placeImageOnCanvas(image);

                    currentlySelectedPictureButton = pictureButton;

                    DropShadow dropShadowRed = new DropShadow(16, Color.RED);
                    pictureButton.getImageView().setEffect(dropShadowRed);

                    break;
                }
            }
        }
    }

    @Override
    public void onRightHandMoved(int rightHandX, int rightHandY)
    {
        if (rightHandX >= oldRightHandX + 5 || rightHandX <= oldRightHandX - 5 ||
                rightHandY >= oldRightHandY + 5 || rightHandY <= oldRightHandY - 5)
        {
            cursorGraphicsContext.clearRect(oldRightHandX, oldRightHandY, 100, 100);
            cursor.setPosition(rightHandX, rightHandY);
            cursor.render(cursorGraphicsContext);

            oldRightHandX = rightHandX;
            oldRightHandY = rightHandY;
        }
    }

    @Override
    public void onRightHandSwipedLeft()
    {
        if (scrollPane.getHvalue() == 1.0)
        {
            return;
        }

        scrollPane.setHvalue(1.0);

        /*Runnable r = new Runnable()
        {
            public void run()
            {
                for (int i = 1; i < 11; i++)
                {
                    scrollPane.setHvalue(i / 10);

                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        };

        Thread t = new Thread(r);
        t.start();*/
    }

    @Override
    public void onRightHandSwipedRight()
    {
        if (scrollPane.getHvalue() == 0.0)
        {
            return;
        }

        scrollPane.setHvalue(0.0);

        /*for (int i = 10; i > 0; i--)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.exit(1);
            }

            scrollPane.setHvalue(i / 10);
        }*/
    }

    @Override
    public void onZoomInDetected()
    {
        if (isZomedIn)
        {
            return;
        }

        isZomedIn = true;

        Image image = currentlySelectedPictureButton.getImage();

        PixelReader reader = image.getPixelReader();

        drawZoomFrame(reader, image, 8, 1.2);

        drawZoomFrame(reader, image, 7, 1.4);

        drawZoomFrame(reader, image, 6, 1.6);

        drawZoomFrame(reader, image, 5, 1.8);

        drawZoomFrame(reader, image, 4, 2);
    }

    @Override
    public void onZoomOutDetected()
    {
        if (!isZomedIn)
        {
            return;
        }

        isZomedIn = false;

        Image image = currentlySelectedPictureButton.getImage();

        PixelReader reader = image.getPixelReader();

        drawZoomFrame(reader, image, 5, 1.8);

        drawZoomFrame(reader, image, 6, 1.6);

        drawZoomFrame(reader, image, 7, 1.4);

        drawZoomFrame(reader, image, 8, 1.2);

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, canvasAreaHeight);
        placeImageOnCanvas(image);

    }

    private void drawZoomFrame(PixelReader reader, Image image, double coordinateDivisor, double sizeDivisor)
    {
        WritableImage newImage = new WritableImage(reader,
                (int) (image.getWidth() / coordinateDivisor),
                (int) (image.getHeight() / coordinateDivisor),
                (int) (image.getWidth() / sizeDivisor), (int) (image.getHeight() / sizeDivisor));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, canvasAreaHeight);
        placeImageOnCanvas(newImage);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setBlackShadowToAllButtons()
    {
        for (PictureButton pictureButton : this.pictureButtons)
        {
            DropShadow dropShadowBlack = new DropShadow(16, Color.BLACK);
            pictureButton.getImageView().setEffect(dropShadowBlack);
        }
    }
}