package net.fpalacios.cabinet;

import net.fpalacios.cabinet.view.states.Application;

public class Main {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		Application.start();
	}
}
