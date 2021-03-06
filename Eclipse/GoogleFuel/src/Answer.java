package com.google.challenges;
import java.util.ArrayList;
import java.util.Stack;

public class Answer {   
    public static int[] answer(int[][] m) {
        Answer answer = new Answer();
        return answer.getAnswer(m);
    }
    
    public int[] getAnswer(int[][] m) {
        
        // Your code goes here.
		ArrayList<Integer> finalSate = new ArrayList<Integer>();
		Stack<Integer> finalSate2Switch = new Stack<Integer>();	
		int[] rowSum = new int[m.length];
		//Tracking state order in matrix.
		int[] stateOrder = new int[m.length];		
		
		for(int i=0; i<m.length; i++)
		{
			boolean isFinal = true;
			for(int j=0; j<m[i].length; j++)
			{
				rowSum[i]+=m[i][j];
    			if(m[i][j]>0)
    				isFinal = false;
			}
    		if(isFinal)
    			finalSate.add(i); 		
		}
		
		//Size of final state.
		int finalSateL= finalSate.size();
		if(finalSateL==0)
			return new int[]{0,1};		
		
		//Get probability matrix p
		Fraction[][] p= new Fraction[m.length][m[0].length];
		for(int i=0; i<m.length; i++)
		{
			//Initial state order
			stateOrder[i]=i;
			
			for(int j=0; j<m[i].length; j++)
			{
				p[i][j] = new Fraction(m[i][j],rowSum[i]);
				if(i==j && finalSate.contains(i)){
					p[i][j] = new Fraction(1,1);
					
					if(i<m.length-finalSateL)
						finalSate2Switch.push(i);
				}
			}
		}
		
		//Switch to Standard format
		for(int i= p.length-1; i>=p.length-finalSateL; i--)
		{	
			if(!finalSate.contains(i))
			{
				//Switch row i with finalSate2Switch.pop()
				int nextFinal = finalSate2Switch.pop();
				for(int j =0; j<p[0].length; j++){
		        	Fraction temp= p[i][j];
		        	p[i][j]=p[nextFinal][j];
		        	p[nextFinal][j]=temp;
				}
				//Switch column i with nextFinal column
				for(int j =0; j<p.length; j++){
		        	Fraction temp= p[j][i];
		        	p[j][i]=p[j][nextFinal];
		        	p[j][nextFinal]=temp;
				}
				
				int temp=stateOrder[i];
				stateOrder[i]=nextFinal;
				stateOrder[nextFinal]=temp;
			}
		}
		
		int[] resOrderMap = new int[finalSateL];
		for(int i=0; i<finalSateL; i++){
			int order = finalSate.indexOf(stateOrder[p.length-finalSateL+i]);
			resOrderMap[i]=order;
		}		
		
		Fraction[][] i_q= new Fraction[p.length-finalSateL][p[0].length-finalSateL];
		Fraction[][] r= new Fraction[p.length-finalSateL][finalSateL];
		for(int i=0; i<p.length-finalSateL; i++)
		{
			for(int j=0; j<p[i].length; j++)
			{
				if(j<p[i].length-finalSateL)
				{
					if(i==j)
						i_q[i][j] =new Fraction(1,1).sub(p[i][j]);
					else
						i_q[i][j] = new Fraction(0,1).sub(p[i][j]);
				}
				else
					r[i][j-p[i].length+finalSateL] = p[i][j];					
			}
		}
		
		if(i_q.length ==0 || r.length ==0)
			return convert2FinalRes(p, resOrderMap);
			
		//f=(i-q)^-1 * r
		Fraction[][] n= inverse(i_q);
		
		Fraction[][] f= new Fraction[n.length][r[0].length];
		for(int i=0; i<n.length; i++)
		{			
			for(int j=0; j<r[0].length; j++)
			{	
				Fraction temp = new Fraction(0,1);
				for(int k=0; k<r.length; k++){
					temp=temp.add(n[i][k].muti(r[k][j]));
				}
				f[i][j]=temp;			
			}
		}

		int[] result =convert2FinalRes(f, resOrderMap);   	
    	return result;
	}
	
	//Helper
	int[] convert2FinalRes(Fraction[][] f, int[] order){
		int[] result = new int[f[0].length+1];
		
    	int resultDinomi = 1;
    	for(int i= 0; i<f[0].length; i++){
    		resultDinomi=f[0][i].mcm(f[0][i].Denominator, resultDinomi);
    	}   		
		
    	for(int i=0; i<result.length; i++){
    		if(i==result.length-1)
    			result[i]=resultDinomi;
    		else{
    			result[order[i]] = f[0][i].Numerator * (resultDinomi /f[0][i].Denominator);
    		}
    	}
    	return result;
	}
    
	Fraction[][] inverse ( Fraction[][] matrix1)
	{
		int size = matrix1.length;
		Fraction[][] matrix2 = new Fraction[size][size];
		

	    for (int i=0; i<size; i++)
	    {
	        for (int j=0; j<size; j++)
	        {
	            if (i == j)
	                matrix2[i][j] = new Fraction(1,1);
	            else
	                matrix2[i][j] = new Fraction(0,1);
	        }
	    }
	    
	    boolean canInvert = true;
	    for (int i=0; i<size; i++)
	    { 	
	        int rowmaxpos = i;
	        for (int j=i+1; j<size; j++)
	        {
	            if ( Math.abs((double)matrix1[j][i].Numerator/(double)matrix1[j][i].Denominator) 
	            		> Math.abs((double)matrix1[rowmaxpos][i].Numerator/(double)matrix1[rowmaxpos][i].Denominator) )
	                rowmaxpos = j;
	        }
	        for (int j=0; j<size; j++)
	        {
	        	Fraction temp= matrix1[i][j];
	        	matrix1[i][j] = matrix1[rowmaxpos][j];
	        	matrix1[rowmaxpos][j] = temp;

	        	temp= matrix2[i][j];
	        	matrix2[i][j] = matrix2[rowmaxpos][j];
	        	matrix2[rowmaxpos][j] = temp;
	        }
	        if ( (double)matrix1[i][i].Numerator/(double)matrix1[i][i].Denominator !=0 )
	        {
	        	Fraction divisor = new Fraction(matrix1[i][i]);
	            for (int j=0; j<size; j++)
	            {
	                matrix1[i][j] = matrix1[i][j].div(divisor);
	                matrix2[i][j] = matrix2[i][j].div(divisor);
	            }
	            for (int j=0; j<size; j++)
	            {
	            	if(j!=i){
		            	Fraction multiple = new Fraction(matrix1[j][i]);
		                for ( int k=0; k<size; k++)
		                {
		                    matrix1[j][k] = matrix1[j][k].sub(matrix1[i][k].muti(multiple));
		                    matrix2[j][k] = matrix2[j][k].sub(matrix2[i][k].muti(multiple));
		                }	            		
	            	}

	            }
	        }
	        else
	        {
	        	canInvert = false;
	        	break;
	        }
	    }

	    if(canInvert)
	    	return matrix2;
	    else
	    	return new Fraction[][]{};
	}
   
    public class Fraction {  
        int Numerator; 
        int Denominator; 
          
        Fraction(){  
        }  
      
        Fraction(int a,int b){  
            if(a == 0){  
            	this.Numerator = 0;  
            	this.Denominator = 1;  
            }  
            else{  
                int c = gcd(Math.abs(a),Math.abs(b)); 
                this.Numerator = a / c;  
                this.Denominator = b / c;  
                if(this.Numerator<0 && this.Denominator<0){  
                	this.Numerator = - this.Numerator;  
                	this.Denominator = - this.Denominator;  
                }   
            }  
        }
        
        Fraction(Fraction a){  
        	this.Denominator =a.Denominator;
        	this.Numerator =a.Numerator;
        }          
        
        int gcd(int a,int b){
        	if(a==0 || b==0)
        		return 1;
        	
            if(a < b){  
                int c = a;  
                a = b;  
                b = c;  
            }  
            int r = a % b;  
            while(r != 0){  
                a = b;  
                b = r;;  
                r = a % b;  
            }  
            return b;  
        }
        
        int mcm(int m, int n) {  
            return m * (n / gcd(m, n));  
        }         
          
        Fraction add(Fraction r){ 
            int a = r.Numerator;  
            int b = r.Denominator; 
            int newDenominator = mcm(this.Denominator, b);  
            int newNumerator = this.Numerator * (newDenominator/this.Denominator) + a * (newDenominator/b);  
            Fraction result = new Fraction(newNumerator,newDenominator);  
            return result;  
        }  
          
        Fraction sub(Fraction r){ 
            int a = r.Numerator;  
            int b = r.Denominator;  
            int newDenominator = mcm(this.Denominator, b);  
            int newNumerator = this.Numerator * (newDenominator/this.Denominator) - a * (newDenominator/b);  
            Fraction result = new Fraction(newNumerator,newDenominator);  
            return result;  
        }   
          
        Fraction muti(Fraction r){ 
            int a = r.Numerator;  
            int b = r.Denominator;  
            int g1 = gcd(this.Numerator, b);
            int g2 = gcd(a, this.Denominator);
            int newNumerator = (this.Numerator / g1) * (a / g2);  
            int newDenominator = (this.Denominator / g2)  * (b / g1);  
            Fraction result = new Fraction(newNumerator,newDenominator);  
            return result;  
        }  
          
        Fraction div(Fraction r){  
            int a = r.Numerator;  
            int b = r.Denominator;  
            int g1 = gcd(this.Numerator, a);
            int g2 = gcd(b, this.Denominator);            
            int newNumerator = ( this.Numerator / g1) * (b / g2);  
            int newDenominator = (this.Denominator /g2) * (a / g1);  
            Fraction result = new Fraction(newNumerator,newDenominator);  
            return result;  
        }     
  
    }
    
}
