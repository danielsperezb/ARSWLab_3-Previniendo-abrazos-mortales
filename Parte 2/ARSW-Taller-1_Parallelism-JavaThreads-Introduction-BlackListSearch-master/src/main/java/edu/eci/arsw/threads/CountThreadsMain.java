/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String a[]) throws InterruptedException {
  
        CountThread primerHilo = new CountThread(0, 99);
        CountThread segundoHilo = new CountThread(99, 199);
        CountThread tercerHilo = new CountThread(200, 299);
        
        System.out.println("Hilo principal: "+ Thread.currentThread().getName());
        primerHilo.start();
        segundoHilo.start();
        tercerHilo.start();
    }
    
}

