package en.opengl.com.params;

import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;

import static en.opengl.com.params.Properties.h;

public class DotFactory {
    public static Vector2D getDot(Projection projection, Vector3D vector){
        switch (projection){
            case FRONT: return new Vector2D(vector.x, h - vector.y);
            case TOP: return new Vector2D(vector.z, vector.x);
            case LEFT: return new Vector2D(vector.z, h - vector.y);
            default: return null;
        }
    }
}
