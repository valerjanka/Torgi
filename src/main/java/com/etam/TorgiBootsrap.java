package com.etam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: vryzhuk
 * Date: 5/7/14
 * Time: 2:45 PM
 */
public class TorgiBootsrap {
    private final static ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    public static HashMap<String, Item> itemMap = new HashMap<String, Item>();
    public static Test clientGet = new Test();

    public static void main(String[] args) {
        int everyMinutes = 2;
        if (args.length == 1) {
            try {
                everyMinutes = 60 * Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        final File file = new File("lots.txt");
        if (file.exists()) {
            readValues(file);
        }

        final Runnable runnableClient = new Runnable() {

            @Override
            public void run() {
                System.out.println("Time:" + new Date());
                List<Item> serverItems = null;
                try {
                    serverItems = clientGet.getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean wasChanged = false;
                for(Item item : serverItems) {
                    Item oldItem = itemMap.get(item.lot);
                    if(!item.equals(oldItem)) {
                        wasChanged = true;
                        if(oldItem == null) {
                            System.out.println("New lot:" + item.lot + "=" + item);
                        } else {
                            System.out.println("Changed lot:" + item.lot);
                            System.out.println("/t Old value=" + oldItem);
                            System.out.println("/t New value=" + item);
                        }
                        itemMap.put(item.lot, item);
                    }
                }
                if(wasChanged) {
                    writeToFile(file);
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnableClient, 0, everyMinutes, TimeUnit.MINUTES);
    }

    private static void writeToFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            for(Item value : itemMap.values()) {
                bw.write(value + "\n");
            }
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void readValues(File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String link = line.substring(line.indexOf("link=") + 6, line.indexOf(", lot")-1);
                String lot = line.substring(line.indexOf("lot=") + 5, line.indexOf(", name")-1);
                String name = line.substring(line.indexOf("name=") + 6, line.indexOf(", price")-1);
                String price = line.substring(line.indexOf("price=") + 7, line.indexOf(", endRegistration")-1);
                String endRegistration = line.substring(line.indexOf("endRegistration=") + 17, line.indexOf(", beginDate")-1);
                String beginDate = line.substring(line.indexOf("beginDate=") + 11, line.indexOf(", place")-1);
                String place = line.substring(line.indexOf("place=") + 7, line.indexOf("'}"));
                itemMap.put(lot, new Item(link, lot, name, price, endRegistration, beginDate, place));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

