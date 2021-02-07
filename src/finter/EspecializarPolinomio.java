package finter;

import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class EspecializarPolinomio extends JPanel {
	
	public EspecializarPolinomio(JLabel valorIngresado, JLabel resultado, JTextField escribirPunto, JButton calcularEspecializacion) {
		
		setLayout(new GridLayout(6,1,10,10));
		setBorder(new TitledBorder("ESPECIALIZAR POLINOMIO"));
		
		JPanel ingresoX = new JPanel();
		ingresoX.add(new JLabel("   k: "));
		ingresoX.add(escribirPunto);
		add(ingresoX);
		add(calcularEspecializacion);
		/////////////////////////////////////////
		JPanel laminaDominio = new JPanel();
		laminaDominio.add(new JLabel("   k: "));
		laminaDominio.add(valorIngresado);
		add(laminaDominio);
		/////////////////////////////////////////
		JPanel laminaImagen = new JPanel();
		laminaImagen.add(new JLabel("P(k):"));
		laminaImagen.add(resultado);
		add(laminaImagen);
		
		
	}

}