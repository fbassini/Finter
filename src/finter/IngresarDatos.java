package finter;

import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class IngresarDatos extends JPanel {

	public IngresarDatos(JTextField escribeX, JTextField escribeFx, JButton cargar, JButton lagrange, 
			JButton progresivo, JButton regresivo) {
		
		setLayout(new GridLayout(6,1,10,10));
		setBorder(new TitledBorder("INGRESAR DATOS"));
		
		JPanel ingresoX = new JPanel();
		ingresoX.add(new JLabel("   x: "));
		ingresoX.add(escribeX);
		
		JPanel ingresoFx = new JPanel();
		ingresoFx.add(new JLabel("f(x): "));
		ingresoFx.add(escribeFx);
		
		add(ingresoX);
		add(ingresoFx);
		add(cargar);
		add(lagrange);
		add(progresivo);
		add(regresivo);
		
				
	}
	
}