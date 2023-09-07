package edu.eci.arsw.threads;

/**
 *
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohórquez
 * @author Juan Francisco Terán Roman
 * @author Juan Felipe Vivas Manrique
 */
public class CountThread extends Thread {

	private final int rangoInicial;
	private final int rangoFinal;

	CountThread(int rangoInicial, int rangoFinal) {
		this.rangoInicial = rangoInicial;
		this.rangoFinal = rangoFinal;
	}

	@Override
	public void run() {
		System.out.println("Este es el hilo que se está ejecutando: " + Thread.currentThread().getName());
		for (int i = rangoInicial; i <= rangoFinal; i++) {
			System.out.println(i);
		}
	}
}
