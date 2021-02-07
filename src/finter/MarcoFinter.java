package finter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.*;

@SuppressWarnings("serial")
class MarcoFinter extends JFrame {
	//////////////// INFORMACION PARA LOS CALCULOS //////////////////
	private int cantidadPuntos = 0;
	private ArrayList<Point2D.Double> puntos = new ArrayList<Point2D.Double>(1);
	private double xCargar = 0;
	private double fxCargar = 0;
	private double kEspecializar = 0;
	private double xAgregar = 0;
	private double fxAgregar = 0;
	private double xEliminar = 0;
	private String entradaX = "";
	private String entradaFx = "";
	private Polinomio polinomio = null;
	private String stringCalculo = "";
	private String ultimoMetodoUsado = "";
	
	//////////////// INGRESAR DATOS ////////////////
	private JButton cargarDatos, calcularPorLagrange, calcularProgresivo, calcularRegresivo;
	private JTextField escribirXi, escribirFXi;
	
	//////////////// MOSTRAR PUNTOS CARGADOS ////////////////
	private JScrollPane listaCarga;
	private JTextArea areaCarga;
	
	//////////////// MOSTRAR PASOS DE CALCULO ////////////////
	private JScrollPane pantallaCalculo;
	private JTextArea areaCalculo;
	
	//////////////// ESPECIALIZAR POLINOMIO ////////////////
	private JLabel resultado, valorIngresado;
	private JTextField escribirPunto;
	private JButton calcularEspecializacion;
	
	//////////////// ALTERAR VALORES INICIALES ////////////////
	private JButton botonAgregarPar, botonEliminarPar;
	private JTextField eliminar, nuevoX, nuevoFX;
	
	//////////////// FINALIZAR ///////////////
	private JButton finalizar;
	
	public MarcoFinter() {
		
		setTitle("FINTER");
		setSize(1000,650);
		setLocationRelativeTo(null);
		
		////////////// LAMINA GENERAL //////////////		
		JPanel laminaGeneral = new JPanel();
		laminaGeneral.setLayout(new GridLayout(1,2));
		
		///////////////////// LAMINA IZQUIERDA /////////////////////
		JPanel laminaIzquierda = new JPanel();
		laminaIzquierda.setLayout(new GridLayout(2,2));
		
		////////////////////////////////////////////////////////////
		
		
		
		///////////////////////////////////////////////////////////
		
		inicializarIngresarDatos();
		IngresarDatos laminaIngresoDeDatos = new IngresarDatos(escribirXi, escribirFXi, cargarDatos, calcularPorLagrange,
				calcularProgresivo, calcularRegresivo);
		
		inicializarEspecializarPolinomio();
		EspecializarPolinomio laminaEspecializarPolinomio = new EspecializarPolinomio(valorIngresado, resultado, escribirPunto,
				calcularEspecializacion);
		
		inicializarMostrarCarga();
		MostrarCarga laminaMostrarCarga = new MostrarCarga(listaCarga);
		
		inicializarAlterarValoresIniciales();
		AlterarValores laminaAlterarValores = new AlterarValores(eliminar, nuevoX, nuevoFX, botonAgregarPar, botonEliminarPar);
		
		laminaIzquierda.add(laminaIngresoDeDatos);
		laminaIzquierda.add(laminaEspecializarPolinomio);
		laminaIzquierda.add(laminaMostrarCarga);
		laminaIzquierda.add(laminaAlterarValores);	
		
		///////////////////// LAMINA DERECHA /////////////////////
		JPanel laminaDerecha = new JPanel();
		laminaDerecha.setLayout(new BorderLayout());
		
		inicializarMostrarCalculo();
		MostrarCalculo calculo = new MostrarCalculo(pantallaCalculo);
		
		finalizar = new JButton("Finalizar");
		finalizar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Finalizado");
				cantidadPuntos = 0;
				valorIngresado.setText("");
				resultado.setText("");
				polinomio = null;
				puntos = new ArrayList<Point2D.Double>(1);
				areaCarga.setText("");
				areaCalculo.setText("");
				stringCalculo = "";
				ultimoMetodoUsado = "";
			}
			
		});
		
		laminaDerecha.add(calculo, BorderLayout.CENTER);
		laminaDerecha.add(finalizar, BorderLayout.SOUTH);
		
		///////////////////// LAMINA GENERAL /////////////////////
		laminaGeneral.add(laminaIzquierda);
		laminaGeneral.add(laminaDerecha);
		add(laminaGeneral);
		
	}
	
	///////////////////// INGRESO DE DATOS ////////////////////////
	
	private void inicializarIngresarDatos() {
		
		escribirXi = new JTextField(17);
		escribirXi.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)
						&& (caracter != ',') && (caracter != '(') && (caracter != ')')) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		escribirFXi = new JTextField(17);
		escribirFXi.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)
						&& (caracter != ',') && (caracter != '(') && (caracter != ')')) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		cargarDatos = new JButton("Cargar Datos");
		cargarDatos.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				entradaX = escribirXi.getText();
				escribirXi.setText("");
				entradaFx = escribirFXi.getText();
				escribirFXi.setText("");
				
				if(entradaX.length() > 0 && entradaFx.length() > 0) {
					if(entradaX.charAt(0) == '(') {// INGRESO DE MULTIPLES DATOS
						
						int cantXs = contarCaracteres(entradaX, ',') + 1;
						int cantFxs = contarCaracteres(entradaFx, ',') + 1;
						
						if(cantXs == cantFxs) {
							
							int[] posicionComasX = new int[cantXs-1];
							int[] posicionComasFx = new int[cantFxs-1];
							int posicion = 0;
							for(int i = 0; i < entradaX.length(); i++) {
								if(entradaX.charAt(i) == ',') {
									posicionComasX[posicion] = i;
									posicion++;
								}
							}
							posicion = 0;
							for(int i = 0; i < entradaFx.length(); i++) {
								if(entradaFx.charAt(i) == ',') {
									posicionComasFx[posicion] = i;
									posicion++;
								}
							}
							
							double[] numerosX = new double[cantXs];
							double[] numerosFx = new double[cantFxs];
							numerosX = obtenerNumeros(entradaX, posicionComasX);
							numerosFx = obtenerNumeros(entradaFx, posicionComasFx);
							
							for(int i = 0; i < cantXs; i++) {
								if(!valorXYaIngresado(numerosX[i], puntos)) {
									puntos.add(new Point2D.Double(numerosX[i], numerosFx[i]));
								}
							}
							cantidadPuntos += numerosX.length;
							mostrarDatos();
							
						}
						else {
							JOptionPane.showMessageDialog(null, "Error al ingresar datos", "ERROR", JOptionPane.WARNING_MESSAGE);
						}
						

						
					}
					else { // INGRESO INDIVIDUAL DE DATOS
						if(ingresoIndividualValido(entradaX) && ingresoIndividualValido(entradaFx)) {
							xCargar = Double.parseDouble(entradaX);
							fxCargar = Double.parseDouble(entradaFx);
							
							if(!valorXYaIngresado(xCargar, puntos)) {
								
								puntos.add(new Point2D.Double(xCargar, fxCargar));
								Collections.sort(puntos, new Comparator<Point2D.Double>() {    // ORDENO LOS PUNTOS QUE TENGO CARGADOS
									public int compare(Point2D.Double p1, Point2D.Double p2) {
										return Double.compare(p1.getX(), p2.getX());
									}
								});
													
								cantidadPuntos++;
								mostrarDatos();
							}
							else {
								JOptionPane.showMessageDialog(null, "El valor x = " + xCargar + " ya fue ingresado", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
							
						}
						else {
							JOptionPane.showMessageDialog(null, "Entrada no válida", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			
		});
		
		calcularPorLagrange = new JButton("Lagrange");
		calcularPorLagrange.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(cantidadPuntos > 0) {
					stringCalculo = "";
					polinomio = new Polinomio(cantidadPuntos);
					stringCalculo = polinomio.metodoLagrange(puntos);
					ultimoMetodoUsado = "Lagrange";
					JOptionPane.showMessageDialog(null, "Polinomio de Lagrange Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
					areaCalculo.setText(stringCalculo);
				}
				else {
					JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			
		});
		
		calcularProgresivo = new JButton("Newton-Gregory Progresivo");
		calcularProgresivo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(cantidadPuntos > 0) {
					stringCalculo = "";
					polinomio = new Polinomio(cantidadPuntos);
					stringCalculo = polinomio.metodoProgresivo(puntos);
					ultimoMetodoUsado = "Progresivo";
					JOptionPane.showMessageDialog(null, "Polinomio Progresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
					areaCalculo.setText(stringCalculo);	
				}
				else {
					JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			
		});
		
		calcularRegresivo = new JButton("Newton-Gregory Regresivo");
		calcularRegresivo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(cantidadPuntos > 0) {
					stringCalculo = "";
					polinomio = new Polinomio(cantidadPuntos);
					stringCalculo = polinomio.metodoRegresivo(puntos);
					ultimoMetodoUsado = "Regresivo";
					JOptionPane.showMessageDialog(null, "Polinomio Regresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
					areaCalculo.setText(stringCalculo);
				}
				else {
					JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			
		});
		
	}
	
	//////////////////// ESPECIALIZAR POLINOMIO //////////////////
	
	private void inicializarEspecializarPolinomio() {
		
		valorIngresado = new JLabel();
		valorIngresado.setPreferredSize(new Dimension(200,10));
		
		resultado = new JLabel();
		resultado.setPreferredSize(new Dimension(200,10));
		
		calcularEspecializacion = new JButton("Calcular");
		calcularEspecializacion.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				entradaX = escribirPunto.getText();
				escribirPunto.setText("");
				if(ingresoIndividualValido(entradaX) && polinomio != null) {
					kEspecializar = Double.parseDouble(entradaX);
					resultado.setText(Double.toString(polinomio.especializar(kEspecializar, ultimoMetodoUsado)));
					valorIngresado.setText(Double.toString(kEspecializar));
				}
				else {
					
					if(!ingresoIndividualValido(entradaX)) {
						JOptionPane.showMessageDialog(null, "Entrada no válida", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					else if(polinomio == null) {
						JOptionPane.showMessageDialog(null, "El polinomio no fue calculado", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					
					
				}
				
			}
			
		});
		
		escribirPunto = new JTextField(17);
		escribirPunto.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
	}
	
	/////////////////// MOSTRAR CARGA ////////////////////////////
	
	private void inicializarMostrarCarga() {
		
		areaCarga = new JTextArea();
		areaCarga.setEditable(false);
		listaCarga = new JScrollPane(areaCarga);		
		
	}
	
	////////////////// ALTERAR VALORES INICIALES /////////////////
	
	private void inicializarAlterarValoresIniciales() {
		
		botonAgregarPar = new JButton("Agregar Dato");
		botonAgregarPar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				entradaX = nuevoX.getText();
				nuevoX.setText("");
				entradaFx = nuevoFX.getText();
				nuevoFX.setText("");
				
				if(polinomio != null) {
					
					if(ingresoIndividualValido(entradaX) && ingresoIndividualValido(entradaFx)) {
						
						xAgregar = Double.parseDouble(entradaX);
						fxAgregar = Double.parseDouble(entradaFx);
						
						if(!valorXYaIngresado(xAgregar, puntos)) {
							
							puntos.add(new Point2D.Double(xAgregar, fxAgregar));
							Collections.sort(puntos, new Comparator<Point2D.Double>() {    // ORDENO LOS PUNTOS QUE TENGO CARGADOS
								public int compare(Point2D.Double p1, Point2D.Double p2) {
									return Double.compare(p1.getX(), p2.getX());
								}
							});											
							cantidadPuntos++;
							
							////////////// CALCULO DE NUEVO EL POLINOMIO //////////////////////
							
							if(ultimoMetodoUsado == "Lagrange") {
								if(cantidadPuntos > 0) {
									stringCalculo = "";
									polinomio = new Polinomio(cantidadPuntos);
									stringCalculo = polinomio.metodoLagrange(puntos);
									ultimoMetodoUsado = "Lagrange";
									JOptionPane.showMessageDialog(null, "Polinomio de Lagrange Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
									areaCalculo.setText(stringCalculo);
								}
								else JOptionPane.showMessageDialog(null, "Polinomio de Lagrange Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
							}
							else if(ultimoMetodoUsado == "Progresivo") {
								if(cantidadPuntos > 0) {
									stringCalculo = "";
									polinomio = new Polinomio(cantidadPuntos);
									stringCalculo = polinomio.metodoProgresivo(puntos);
									JOptionPane.showMessageDialog(null, "Polinomio Progresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
								}
								else {
									JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
								}
							}
							else if(ultimoMetodoUsado == "Regresivo") {
								if(cantidadPuntos > 0) {
									stringCalculo = "";
									polinomio = new Polinomio(cantidadPuntos);
									stringCalculo = polinomio.metodoRegresivo(puntos);
									JOptionPane.showMessageDialog(null, "Polinomio Regresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
								}
								else {
									JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
								}
							}
							
							////////////// ACTUALIZO LOS PUNTOS CARGADOS //////////////////
							areaCarga.setText("");
							String stringCarga = "";				
							
							if(puntos.size() == 0) {
								areaCarga.setText("   NO HAY DATOS CARGADOS");
							}
							else {
								
								for(int i = 0; i < puntos.size(); i++) {
									stringCarga += i+1 + ")  x = " + String.format("%.8f",puntos.get(i).getX()) + ",   f(x) = " + 
									String.format("%.8f",puntos.get(i).getY()) + "\n";
								}
								areaCarga.setText(stringCarga);
							}
							
							///////////// ACTUALIZO LOS CALCULOS EN PANTALLA ///////////////////
							areaCalculo.setText(stringCalculo);
							
						}
						else {
							JOptionPane.showMessageDialog(null, "El valor x = " + xAgregar + " ya fue ingresado", "ERROR", JOptionPane.WARNING_MESSAGE);
						}

					}
					else {
						JOptionPane.showMessageDialog(null, "Entrada no válida", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "El polinomio no fue calculado", "ERROR", JOptionPane.WARNING_MESSAGE);
				}

				
			}
			
		});
		
		botonEliminarPar = new JButton("Eliminar Dato");
		botonEliminarPar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				entradaX = eliminar.getText();
				eliminar.setText("");
				if(ingresoIndividualValido(entradaX)) {
					
					if(puntos.size() > 0) {
						
						xEliminar = Double.parseDouble(entradaX);
						
						Iterator<Point2D.Double> it = puntos.iterator();
						boolean eliminado = false;					
						
						while(it.hasNext() && !eliminado) {
							
							if(it.next().getX() == xEliminar) {
								it.remove();
								eliminado = true;
								cantidadPuntos--;
							}
							
						}
						//////////////// CALCULO DE NUEVO EL POLINOMIO ///////////////
						if(ultimoMetodoUsado == "Lagrange") {
							if(cantidadPuntos > 0) {
								stringCalculo = "";
								polinomio = new Polinomio(cantidadPuntos);
								stringCalculo = polinomio.metodoLagrange(puntos);
								ultimoMetodoUsado = "Lagrange";
								JOptionPane.showMessageDialog(null, "Polinomio de Lagrange Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
								areaCalculo.setText(stringCalculo);
							}
							else {
								areaCarga.setText("   NO HAY DATOS CARGADOS");
								stringCalculo = "";
								JOptionPane.showMessageDialog(null, "Polinomio de Lagrange Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else if(ultimoMetodoUsado == "Progresivo") {
							if(cantidadPuntos > 0) {
								stringCalculo = "";
								polinomio = new Polinomio(cantidadPuntos);
								stringCalculo = polinomio.metodoProgresivo(puntos);
								JOptionPane.showMessageDialog(null, "Polinomio Progresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								areaCarga.setText("   NO HAY DATOS CARGADOS");
								stringCalculo = "";
								JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
						}
						else if(ultimoMetodoUsado == "Regresivo") {
							if(cantidadPuntos > 0) {
								stringCalculo = "";
								polinomio = new Polinomio(cantidadPuntos);
								stringCalculo = polinomio.metodoRegresivo(puntos);
								JOptionPane.showMessageDialog(null, "Polinomio Regresivo Calculado", "AVISO", JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								stringCalculo = "";
								areaCarga.setText("   NO HAY DATOS CARGADOS");
								JOptionPane.showMessageDialog(null, "No se ingresaron datos", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
						}
						
						////////////// ACTUALIZO LOS PUNTOS CARGADOS //////////////////
						areaCarga.setText("");
						String stringCarga = "";				
						
						if(puntos.size() == 0) {
							areaCarga.setText("   NO HAY DATOS CARGADOS");
						}
						else {
							
							for(int i = 0; i < puntos.size(); i++) {
								stringCarga += i+1 + ")  x = " + String.format("%.8f",puntos.get(i).getX()) + ",   f(x) = " + 
								String.format("%.8f",puntos.get(i).getY()) + "\n";
							}
							areaCarga.setText(stringCarga);
						}
						
						///////////// ACTUALIZO LOS CALCULOS EN PANTALLA ///////////////////
						areaCalculo.setText(stringCalculo);
						
					}
					else {
						areaCarga.setText("NO HAY DATOS CARGADOS");
						JOptionPane.showMessageDialog(null, "No hay puntos para borrar", "ERROR", JOptionPane.WARNING_MESSAGE);
					}					
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Entrada no válida", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		eliminar = new JTextField(17);
		eliminar.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		nuevoX = new JTextField(17);
		nuevoX.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		nuevoFX = new JTextField(17);
		nuevoFX.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char caracter = e.getKeyChar();
				if((caracter < '0' || caracter > '9') && (caracter != '.') && (caracter != '-') && (caracter != KeyEvent.VK_BACK_SPACE)) {
					e.consume();					
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
	}
	
	////////////////// MOSTRAR CALCULO ///////////////////////////
	
	private void inicializarMostrarCalculo() {
		
		areaCalculo = new JTextArea();
		areaCalculo.setEditable(false);
		pantallaCalculo = new JScrollPane(areaCalculo);
		
	}
	
	/////////////////// AUXILIARES //////////////////////////////
	
	private boolean ingresoIndividualValido(String entrada) {
		
		if(entrada.length() != 0) {// HAY ESCRITO ALGO
			
			if(entrada.indexOf('-') == -1 && entrada.indexOf('.') == -1 ) return true;// NO HAY NUMERO NEGATIVO NI DECIMAL

			else if(entrada.indexOf('-') != -1 && entrada.indexOf('.') == -1) {// HAY NUMERO NEGATIVO, PERO NO HAY DECIMAL
				
				if(cantidadRepeticiones(entrada, '-') > 1) return false;
				else {						
					if(entrada.charAt(0) == '-') return true;// EL PRIMER CARACTER ES EL -
					else return false;
				}
				
			}
			else if(entrada.indexOf('-') == -1 && entrada.indexOf('.') != -1) {// NO HAY NUMERO NEGATIVO, PERO HAY DECIMAL
				
				if(cantidadRepeticiones(entrada, '.') > 1) return false;
				else {
					
					if(entrada.charAt(0) != '.' && entrada.charAt(entrada.length()-1) != '.') {// EL PRIMER CARACTER NO ES EL .
						return true;														   // TAMPOCO EL ULTIMO
					}
					else return false;	
				}
				
			}
			else {// HAY NUMERO NEGATIVO Y DECIMAL
				
				if(cantidadRepeticiones(entrada, '-') == 1 && cantidadRepeticiones(entrada, '.') == 1) {// HAY UN SOLO - Y UN SOLO .
					
					if(entrada.charAt(0) == '-' && entrada.charAt(1) != '.') {// EL PRIMER CARACTER ES EL -, EL SEGUNDO CARACTER NO ES EL .
						
						if(entrada.charAt(entrada.length()-1) != '.') return true;// EL PUNTO NO ES EL ULTIMO CARACTER
						else return false;
					}
					else return false;
				}
				else return false;
			}
			
		}
		else return false;
}
	
	private int cantidadRepeticiones(String palabra, char caracter) {
		
		int resultado = 0;
		
		for(int i = 0; i < palabra.length(); i++) {
			
			if(palabra.charAt(i) == caracter) {
				resultado++;
			}
			
		}
		
		return resultado;
		
	}
	
	private boolean valorXYaIngresado(Double x, ArrayList<Point2D.Double> lista) {
		
		boolean res = false;
		int i = 0;
		
		while(!res && i < lista.size()) {
			
			if(x == lista.get(i).getX()) {
				res = true;
			}
			i++;
			
		}
		
		return res;
		
	}
	
	private int contarCaracteres(String s, char c) {
		int cant = 0;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i)== ',') cant++;
		}
		return cant;
	}
	
	private double[] obtenerNumeros(String s, int[] posicionComas) {
		

		double[] res = new double[posicionComas.length+1];
		int numerosCargados = 0;		
		
		for(int i = -1; i < posicionComas.length; i++) {
			
			if(i == -1) {
				res[numerosCargados] = Double.parseDouble(s.substring(1,posicionComas[i+1]));
				numerosCargados++;
			}
			else if(i+1 == posicionComas.length) {
				res[numerosCargados] = Double.parseDouble(s.substring(posicionComas[i]+1, s.length()-1));
				numerosCargados++;
			}
			else {
				res[numerosCargados] = Double.parseDouble(s.substring(posicionComas[i]+1, posicionComas[i+1]));
				numerosCargados++;
			}
			
		}
		
		return res;
		
	}
	
	private void mostrarDatos() {
		areaCarga.setText("");
		String stringCarga = "";				
		
		if(puntos.size() == 0) {
			areaCarga.setText("   NO HAY DATOS CARGADOS");
		}
		else {
			
			for(int i = 0; i < puntos.size(); i++) {
				stringCarga += i+1 + ")  x = " + String.format("%.8f",puntos.get(i).getX()) + ",   f(x) = " + 
				String.format("%.8f",puntos.get(i).getY()) + "\n";
			}
			areaCarga.setText(stringCarga);
		}
	}
	
}