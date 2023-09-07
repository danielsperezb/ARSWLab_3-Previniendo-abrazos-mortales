/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

/**
 *
 * @author daniel.perez-b
 */


public class ThreadValidator extends Thread{
    
    
    private final int rangoInicial;
    private final int rangoFinal;
    String ipaddress;
    
    
    private int ocurrencesCount=0;
    private int checkedListsCount=0;
    
    private static final int BLACK_LIST_ALARM_COUNT = 5;
    private final HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
    private final LinkedList<Integer> blackListOcurrences=new LinkedList<>();

    public ThreadValidator(String ipAddres, int rangoInicial, int rangoFinal){
        this.ipaddress = ipAddres;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
    }
    
    @Override
    public void run() {
         for (int i = this.rangoInicial; i < this.rangoFinal && ocurrencesCount < BLACK_LIST_ALARM_COUNT; i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
        System.out.println("Para el thread "+ ThreadValidator.currentThread().getName() + " se encontraron " + ocurrencesCount + " ocurrencias");
    }

    
    public int getCheckedListsCount() {
        return checkedListsCount;
    }

    public LinkedList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }
    
}
