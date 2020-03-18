/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;

/**
 *
 * @author Acer
 */
public class Notas {
    public int notas[];

    public void setNotas(int[] notas) {
        this.notas = notas;
    }

    public int[] getNotas() {
        return notas;
    }
    
    public Notas(){ 
        notas = new int[5];  
    }
}