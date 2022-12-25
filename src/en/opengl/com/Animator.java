package en.opengl.com;

import en.opengl.com.dimention.three.Renderer3D;
import en.opengl.com.dimention.two.Renderer2D;
import en.opengl.com.entity.Matrix4x4;
import en.opengl.com.entity.Obj3D;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector3D;
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
import java.util.List;

import static en.opengl.com.params.Properties.*;

public class Animator extends JFrame implements KeyListener {
    private static Obj3D object;
    public static Vector3D min;
    public static Vector3D max;
    private static boolean shape = false;
    private static boolean fill = false;
    private static boolean light = true;
    private static boolean cords = false;
    private static boolean debug = false;
    private static boolean switcher = true;

    private static void draw(BufferedImage img, Matrix4x4 mResult, Matrix4x4 pMatrix) {
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        List<Polygon3D> polygons;
        Projection projection = Projection.FRONT;

        if (switcher)
            polygons = Renderer2D.render(object.polygons, img, projection, lightX, lightY, lightZ, mResult, shape, fill, light, cords);
        else
            polygons = Renderer3D.render(object.polygons, img, lightX, lightY, lightZ, mResult, pMatrix, shape, fill, light, cords);

        if (debug) {
            setMinMax(polygons);
            g2.drawString(String.format("Lights (%d %d %d)", lightX, lightY, lightZ), 20, h - 20);
            g2.drawString(String.format("Min (%d %d %d)", (int) min.x, (int) min.y, (int) min.z), 20, h - 40);
            g2.drawString(String.format("Max (%d %d %d)", (int) max.x, (int) max.y, (int) max.z), 20, h - 60);
        }
    }

    private static void setMinMax(List<Polygon3D> polygons) {
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            for (Vector3D vertex : vertexes) {
                if (vertex.x > maxX) maxX = vertex.x;
                if (vertex.y > maxY) maxY = vertex.y;
                if (vertex.z > maxZ) maxZ = vertex.z;
                if (vertex.x < minX) minX = vertex.x;
                if (vertex.y < minY) minY = vertex.y;
                if (vertex.z < minZ) minZ = vertex.z;
            }
        }
        min = new Vector3D(minX, minY, minZ);
        max = new Vector3D(maxX, maxY, maxZ);
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
        setObject(0);

        while (true) {
            long rate = framerate;
            long start = System.currentTimeMillis();
            BufferStrategy bs = jf.getBufferStrategy();
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.clearRect(0, 0, jf.getWidth(), jf.getHeight());
            img = new BufferedImage(w + 1, h + 1, BufferedImage.TYPE_INT_ARGB);

            if (object != null) {
                Matrix4x4 resulMatrix = Matrix4x4.multiply(new Matrix4x4[]{
                        Matrix4x4.getShiftMatrix(-object.dx / 2, -object.dy / 2, -object.dz / 2),
                        Matrix4x4.getRotationMatrixX(thetaX),
                        Matrix4x4.getRotationMatrixY(thetaY),
                        Matrix4x4.getRotationMatrixZ(thetaZ),
                        Matrix4x4.getShiftMatrix(object.dx / 2, object.dy / 2, object.dz / 2),
                        Matrix4x4.getMultiplicationMatrix(scale),
                        Matrix4x4.getShiftMatrix(shiftX, shiftY, shiftZ)
                });
                Matrix4x4 projectMatrix = Matrix4x4.getProjectionMatrix(fNear, fFar, fAspectRatio, fFovRad);

                draw(img, resulMatrix, projectMatrix);
            }

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
            case '0':
                setObject(0);
                break;
            case '1':
                setObject(1);
                break;
            case '2':
                setObject(2);
                break;
            case '3':
                setObject(3);
                break;
            case '*':
                thetaX = 0;
                thetaY = 0;
                thetaZ = 0;
                scale = 1;
                shiftX = 0;
                shiftY = 0;
                shiftZ = 0;
                lightX = 0;
                lightY = 0;
                lightZ = 1000;
                stretchX = 0;
                stretchY = 0;
                stretchZ = 0;
                speed = 1 / 20.0;
                speedShift = 10;
                speedLight = 50;
                fNear = 0.1;
                fFar = 1000.0;
                fFov = 90.0;
                fAspectRatio = h / (double) w;
                fFovRad = 1.0 / Math.tan(fFov * 0.5 / 180.0 * Math.PI);
                break;
            case '[':
                shape = !shape;
                break;
            case ']':
                cords = !cords;
                break;
            case ';':
                light = !light;
                break;
            case '\'':
                fill = !fill;
                break;
            case 'z':
                debug = !debug;
                break;
            case 'x':
                switcher = !switcher;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
