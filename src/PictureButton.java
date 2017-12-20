import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureButton
{
    private Image image;
    private int id;
    private ImageView imageView;

    PictureButton(ImageView imageView, Image image)
    {
        this.image = image;
        this.imageView = imageView;
    }

    void setId(int id)
    {
        this.id = id;
    }

    int getId()
    {
        return this.id;
    }

    Image getImage()
    {
        return image;
    }

    ImageView getImageView()
    {
        return imageView;
    }

    private Rectangle2D getBoundary()
    {
        return new Rectangle2D(imageView.localToScene(imageView.getBoundsInLocal()).getMinX(), 652, 200, 100);
    }

    ImageView getButtonImage()
    {
        ImageView anImageView = imageView;
        anImageView.setFitWidth(200);
        anImageView.setFitHeight(100);
        return anImageView;
    }

    boolean intersects(Cursor cursor)
    {
        return cursor.getBoundary().intersects(this.getBoundary());
    }
}