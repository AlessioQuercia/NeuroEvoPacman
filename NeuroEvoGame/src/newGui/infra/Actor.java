package newGui.infra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.joml.Vector2d;

/**
 * Actor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Actor<T extends Game> implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final boolean DRAW_COLLIDER = false;
    
    public T game;
    public double x, y;
	public boolean visible;
    transient public BufferedImage frame;
    transient public BufferedImage[] frames;
    public Rectangle collider; 
    
    protected int instructionPointer;
    protected long waitTime;
    
    
    public Actor(T game) {
        this.game = game;
    }
    
    public void init() {
    }
    
    public void update() {
    }
    
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        if (frame != null) {
            g.drawImage(frame, (int) x, (int) y, frame.getWidth(), frame.getHeight(), null);
        }
        if (DRAW_COLLIDER && collider != null) {
            updateCollider();
            g.setColor(Color.RED);
            g.draw(collider);
        }
    }
    
    protected void loadFrames(String ... framesRes) {
        try {
            frames = new BufferedImage[framesRes.length];
            for (int i = 0; i < framesRes.length; i++) {
                String frameRes = framesRes[i];
                frames[i] = ImageIO.read(getClass().getResourceAsStream(frameRes));
            }
            frame = frames[0];
        } catch (IOException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    } 

    public void updateCollider() {
        if (collider != null) {
            collider.setLocation((int) x, (int) y);
        }
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(frame, "png", buffer);

        out.writeInt(buffer.size()); // Prepend image with byte count
        buffer.writeTo(out);         // Write image
        
        out.writeInt(frames.length); // how many images are serialized?

        for (int i = 0; i<frames.length; i++) 
        {
            buffer = new ByteArrayOutputStream();
            ImageIO.write(frames[i], "png", buffer);

            out.writeInt(buffer.size()); // Prepend image with byte count
            buffer.writeTo(out);         // Write image
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException 
    {
        in.defaultReadObject();
        
        int size = in.readInt(); // Read byte count

        byte[] buffer = new byte[size];
        in.readFully(buffer); // Make sure you read all bytes of the image

        frame = ImageIO.read(new ByteArrayInputStream(buffer));

        int imageCount = in.readInt();
        frames = new BufferedImage[imageCount];
        for (int i = 0; i < imageCount; i++) 
        {
            size = in.readInt(); // Read byte count

            buffer = new byte[size];
            in.readFully(buffer); // Make sure you read all bytes of the image

            frames[i] = ImageIO.read(new ByteArrayInputStream(buffer));
        }
    }
}
