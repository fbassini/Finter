package finter;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MostrarCalculo extends JPanel {

	public MostrarCalculo(JScrollPane pantallaCalculo) {
		
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("CALCULO REALIZADO"));

		add(pantallaCalculo, BorderLayout.CENTER);
		
	}
	
}