/*
 
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
*/
package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohórquez
 * @author Juan Francisco Terán Roman
 * @author Juan Felipe Vivas Manrique
 */
public class Main {

	public static void main(String a[]) {

		int nucleos = Runtime.getRuntime().availableProcessors();
		System.out.println("Número de núcleos de procesamiento: " + nucleos);

		Scanner scanners = new Scanner(System.in);
		System.out.println("Escriba ip de servidor sospechoso: ");
		String hostSospechoso = scanners.next();

		long inicialTime = System.currentTimeMillis();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Escriba la cantidad de hilos que quiere utilizar: ");
		int numeroHilos = scanner.nextInt();

		HostBlackListsValidator hblv = new HostBlackListsValidator();
		List<Integer> blackListOcurrences = hblv.checkHost(hostSospechoso, numeroHilos);
		long finalTime = System.currentTimeMillis();

		System.out.println("The host was found in the following blacklists:" + blackListOcurrences
				+ " and the process took: " + (finalTime - inicialTime) + "ms");
	}

}
