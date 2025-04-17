package net.javamio.playerkits.storage;

import net.javamio.playerkits.storage.file.KitFile;
import net.javamio.playerkits.storage.file.KitRoomFile;

public class StorageManager {

    private final KitRoomFile kitRoomFile;
    private final KitFile kitFile;

    public StorageManager(){
        this.kitRoomFile = new KitRoomFile();
        this.kitFile = new KitFile();

        kitFile.init();
        kitRoomFile.init();
    }
}
