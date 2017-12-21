import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
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

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle(Constants.STAGE_TITLE);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        /*StackPane splashScenePane = new StackPane();
        splashScenePane.setAlignment(Pos.CENTER);

        ImageView arkImageView = new ImageView("http://www.theark.in/images/logo_white.png");
        arkImageView.setPreserveRatio(true);
        arkImageView.setFitWidth(400);
        arkImageView.setFitHeight(300);

        splashScenePane.getChildren().add(arkImageView);

        Scene launchScene = new Scene(splashScenePane);
        launchScene.setFill(Color.web("#16272E"));

        primaryStage.setScene(launchScene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        try
        {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }*/

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

        Canvas imageCanvas = new Canvas(Constants.STAGE_WIDTH, 636);
        imageGraphicsContext = imageCanvas.getGraphicsContext2D();

        DropShadow dropShadowRed = new DropShadow(16, Color.RED);
        pictureButtons.get(0).getImageView().setEffect(dropShadowRed);

        imageGraphicsContext.drawImage(pictureButtons.get(0).getImage(), 0, 0, Constants.STAGE_WIDTH, 636);
        currentlySelectedPictureButton = pictureButtons.get(0);

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

            Image image1 = new Image(new FileInputStream("images\\image-01.jpg"));
            ImageView imageView1 = new ImageView(image1);
            PictureButton pictureButton1 = new PictureButton(imageView1, image1);
            pictureButton1.setId(1);

            Image image2 = new Image(new FileInputStream("images\\image-02.jpg"));
            ImageView imageView2 = new ImageView(image2);
            PictureButton pictureButton2 = new PictureButton(imageView2, image2);
            pictureButton2.setId(2);

            Image image3 = new Image(new FileInputStream("images\\image-03.jpg"));
            ImageView imageView3 = new ImageView(image3);
            PictureButton pictureButton3 = new PictureButton(imageView3, image3);
            pictureButton3.setId(3);

            Image image4 = new Image(new FileInputStream("images\\image-04.jpg"));
            ImageView imageView4 = new ImageView(image4);
            PictureButton pictureButton4 = new PictureButton(imageView4, image4);
            pictureButton4.setId(4);

            Image image5 = new Image(new FileInputStream("images\\image-05.jpg"));
            ImageView imageView5 = new ImageView(image5);
            PictureButton pictureButton5 = new PictureButton(imageView5, image5);
            pictureButton5.setId(5);

            Image image6 = new Image(new FileInputStream("images\\image-06.jpeg"));
            ImageView imageView6 = new ImageView(image6);
            PictureButton pictureButton6 = new PictureButton(imageView6, image6);
            pictureButton5.setId(6);

            Image image7 = new Image(new FileInputStream("images\\image-07.jpg"));
            ImageView imageView7 = new ImageView(image7);
            PictureButton pictureButton7 = new PictureButton(imageView7, image7);
            pictureButton5.setId(7);

            Image image8 = new Image(new FileInputStream("images\\image-08.jpg"));
            ImageView imageView8 = new ImageView(image8);
            PictureButton pictureButton8 = new PictureButton(imageView8, image8);
            pictureButton5.setId(8);

            Image image9 = new Image(new FileInputStream("images\\image-09.jpg"));
            ImageView imageView9 = new ImageView(image9);
            PictureButton pictureButton9 = new PictureButton(imageView9, image9);
            pictureButton5.setId(9);

            Image image10 = new Image(new FileInputStream("images\\image-10.jpg"));
            ImageView imageView10 = new ImageView(image10);
            PictureButton pictureButton10 = new PictureButton(imageView10, image10);
            pictureButton5.setId(10);

            Image image11 = new Image(new FileInputStream("images\\image-11.jpg"));
            ImageView imageView11 = new ImageView(image11);
            PictureButton pictureButton11 = new PictureButton(imageView11, image11);
            pictureButton5.setId(11);

            Image image12 = new Image(new FileInputStream("images\\image-12.jpg"));
            ImageView imageView12 = new ImageView(image12);
            PictureButton pictureButton12 = new PictureButton(imageView12, image12);
            pictureButton5.setId(12);

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

                    imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
                    imageGraphicsContext.drawImage(pictureButton.getImage(), 0, 0, Constants.STAGE_WIDTH, 636);

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


        WritableImage newImage = new WritableImage(reader, (int) (image.getWidth() / 8), (int) (image.getHeight() / 8),
                (int) (image.getWidth() / 1.2), (int) (image.getHeight() / 1.2));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage2 = new WritableImage(reader, (int) (image.getWidth() / 7), (int) (image.getHeight() / 7),
                (int) (image.getWidth() / 1.4), (int) (image.getHeight() / 1.4));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage2, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage3 = new WritableImage(reader, (int) (image.getWidth() / 6), (int) (image.getHeight() / 6),
                (int) (image.getWidth() / 1.6), (int) (image.getHeight() / 1.6));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage3, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage4 = new WritableImage(reader, (int) (image.getWidth() / 5), (int) (image.getHeight() / 5),
                (int) (image.getWidth() / 1.8), (int) (image.getHeight() / 1.8));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage4, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage5 = new WritableImage(reader, (int) image.getWidth() / 4, (int) image.getHeight() / 4,
                (int) image.getWidth() / 2, (int) image.getHeight() / 2);

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage5, 0, 0, Constants.STAGE_WIDTH, 636);

        /*for (double i = 1; i < 2; i = i + 0.1)
        {
            int imageWidth = (int) (image.getWidth() / i);
            int imageHeight = (int) (image.getHeight() / i);
            int imageX = (int)image.getWidth() - imageWidth;
            int imageY = (int)image.getHeight() - imageHeight;

            System.out.println("\nImage width, height: " + (int) image.getWidth() + ", " + (int) image.getHeight());
            System.out.println("I: " + i);
            System.out.println("Calc. Image width, height: " + imageWidth + ", " + imageHeight);
            System.out.println("Calc. Image x, y: " + imageX + ", " + imageY);

            WritableImage newImage = new WritableImage(reader, imageX, imageY, imageWidth, imageHeight);

            imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
            imageGraphicsContext.drawImage(newImage, 0, 0, Constants.STAGE_WIDTH, 636);

            try
            {
                TimeUnit.MILLISECONDS.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }*/
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

        WritableImage newImage4 = new WritableImage(reader, (int) (image.getWidth() / 5), (int) (image.getHeight() / 5),
                (int) (image.getWidth() / 1.8), (int) (image.getHeight() / 1.8));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage4, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage3 = new WritableImage(reader, (int) (image.getWidth() / 6), (int) (image.getHeight() / 6),
                (int) (image.getWidth() / 1.6), (int) (image.getHeight() / 1.6));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage3, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        WritableImage newImage2 = new WritableImage(reader, (int) (image.getWidth() / 7), (int) (image.getHeight() / 7),
                (int) (image.getWidth() / 1.4), (int) (image.getHeight() / 1.4));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage2, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }


        WritableImage newImage = new WritableImage(reader, (int) (image.getWidth() / 8), (int) (image.getHeight() / 8),
                (int) (image.getWidth() / 1.2), (int) (image.getHeight() / 1.2));

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage, 0, 0, Constants.STAGE_WIDTH, 636);

        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(image, 0, 0, Constants.STAGE_WIDTH, 636);

        /*for (double i = 2; i >= 1; i = i - 0.1)
        {
            double imageWidth = currentlySelectedPictureButton.getImage().getWidth() / i;
            double imageHeight = currentlySelectedPictureButton.getImage().getHeight() / i;

            PixelReader reader = currentlySelectedPictureButton.getImage().getPixelReader();
            WritableImage newImage = new WritableImage(reader, 0, 0, (int) imageWidth, (int) imageHeight);
            imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
            imageGraphicsContext.drawImage(newImage, 0, 0, Constants.STAGE_WIDTH, 636);

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

        PixelReader reader = currentlySelectedPictureButton.getImage().getPixelReader();
        WritableImage newImage = new WritableImage(reader, 0, 0,
                (int) currentlySelectedPictureButton.getImage().getWidth(), (int) currentlySelectedPictureButton.getImage().getHeight());
        imageGraphicsContext.clearRect(0, 0, Constants.STAGE_WIDTH, 636);
        imageGraphicsContext.drawImage(newImage, 0, 0, Constants.STAGE_WIDTH, 636);*/
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