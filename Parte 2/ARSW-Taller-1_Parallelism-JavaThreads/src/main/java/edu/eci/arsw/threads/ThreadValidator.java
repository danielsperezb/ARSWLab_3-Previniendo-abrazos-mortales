/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.threads;

import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

/**
 *
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohórquez
 * @author Juan Francisco Terán Roman
 * @author Juan Felipe Vivas Manrique
 */

public class ThreadValidator extends Thread {

	private final int rangoInicial;
	private final int rangoFinal;
	String ipaddress;

	private int ocurrencesCount = 0;
	private int checkedListsCount = 0;

	private static final int BLACK_LIST_ALARM_COUNT = 5;
	private final HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
	private LinkedList<Integer> blackListOcurrences = null;

	public ThreadValidator(String ipAddres, int rangoInicial, int rangoFinal, LinkedList<Integer> blackListOcurrences) {
		this.ipaddress = ipAddres;
		this.rangoInicial = rangoInicial;
		this.rangoFinal = rangoFinal;
		this.blackListOcurrences = blackListOcurrences;
	}

	@Override
	public void run() {
		// System.out.println(this.rangoInicial + "-" + this.rangoFinal);

		for (int i = this.rangoInicial; i < this.rangoFinal; i++) {
			checkedListsCount++;

			if (blackListOcurrences.size() < BLACK_LIST_ALARM_COUNT) {

				if (skds.isInBlackListServer(i, ipaddress)) {
					synchronized (blackListOcurrences) {
						blackListOcurrences.add(i);
						ocurrencesCount++;
					}
				}
			} else {
				break;
			}
		}
		System.out.println("Para el thread " + ThreadValidator.currentThread().getName() + " se encontraron "
				+ ocurrencesCount + " ocurrencias");
	}

	public int getCheckedListsCount() {
		return checkedListsCount;
	}

	public LinkedList<Integer> getBlackListOcurrences() {
		return blackListOcurrences;
	}

	public int getOcurrencesCount() {
		return ocurrencesCount;
	}

}