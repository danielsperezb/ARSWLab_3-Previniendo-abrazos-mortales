package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Immortal extends Thread {

    private boolean pause;

    private ImmortalUpdateReportCallback updateCallback = null;

    private int health;

    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
        this.pause = false;
    }

    public void run() {

        while (health > 0) {

            if (this.pause) {
                
                synchronized(this){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Immortal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            Immortal im;

            synchronized (immortalsPopulation) {

                int myIndex = immortalsPopulation.indexOf(this);

                int nextFighterIndex = r.nextInt(immortalsPopulation.size());

                //avoid self-fight
                if (nextFighterIndex == myIndex) {
                    nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                }

                im = immortalsPopulation.get(nextFighterIndex);

                this.fight(im);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        //si esta muerto lo quitamos
        synchronized (immortalsPopulation) {
            immortalsPopulation.remove(this);
        }

    }

    //A QUIEN VAS A ATACAR?
    public void fight(Immortal i2) {

        synchronized (i2) {
            if (i2.getHealth() > 0) {
                //
                i2.changeHealth(i2.getHealth() - defaultDamageValue);
                //PAUSE AND CHECK
                synchronized (this) {
                    this.health += defaultDamageValue;
                }

                updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
            } else {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }
        }

    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        //TODOS TIENEN ACCESO A LA VIDA DE OTRO
        synchronized (this) {
            return health;
        }
    }

    public void pause() {
        this.pause = true;
    }

    public void resumeGame() {
        this.pause = false;
        synchronized (this) {
            notifyAll();
        }

    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
