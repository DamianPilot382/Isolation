
import java.util.HashMap;

public class Storage {

    private HashMap<Long, HashMap<Integer, Integer>> storage;

    public Storage(){
        this.storage = new HashMap<Long, HashMap<Integer, Integer>>();
    }

    public void insert(long hash, int position, int value){
        if(!this.storage.containsKey(hash))
            this.storage.put(hash, new HashMap<Integer, Integer>());

        this.storage.get(hash).put(position, value);
    }

    public int getValue(long hash, int position){
        if(!this.storage.containsKey(hash))
            return -1;

        if(!this.storage.get(hash).containsKey(position))
            return -1;
        
        return this.storage.get(hash).get(position);
    }

}