/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProduction {
    
    
    public static void main(String[] args) {
        
         Queue<Integer> queue=new LinkedBlockingQueue<>();
       
         new Producer(queue,Long.MAX_VALUE).start();
        
        //let the producer create products for 5 seconds (stock).
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(StartProduction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        new Consumer(queue).start();
    }
    

}

