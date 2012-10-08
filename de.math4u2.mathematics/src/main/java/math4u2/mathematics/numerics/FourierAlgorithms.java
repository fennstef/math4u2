package math4u2.mathematics.numerics;

import math4u2.mathematics.functions.MathException;

public class FourierAlgorithms {
	
	/*
	 * Fuehrt fuer das Array data die FT durch.
	 * Die Laenge von data muss eine Potenz von 2 sein.
	 * vz == 1: Transformation
	 * vz = -1: Rueck-Transfomation.
	 */
	
	public static double [] ff(double[] data, int vz) throws MathException {
		
		if( ! isBinPowGT4(data.length)) {
			throw new MathException("Anzahl der Datenpunkte bei FT ist keine Potenz von 2");
		}
		
		if ( vz != 1 && vz != -1) {
			throw new MathException("VZ bei FFT hat nicht den Wert 1 oder -1");
		}
		
		int nn = data.length/2;		
		int n = nn << 1;
				
		//		 bit-reversal-ordering
		int j = 1;
		for ( int i = 1; i < n; i += 2 ) {
			if ( j > i ) {
				double temp = data[j-1];
				data[j-1] = data[i-1];
				data[i-1] = temp;
				temp = data[j];
				data[j] = data[i];
				data[i] = temp;		
			}
			int m = n >> 1;
			while( m >= 2 && j > m ){
				j -= m;
				m >>= 1;
			}
			j += m;
		}
		
		//		 complex transformation
		
		int mmax = 2;
		while ( n > mmax ){
			int istep = mmax << 1;
			double theta = vz*2*Math.PI/mmax;
			double wtemp = Math.sin(0.5*theta);
			double wpr = -2.0*wtemp*wtemp;
			double wpi = Math.sin(theta);
			double wr = 1.0;
			double wi = 0.0;
			for ( int m = 1; m < mmax; m +=2 ){
				for ( int i = m ; i < n ; i += istep ){
					j = i + mmax;
					double tempr = wr*data[j-1]-wi*data[j];
					double tempi = wr*data[j]+wi*data[j-1];
					data[j-1]= data[i-1]-tempr;
					data[j] = data[i]-tempi;
					data[i-1] += tempr;
					data[i] += tempi;
				}
				wtemp = wr;
				wr = wr*wpr-wi*wpi+wr;
				wi = wi*wpr+wtemp*wpi+wi;
			}
			mmax = istep;
		}		
		
		return data;
	} //ff
	
	
	// Fourier-Trafo
	public static double [] fft(double [] values)throws MathException{
		
		return ff(values, +1);
	}
	
	// Inverse Fourier-Trafo
	public static double [] ffi(double [] values)throws MathException{
		return ff(values, -1);
	}
	
	/* 
	 * Fourier-Trafo Cos- und Sin-Amplituden
	 * 
	 * values[] ist ein Array, dessen Lanege eine Zweierpotenz ist, 
	 * z.B. 32. Es enthaelt die reellen Daten, die transformiert werden sollen.
	 * Erstellt fuer values[] der Laenge 32 ein Array mit zwei Spalten
	 * und 17 ( = 32/2 +1 ) Zeilen.
	 * In der ersten Spalte stehen die cos-Amplituden fuer aufsteigende Frequenzen, 
	 * in der zweiten Spalte die sin-Amplituden.
	 * Analog statt fuer 32 fuer andere Laengen, z.B. 64, 128, ...
	 */
	public static double[][] ffcs(double [] rvalues)throws MathException {
		int rn = rvalues.length;

		double[] values = new double[2*rn];
		for ( int i = 0; i < rn; i ++){
			values[2*i] = rvalues[i];  // real part
			values[2*i+1] = 0;      // imaginary part
		}	
		
		double [] data = fft(values);
		
		int nn = data.length/4;
		double [][] cossin = new double[nn+1][2];
		for( int i = 0; i <= nn; i++) {
			cossin[i][0]= data[2*i];
			cossin[i][1]= data[2*i+1];
		}		
		return cossin;
	}
	
	/*
	 * Inverse Transformation aus Cos- und Sin-Amplituden
	 * Das Array cos[] der Laenge 16 + 1 = 17 enthaelt die cos-Amplituden,
	 * das Array sin[] der gleichen Laenge 17 enthaelt die sin-Amplituden.
	 * Errechnet aus diesen Amplituden das ruecktransformierte Daten-Array
	 * der Laenge 2*16 = 32.
	 * Analog statt fuer 16 für andere Zweierpotenzen wie 64, 128, ...
	 */
	public static double[] fics(double[] cos, double[] sin) throws MathException {
		int n = cos.length - 1;
		int nn = 4*n;
		double[] data = new double[nn];
		for ( int i = 0; i <= n; i++){
			data[2*i]  = cos[i];
			data[2*i+1]= sin[i];
		}
		for ( int i = 1; i < n; i++){
			data[nn-2*i]= data[2*i];
			data[nn-2*i+1] = -data[2*i+1];
		}
				
		data = ffi(data);
		
		int nnn = 2*n;
		double[] rdata = new double[nnn];
		for ( int i = 0; i < 2*n; i++){
			rdata[i] = data[2*i]/nnn;
		}
		
		return rdata;
	}
	
	/* Fourier-Trafo Amplituden und Phasen
	 * values[] ist ein Array, dessen Lanege eine Zweierpotenz ist, 
	 * z.B. 32.
	 * Erstellt fuer values[] der Laenge 32 ein Array mit zwei Spalten
	 * und 17 ( = 32/2 +1 ) Zeilen.
	 * In der ersten Spalte stehen die Gesamt-Amplituden fuer aufsteigende Frequenzen, 
	 * in der zweiten Spalte die zugehörigen Phasen.
	 * Analog statt fuer 32 fuer andere Laengen, z.B. 64, 128, ...
	 */
	public static double[][] ffap(double [] rvalues)throws MathException {
		int rn = rvalues.length;

		double[] values = new double[2*rn];
		for ( int i = 0; i < rn; i ++){
			values[2*i] = rvalues[i];  // real part
			values[2*i+1] = 0;      // imaginary part
		}	
			
		double [] data = fft(values);
		int nn = data.length/4;
		double [][] ampphas = new double[nn+1][2];
		
		double maxAmp = 0;

		for (int i = 0; i <= nn; i++) {			
			double amp = Math.sqrt(    data[2*i] * data[2*i]																		
									 + data[2*i+1] * data[2*i+1]	 );
			ampphas[i][0] = amp;
			if( amp > maxAmp) maxAmp = amp;	
		}		
		if ( maxAmp == 0 ) maxAmp = 1;
		
		// Phasen
		for (int i = 0; i <= nn; i++) {
		    if( ampphas[i][0]/ maxAmp < 1.0E-10 ) 
		    	 ampphas[i][1] = 0;
			else 
				ampphas[i][1] = Math.atan2(    data[2*i +1] , data[2*i]);
		}			
		return ampphas;
	}
	
	/*
	 * Inverse Transformation aus Amplituden und Phasen
	 * Das Array amp[] der Laenge 16 + 1 = 17 enthaelt die Amplituden,
	 * das Array phas[] der gleichen Laenge 17 enthaelt die Phasen.
	 * Errechnet aus diesen Amplituden das ruecktransformierte Daten-Array
	 * der Laenge 2*16 = 32.
	 * Analog statt fuer 16 für andere Zweierpotenzen wie 64, 128, ...
	 */
	public static double[] fiap(double[] amp, double[] phas) throws MathException {
		int n = amp.length - 1;
		int nn = 4*n;
		double[] data = new double[nn];
		for ( int i = 0; i <= n; i++){
			data[2*i]  = amp[i]*Math.cos(phas[i]);
			data[2*i+1]= amp[i]*Math.sin(phas[i]);;
		}
		for ( int i = 1; i < n; i++){
			data[nn-2*i]= data[2*i];
			data[nn-2*i+1] = -data[2*i+1];
		}
			
		data = ffi(data);
		
		int nnn = 2*n;
		double[] rdata = new double[nnn];
		for ( int i = 0; i < 2*n; i++){
			rdata[i] = data[2*i]/nnn;
		}		
		return rdata;
	}
	
	
	/*
	 * Ermittlet, ob num eine Zweierpotenz >= 4 ist.
	 */
	private static boolean isBinPowGT4(int num ){
		if ( num < 4) return false;
		while ( true ) {
			if ( num % 2 == 1){
				if ( num / 2 == 0) return true;
				else return false;
			}
			num = num / 2; ;
		}
	}
	

}
