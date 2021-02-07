package finter;

public class Lagrange {

	public static void main(String[] args) {
		
		  int x[]={1,2,3};
		  int y[]={3,7,13};
		  Lagrange2 l=new Lagrange2(y, x);
		  double res=l.getResultadoP(4);
		  
		  System.out.println("\nEl resultado es "+res);

	}

}

class Lagrange2 {

	private int y[];
	private int x[];
	private double a[];

	public Lagrange2(int y[], int x[]) {
		this.x = x;
		this.y = y;
		a=new double[x.length];
	}

	private void getPolinomios_a() {

		double mult;
		
		for(int i=0;i<y.length;i++){
			mult = 1;
			
			for(int j=0;j<x.length;j++){
				
				if(i==j)continue;
				mult=(x[i]-x[j])*mult;
			}
			a[i]=y[i]/mult;
			System.out.print("a"+"["+i+"]="+a[i]+"  ");
			
			
		}
	}
	
	public double getResultadoP(int p){
		getPolinomios_a();
		
		int mult=1;
		double valores[]=new double[a.length];
		//System.out.println("");
		for(int i=0;i<a.length;i++){
			mult=1;
			for(int j=0;j<a.length;j++){
				
				if(i==j)continue;
				mult=(p-x[j])*mult;
				
			}
			valores[i]=a[i]*mult;
			//System.out.print(valores[i]);
			//if(i!=a.length-1)System.out.print(" + ");
			
		}
		
		double resultado=0;
		for(int k=0;k<valores.length;k++){
			
			resultado=resultado+valores[k];
		}
		
		return resultado;
		
	}

}