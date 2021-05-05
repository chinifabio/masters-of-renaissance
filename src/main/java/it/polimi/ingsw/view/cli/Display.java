package it.polimi.ingsw.view.cli;

import java.util.Arrays;

public class Display implements Runnable{

    //Aggiornare ogni 20 ms
    private final String[][] pixels;

    private final int height;

    private final int width;

    public Display(int height, int width) {
        this.height = height;
        this.width = width;

        this.pixels = new String[height][width];
    }


    @Override
    public void run() {
        while (true){

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Stampa il display

        }
    }

    public synchronized void draw(String[][] image, int height, int width, int x, int y){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.pixels[y+i][x+j] = image[y][x];
            }
        }
    }

    public synchronized void draw(String[] image, int lenght, int x, int y){
        Arrays.stream(image).iterator();
    }

}
