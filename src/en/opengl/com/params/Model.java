package en.opengl.com.params;

public enum Model {
    GARGOYLE("C:\\DELETE\\garg.obj", null),
    CUBE("C:\\DELETE\\cube.obj", null),
    FACE("C:\\DELETE\\african_head.obj", "C:\\DELETE\\african_head_diffuse.jpg"),
    TRUC("C:\\DELETE\\uaz.obj", "C:\\DELETE\\uaz_med_white_d.png");
    private final String modelPath;
    private final String texturePath;

    Model(String modelPath, String texturePath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
