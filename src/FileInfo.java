public class FileInfo{
    private String name;
    private String path;
    private String type;

    public FileInfo(String name, String path, String type){
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public String getName(){
        return name;
    }
    public String getPath(){
        return path;
    }
    public String getType(){
        return type;
    }
    @Override
    public String toString(){
        return name;
    }
}

