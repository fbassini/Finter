package finter;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Polinomio {

	private double[] coeficientes;
	private double[] xs;
	private double[] ys;
	private double[] lagrange;
	
	public Polinomio(int grado) {
		
		//maximoGradoPosible = grado;
		coeficientes = new double[grado];
		
		for(int i = 0; i < coeficientes.length; i++) {
			coeficientes[i] = 0;
		}
		
	}
	
	public double especializar(double valor, String metodo) {
		
		double resultado = 0;
		double producto = 1;
		
		if(metodo == "Lagrange") {
			
			double[] valores = new double[lagrange.length];
			for(int i=0;i<lagrange.length;i++){
				producto = 1;
				for(int j=0;j<lagrange.length;j++){					
					if(i!=j) {
						producto =(valor-xs[j])*producto;
					}					
				}
				valores[i]=lagrange[i]*producto;
				System.out.println(valores[i]);
			}
			
			for(int i = 0; i < valores.length; i++) {
				resultado += valores[i];
			}
			
		}
		else if(metodo == "Progresivo") {
			
			for(int i = 0; i < coeficientes.length; i++) {
				for(int j = 0; j < i; j++) {
					producto = producto * (valor - xs[j]);
				}
				resultado += coeficientes[i] * producto;
				producto = 1;
			}
			
		}
		else if(metodo == "Regresivo") {
			
			for(int i = 0; i < coeficientes.length; i++) {
				for(int j = coeficientes.length-1; j > coeficientes.length-1 - i; j--) {
					producto = producto * (valor - xs[j]);
				}
				resultado += coeficientes[i] * producto;
				producto = 1;
			}
			
		}
		
		System.out.println("El resultado es " + resultado);
		return resultado;		
		
	}
	
	public String metodoLagrange(ArrayList<Point2D.Double> puntos) {
		
		String procedimiento = "";
		
		xs = new double[puntos.size()];
		ys = new double[puntos.size()];
		lagrange = new double[puntos.size()];
		
		for(int i = 0; i < puntos.size(); i++) {			
			xs[i] = puntos.get(i).getX();						
			ys[i] = puntos.get(i).getY();
		}
		
		double mult;
		for(int i = 0; i < xs.length; i++) {
			
			mult = 1;
			for(int j = 0; j < lagrange.length; j++) {
				if(i != j) {
					mult = (xs[i]-xs[j]) * mult;
				}
			}
			lagrange[i] = ys[i]/mult;
			procedimiento += escribirLi(mult, i);			
		}
		
		////////////// COEFICIENTES PARA CALCULAR EL GRADO ///////////////
		double[][] y = new double[puntos.size()][puntos.size()];
		for(int i = 0; i < puntos.size(); i++) {
			y[i][0] = puntos.get(i).getY();	
		}
		int desplazamiento = 0;
		for(int i = 1; i < puntos.size(); i++) {
			for(int j = 0; j < puntos.size() - i; j++) {
				y[j][i] = (y[j+1][i-1] - y[j][i-1]) / (xs[i+desplazamiento] - xs[j]);
				desplazamiento++;
			}
			desplazamiento = 0;
		}
		
		for(int i = 0; i < coeficientes.length; i++) {
			coeficientes[i] = y[0][i];			
		}
		
		//////////////////////////////////////////////////////////////////
		
		procedimiento += escribirPolinomioLagrange();
		procedimiento += "\n";
		procedimiento += "Grado: ";
		procedimiento += gradoPolinomio(coeficientes);
		procedimiento += "\n";
		procedimiento += "Puntos equidistantes: ";
		if(puntosEquidistantes(xs)) {
			procedimiento += "Si";
		}
		else {
			procedimiento +="No";
		}
		
		return procedimiento;
		
	}
	
	public String metodoProgresivo(ArrayList<Point2D.Double> puntos) {
		String procedimiento = "";
		
		xs = new double[puntos.size()];
		double[][] y = new double[puntos.size()][puntos.size()];
		
		for(int i = 0; i < puntos.size(); i++) {			
			xs[i] = puntos.get(i).getX();
			y[i][0] = puntos.get(i).getY();			
		}
		
		///////////////////// CALCULO LOS COEFICIENTES ///////////////////////////////
		int desplazamiento = 0;
		for(int i = 1; i < puntos.size(); i++) {
			for(int j = 0; j < puntos.size() - i; j++) {
				y[j][i] = (y[j+1][i-1] - y[j][i-1]) / (xs[i+desplazamiento] - xs[j]);
				desplazamiento++;
			}
			desplazamiento = 0;
		}
		
		for(int i = 0; i < coeficientes.length; i++) {
			coeficientes[i] = y[0][i];			
		}
		
		/////////////////// MUESTRO PROCEDIMIENTOS EN STRING /////////////////
		procedimiento += "X\tY\n";		
		for(int i= 0; i < puntos.size(); i++) {
			
			procedimiento +=  xs[i] + "\t";
			for(int j = 0; j < puntos.size() - i; j++) {
				procedimiento += y[i][j] + "\t";				
			}
			procedimiento += "\n";
		}
		
		procedimiento += "\n";
		procedimiento += "\n";
		procedimiento += escribirPolinomioProgresivo(coeficientes, xs);
		procedimiento += "\n";
		procedimiento += "Grado: " + gradoPolinomio(coeficientes) + "\n";
		procedimiento += "Puntos equidistantes: ";
		if(puntosEquidistantes(xs)) {
			procedimiento += "Si";
		}
		else {
			procedimiento +="No";
		}
		
		return procedimiento;
		
}
	
	public String metodoRegresivo(ArrayList<Point2D.Double> puntos) {
		
		String procedimiento = "";
		
		xs = new double[puntos.size()];
		double[][] y = new double[puntos.size()][puntos.size()];
		
		for(int i = 0; i < puntos.size(); i++) {			
			xs[i] = puntos.get(i).getX();
			y[i][0] = puntos.get(i).getY();			
		}
		
		///////////////// CALCULO LOS COEFICIENTES //////////////////
		int desplazamiento = 1;
		for(int i = 1; i < puntos.size(); i++) {
			for(int j = puntos.size()-1; j >= i; j--) {
				
				y[j][i] = (y[j][i-1] -y[j-1][i-1]) / (xs[j] - xs[j-desplazamiento]);
				
			}
			desplazamiento++;
		}
		
		for(int i = 0; i < coeficientes.length; i++) {
			coeficientes[i] = y[coeficientes.length-1][i];
		}
		
		//////////////// MUESTRO PROCEDIMIENTOS EN STRING ///////////////////////////
		procedimiento += "X\tY\n";
		for(int i = 0; i < puntos.size(); i++) {
			procedimiento += xs[i] + "\t";
			for(int j = 0; j <= i; j++) {				
				procedimiento += y[i][j] + "\t";				
			}
			procedimiento += "\n";
		}
		procedimiento += "\n";
		procedimiento += "\n";
		procedimiento += escribirPolinomioRegresivo(coeficientes, xs);
		procedimiento += "\n";
		procedimiento += "Grado: " + gradoPolinomio(coeficientes) + "\n";
		procedimiento += "Puntos equidistantes: ";
		if(puntosEquidistantes(xs)) {
			procedimiento += "Si";
		}
		else {
			procedimiento +="No";
		}
		
		return procedimiento;
		
	}

	
	private String escribirPolinomioProgresivo(double[] coef, double[] xs) {
		
		String polinomio = "P(X) = ";
		
		for(int i = 0; i < coef.length; i++) {
			
			if(coef[i] != 0) {
				if(coef[i] > 0) {
					if(i > 0) {
						if(anterioresSonCeros(coef, i)) {
							polinomio += coef[i] + " ";
						}
						else {
							polinomio += "+" + Math.abs(coef[i]) + " ";
						}
					}
					else {
						polinomio += coef[i] + " ";
					}
				}
				else {
					polinomio += coef[i] + " ";
				}
				
				for(int j = 0; j < i; j++) {
					if(xs[j] == 0) {
						polinomio += "X ";
					}
					else {
						if(xs[j]>0) {
							polinomio += "(X-" + xs[j] + ") ";
						}
						else {
							polinomio += "(X+" + Math.abs(xs[j]) + ") ";
						}
					}
				}
				polinomio += " ";
			}
			
		}
		
		return polinomio;
		
	}
	
	private String escribirPolinomioRegresivo(double[] coef, double[] xs) {
		
		String polinomio = "P(X) = ";
		
		for(int i = 0; i < coef.length ; i++) {
			
			if(coef[i] != 0) {
				if(coef[i] > 0) {
					if(i > 0) {
						if(anterioresSonCeros(coef, i)) {
							polinomio += coef[i] + " ";
						}
						else {
							polinomio += "+" + Math.abs(coef[i]) + " ";
						}
					}
					else {
						polinomio += coef[i] + " ";
					}
				}
				else {
					polinomio += coef[i] + " ";
				}
				
				for(int j = 0; j < i; j++) {
					if(xs[xs.length -1 -j] == 0) {
						polinomio += "X ";
					}
					else {
						if(xs[j]>0) {
							polinomio += "(X-" + xs[xs.length -1 -j] + ") ";
						}
						else {
							polinomio += "(X+" + Math.abs(xs[xs.length -1 -j]) + ") ";
						}
					}
				}
				polinomio += " ";
			}
			
		}
		
		return polinomio;
		
	}
	
	private int gradoPolinomio(double[] coef) {
		
		int grado = 0;
		
		for(int i = 0; i < coef.length; i++) {
			if(coef[i]!= 0) {
				grado = i;
			}
		}
		
		return grado;
		
	}
	
	private boolean puntosEquidistantes(double[] coef) {
		
		if(coef.length == 1) {
			return true;
		}
		else {
			
			boolean res = true;
			double distancia = 0;
			
			for(int i = 0; i < coef.length - 1; i++) {
				
				if(i == 0) {
					distancia = Math.abs(coef[i] - coef[i+1]);
				}
				else {
					if(!(distancia == Math.abs(coef[i] - coef[i+1]))){
						return false;
					}
				}
				
			}
			
			return res;
			
		}
		
	}
	
	private boolean anterioresSonCeros(double[] coef, int j) {
		boolean res = true;
		
		for(int i = 0; i < j; i++) {
			if(coef[i] != 0) {
				return false;
			}
		}
		
		return res;
				
	}
	
	private String escribirLi(double mult, int l) {
		
		String res = "L" + l + "(x) = " + 1/mult + " ";
		for(int i = 0; i < xs.length; i++) {
			if(i != l) {
				res += "(X-" + xs[i] + ")";
			}
		}
		res += "\n\n";
		return res;
		
	}
	
	private String escribirPolinomioLagrange() {
		
		String res = "P(X) = ";
		
		for(int i = 0; i < lagrange.length; i++) {
			
			if(i != 0) {
				if(ys[i]/lagrange[i] > 0) {
					res += " +";
				}
				else res += " ";
				res += ys[i]/lagrange[i] + " ";
				
				for(int j = 0; j < lagrange.length; j++) {
					if(j != i) {
						res += "(X-" + xs[j] + ") ";
					}
				}				
			}
			else {
				res += ys[i]/lagrange[i] + " ";
				for(int j = 0; j < lagrange.length; j++) {
					if(j != i) {
						res += "(X-" + xs[j] + ") ";
					}
				}	
				
			}

		}
		
		return res;
		
	}
	
}