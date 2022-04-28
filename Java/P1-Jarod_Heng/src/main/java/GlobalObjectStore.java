import java.util.HashMap;
import java.util.Map;

public class GlobalObjectStore {
    private static final Map<String, Object> globalStore = new HashMap<String, Object>();

    private GlobalObjectStore(){
        //no need to build an object
    }

    public static void addObject(String name, Object obj) {
        globalStore.put(name, obj);
        System.out.println("Object added: " + obj);
    }

    public static Object getObject(String name) {
        Object obj = globalStore.get(name);
        System.out.println("Object retrieved: " + obj);
        return obj;
    }

    public static void removeObject(String name) {
        Object obj = globalStore.remove(name);
        System.out.println("Object removed: " + obj);
    }
}