package en.opengl.com.params;

public class ModelFactory {
    public static Model getModel(int index){
        switch (index){
            case 0: return Model.FACE;
            case 1: return Model.GARGOYLE;
            case 2: return Model.TRUC;
            default: return Model.CUBE;
        }
    }
}
