/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.TreeMap;

/**
 * *
 * @version 1.0.0
 * @author Johan Manuel
 */
public class Cuenta {
    
    public static final String ENTBANCARIASFILEPATH = "..\\Profesores\\EntidadesBancarias.txt";
    public static final String SUCRBANCARIASFILEPATH = "..\\Profesores\\SucursalesBancarias.txt";
    private double saldo;

    public Cuenta() {
    }

    /**
     * Número de cuenta bancaria IBAN
     */
    private String cuentaIBAN;

    /**
     * Nombre del propietario del número de cuenta
     */
    private String titular;

    /**
     * Obtiene la cuenta bancaria
     *
     * @return String datos de la cuenta bancaria
     */
    public String getCuentaIBAN() {
        return cuentaIBAN;
    }

    /**
     * Establece el codigo de la cuenta bancaria
     *
     * @param cuentaIBAN
     */
    public void setCuentaIBAN(String cuentaIBAN) {
        this.cuentaIBAN = cuentaIBAN;
    }

    /**
     * Obtiene el nombre del titular de la cuenta bancaria
     *
     * @return String titular de la cuenta
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Establece el nombre del titular de la cuenta bancaria
     *
     * @param titular
     */
    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Permite la validación de una cuenta corriente
     *
     * @param numeroCuenta
     * @throws Exception
     */
    public static void filtroCuenta(String numeroCuenta) throws Exception {
        int factores[] = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
        String entidadBancaria;

        if (numeroCuenta.contains(" ")) {
            numeroCuenta = numeroCuenta.replace(" ", ""); // Eliminamos los posibles espacios de la cadena de la cuenta
        }
        if (numeroCuenta.length() != 24) {
            throw new Exception("La longuitud de la cuenta es incorrecta.");
        }

        if (numeroCuenta.toUpperCase().charAt(0) != 'E' || numeroCuenta.toUpperCase().charAt(1) != 'S') {
            throw new Exception("El número de cuenta no pertenece a una cuenta bancaria de España");
        }

        char digito = 0;
        boolean caracteresNoNumericos = false;

        for (int i = 2; i < numeroCuenta.length(); i++) { // Comprueba que todos sus caracteres sean numéricos a partir de la tercera letra 
            digito = numeroCuenta.charAt(i);
            if (digito < '0' || digito > '9') {
                caracteresNoNumericos = true;
                break;
            }
        }

        if (caracteresNoNumericos) {
            throw new Exception("Existen carácteres no numéricos en la cuenta.");
        }

        entidadBancaria = numeroCuenta.substring(4, 8);

        int numeroEntidad = 0;

        BigInteger numero = new BigInteger(numeroCuenta.substring(4, 24) + "142800");
        int numeroControlIban = 98 - (numero.mod(new BigInteger("97")).intValue());
        String digitosControlIban = String.valueOf(numeroControlIban);

        if (numeroControlIban < 10) {
            digitosControlIban = "0" + digitosControlIban;
        }

        if (digitosControlIban.compareTo(numeroCuenta.substring(2, 4)) != 0) {
            throw new Exception("Código Iban incorrecto");
        }

        int acumulador = 0;

        for (int i = 4; i <= 11; i++) {
            acumulador = acumulador + (numeroCuenta.charAt(i) - 48) * factores[i - 2];
        }
        int digitoCalculado = 11 - (acumulador % 11);

        switch (digitoCalculado) {
            case 10:
                digitoCalculado = 1;
                break;
            case 11:
                digitoCalculado = 0;
                break;
        }

        if (digitoCalculado != ((int) numeroCuenta.charAt(12) - 48)) {
            throw new Exception("Primer dígito de control erroneo.");
        }
        acumulador = 0;
        char a;

        for (int i = 14; i <= 23; i++) {
            acumulador = acumulador + (numeroCuenta.charAt(i) - 48) * factores[i - 14];
        }
        digitoCalculado = 11 - (acumulador % 11);

        switch (digitoCalculado) {
            case 10:
                digitoCalculado = 1;
                break;
            case 11:
                digitoCalculado = 0;
                break;
        }

        if (digitoCalculado != ((int) numeroCuenta.charAt(13) - 48)) {
            throw new Exception("Segundo dígito de control erroneo.");
        }
        TreeMap<String, String> tmEEEE = new TreeMap<>();
        String banco = null;
        cargaEntidadesBancarias(tmEEEE);
        String EEEE = numeroCuenta.substring(4, 8);

        System.out.println("Entidad " + EEEE);

        if (tmEEEE.containsKey(EEEE)) {
            banco = tmEEEE.get(EEEE);
            System.out.println("banco " + banco);
        } else {
            //throw new Exception("Código bancario inexistente");
        }
        TreeMap<String, String> tmEEEESSSS = new TreeMap<>();
        String sucursal = "";
        cargaSucursalesBancarias(tmEEEESSSS);
        String EEEESSSS = numeroCuenta.substring(4, 12);
        System.out.println("Sucursal: " + numeroCuenta.substring(8, 12));

        if (tmEEEESSSS.containsKey(EEEESSSS)) {
            sucursal = tmEEEESSSS.get(EEEESSSS);
            System.out.println("Sucursal " + sucursal);
        } else {
            //throw new Exception("Código bancario inexistente");
        }
    }

    /**
     * Comprueba la validez de la entidad bancaria
     *
     * @param tmEEEE
     */
    static void cargaEntidadesBancarias(TreeMap<String, String> tmEEEE){
        cargaDatos(ENTBANCARIASFILEPATH, CentroEducativoV3.tmEEEE);
    }
    static void cargaSucursalesBancarias(TreeMap<String, String> tmEEEESSSS){
        cargaDatos(SUCRBANCARIASFILEPATH, CentroEducativoV3.tmEEEESSSS);
    }
    
    /**
     * Lee los datos que se encuentra en el fichero y los carga en el treeMap
     */
    public static void cargaDatos(String filePath, TreeMap<String, String> tm){
        FileReader fr = null;
        BufferedReader entrada = null;
        int indice = 0;
        String cadena, key, value;
        
        try {
            fr = new FileReader(filePath);
            entrada = new BufferedReader(fr);
            
            cadena = entrada.readLine();
            
            while(cadena != null){
                indice = cadena.indexOf(",");
                if (indice != -1) {
                    key = cadena.substring(0, indice).toUpperCase();
                    value = cadena.substring(indice + 1 ).toUpperCase();
                    tm.put(key, value);
                }
                cadena = entrada.readLine();
            }
        }
        catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Ha ocurrido una excepción: " + ioe.getMessage());    
        }
        catch (Exception e) {
            System.out.println("Ha ocurrido una excepción: " + e.getMessage());   
        }finally{
            try {
                if (fr != null) {
                    fr.close();
                }
                if(entrada != null){
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println("Se ha producido un error al intentar cerrar el fichero: " + e.getMessage());
            }
        }
    }


}

