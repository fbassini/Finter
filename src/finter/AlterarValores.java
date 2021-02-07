package finter;

import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class AlterarValores extends JPanel {

	public AlterarValores(JTextField eliminar, JTextField nuevoX, JTextField nuevoFX, JButton botonAgregarPar,
			JButton botonEliminarPar) {
		
		setLayout(new GridLayout(2,1));
		setBorder(new TitledBorder("ALTERAR VALORES INICIALES"));
		
		JPanel agregar = new JPanel();
		agregar.setLayout(new GridLayout(3,1,10,10));
		agregar.setBorder(new TitledBorder("Agregar Valores"));
		//////////////////////////////////////
		JPanel ingresoX = new JPanel();
		ingresoX.add(new JLabel("   x: "));
		ingresoX.add(nuevoX);
		agregar.add(ingresoX);
		/////////////////////////////////////
		JPanel ingresoFx = new JPanel();
		ingresoFx.add(new JLabel("f(x): "));
		ingresoFx.add(nuevoFX);
		agregar.add(ingresoFx);
		/////////////////////////////////////
		agregar.add(botonAgregarPar);
		add(agregar);
		
		////////////////////////////////////////////////////////////
		
		JPanel laminaEliminar = new JPanel();
		laminaEliminar.setLayout(new GridLayout(3,1,10,10));
		laminaEliminar.setBorder(new TitledBorder("Eliminar Valores"));
		/////////////////////////////////////
		JPanel ingresoEliminarX = new JPanel();
		ingresoEliminarX.add(new JLabel("   x: "));
		ingresoEliminarX.add(eliminar);
		laminaEliminar.add(ingresoEliminarX);
		laminaEliminar.add(botonEliminarPar);
		add(laminaEliminar);
		
	}
	
}