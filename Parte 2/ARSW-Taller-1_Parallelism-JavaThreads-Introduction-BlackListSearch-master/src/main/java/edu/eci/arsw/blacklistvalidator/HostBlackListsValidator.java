package edu.eci.arsw.blacklistvalidator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.ThreadValidator;

/**
 *
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohórquez
 * @author Juan Francisco Terán Roman
 * @author Juan Felipe Vivas Manrique
 */
public class HostBlackListsValidator {

	private static final int BLACK_LIST_ALARM_COUNT = 5;

	/**
	 * Check the given host's IP address in all the available black lists, and
	 * report it as NOT Trustworthy when such IP was reported in at least
	 * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.The search
	 * is not exhaustive: When the number of occurrences is equal to
	 * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as NOT
	 * Trustworthy, and the list of the five blacklists returned.
	 * 
	 * @param ipaddress suspicious host's IP address.
	 * @param n,        number of threads to do servers search.
	 * @return Blacklists numbers where the given host's IP address was found.
	 */

	// Agregamos el parametro N correspondiente al numero de hilos donde se
	// relaizara la busqueda.
	public List<Integer> checkHost(String ipaddress, Integer n) {

		// Agregamos la lista de hilos.
		List<ThreadValidator> hilos = new ArrayList<>();

		LinkedList<Integer> blackListOcurrences = new LinkedList<>();

		int ocurrencesCount = 0;

		// Consultas en cualquiera de las N listas negras registradas (método
		// 'isInBlacklistServer')
		HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

		// Cuanto le corresponde a cada hilo analizar de las listas negras totales.
		int partesPorHilo = (int) Math.ceil((double) skds.getRegisteredServersCount() / n);

		// Inicialización
		int puntoIncial = 0;
		int puntoFinal = Math.min(partesPorHilo, skds.getRegisteredServersCount());
		int totalOcurrencias = 0;
		int checkedListsCount = 0;

		// Implementatmos un for para crear los hilos dependiendo del parametro de
		// entrada N
		for (int i = 0; i < n; i++) {
			ThreadValidator threadValidator = new ThreadValidator(ipaddress, puntoIncial, puntoFinal);

			puntoIncial = puntoFinal;
			puntoFinal = Math.min(puntoFinal + partesPorHilo, skds.getRegisteredServersCount());

			hilos.add(threadValidator);
			threadValidator.start();
		}

		// Recorremos la lista de hilos para usar el metodo join y esperar a que cada
		// hilo termine su trabajo
		for (ThreadValidator t : hilos) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Error al interrumpir el hilo: " + e.getMessage());
			}
		}

//Este metodo lo pasamos a la clase de ThreadValidator, pero modificado en el
//        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
//            checkedListsCount++;
//
//            if (skds.isInBlackListServer(i, ipaddress)){
//
//                blackListOcurrences.add(i);
//
//                ocurrencesCount++;
//            }
//        }

		// Recorremos la lista de hilos para extrar la informacion de cada hilo sobre:
		// cuantas listas checkearon, cuantas ocurrencias encontraron y en cuales
		// servidores
		for (ThreadValidator t : hilos) {
			checkedListsCount += t.getCheckedListsCount();
			totalOcurrencias += t.getOcurrencesCount();
			blackListOcurrences.addAll(t.getBlackListOcurrences());
		}

		if (totalOcurrencias >= BLACK_LIST_ALARM_COUNT) {
			skds.reportAsNotTrustworthy(ipaddress);
		} else {
			skds.reportAsTrustworthy(ipaddress);
		}

		LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
				new Object[] { checkedListsCount, skds.getRegisteredServersCount() });
		return blackListOcurrences;
	}

	private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

}