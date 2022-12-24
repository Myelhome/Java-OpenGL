package en.opengl.com;

import en.opengl.com.dimention.two.Renderer;
import en.opengl.com.dimention.two.debug.LightRenderer;
import en.opengl.com.entity.Matrix4x4;
import en.opengl.com.entity.Obj3D;
import en.opengl.com.params.Model;
import en.opengl.com.params.ModelFactory;
import en.opengl.com.params.Projection;
import en.opengl.com.parser.ObjectParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static en.opengl.com.params.Properties.*;

public class Animator extends JFrame implements KeyListener {
    private static Obj3D object;

    private static void draw(BufferedImage img, Matrix4x4 mResult) {
        Projection projection = Projection.FRONT;
        Renderer.render(object.polygons, img, projection, lightX, lightY, lightZ, mResult,false, false, true, false);
    }

    public static void main(String[] args) throws InterruptedException {
        Animator jf = new Animator();
        jf.setSize(w, h);
        jf.setUndecorated(false);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.addKeyListener(jf);
        jf.createBufferStrategy(2);
        jf.setBackground(Color.BLACK);
        BufferedImage img;
        setObject(2);

        while (true) {
            long rate = framerate;
            long start = System.currentTimeMillis();
            BufferStrategy bs = jf.getBufferStrategy();
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.clearRect(0, 0, jf.getWidth(), jf.getHeight());
            img = new BufferedImage(w + 1, h + 1, BufferedImage.TYPE_INT_ARGB);

            Matrix4x4 resulMatrix = Matrix4x4.multiply(new Matrix4x4[]{
                    Matrix4x4.getShiftMatrix(-object.dx/2, -object.dy/2, -object.dz/2),
                    Matrix4x4.getRotationMatrixX(thetaX),
                    Matrix4x4.getRotationMatrixY(thetaY),
                    Matrix4x4.getRotationMatrixZ(thetaZ),
                    Matrix4x4.getShiftMatrix(object.dx/2, object.dy/2, object.dz/2),
                    Matrix4x4.getMultiplicationMatrix(scale),
                    Matrix4x4.getShiftMatrix(shiftX, shiftY, shiftZ)
            });

            draw(img, resulMatrix);

            g.drawImage(img, 0, 0, null);

            bs.show();
            g.dispose();

            long end = System.currentTimeMillis();
            long len = end - start;
            if (len < rate) {
                Thread.sleep(rate - len);
            }
        }
    }

    private static void setObject(int index) {
        Model model = ModelFactory.getModel(index);
        modelpath = model.getModelPath();
        texturepath = model.getTexturePath();
        object = ObjectParser.parseObj(modelpath, w, h);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '+':
                scale += speed;
                break;
            case '-':
                scale -= speed;
                break;
            case 'w':
                thetaX -= speed;
                break;
            case 's':
                thetaX += speed;
                break;
            case 'a':
                thetaY += speed;
                break;
            case 'd':
                thetaY -= speed;
                break;
            case 'q':
                thetaZ -= speed;
                break;
            case 'e':
                thetaZ += speed;
                break;
            case '6':
                shiftX += speedShift;
                break;
            case '4':
                shiftX -= speedShift;
                break;
            case '8':
                shiftY += speedShift;
                break;
            case '5':
                shiftY -= speedShift;
                break;
            case '7':
                shiftZ -= speedShift;
                break;
            case '9':
                shiftZ += speedShift;
                break;
            case 'l':
                lightX += speedLight;
                break;
            case 'j':
                lightX -= speedLight;
                break;
            case 'i':
                lightY += speedLight;
                break;
            case 'k':
                lightY -= speedLight;
                break;
            case 'u':
                lightZ -= speedLight;
                break;
            case 'o':
                lightZ += speedLight;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
