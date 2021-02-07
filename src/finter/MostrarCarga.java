package finter;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MostrarCarga extends JPanel{

	public MostrarCarga(JScrollPane listaCarga) {
		
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("DATOS CARGADOS"));
		
		add(listaCarga, BorderLayout.CENTER);
		
	}
	
}