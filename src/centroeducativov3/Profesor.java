/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;


import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Acer
 */
public class Profesor extends Persona{

    private double sueldoBase;
    private int[] horasExtras;
    private double tipoIRPF;
    private String cuentaIBAN;

    
    private TreeMap<String, String> tmAsignaturas;//asignaturas que imparte

    public TreeMap<String, String> getTmAsignaturas() {
        return tmAsignaturas;
    }

    public void setTmAsignaturas(TreeMap<String, String> tmAsignaturas) {
        this.tmAsignaturas = tmAsignaturas;
    }

    public Profesor() {this.horasExtras = new int[12];}
    
    
    public Profesor(double sueldoBase, double tipoIRPF, String cuentaIBAN, String nombre, String apellidos, String calle, String codigoPostal, String ciudad, String dni, String fechaNacimiento) {
        super(nombre, apellidos, calle, codigoPostal, ciudad, dni, fechaNacimiento);
        this.sueldoBase = sueldoBase;
        this.horasExtras = new int[12];
        this.tipoIRPF = tipoIRPF;
        this.cuentaIBAN = cuentaIBAN;
        this.tmAsignaturas = new TreeMap<String, String>();
    }

    public void nuevoProfesor() throws Exception {
        Scanner sc = new Scanner(System.in);
        //DATOS DEL NUEVO PROFESOR
        super.pideDatos();
        String sueldo, irpf;
        boolean correcto = false;
        
        do {            
            System.out.print("Número de cuenta bancaria IBAN: ");
            this.cuentaIBAN = sc.nextLine();
            System.out.print("Sueldo base: ");
            sueldo = sc.nextLine();
            System.out.print("Tipo de IRPF: ");
            irpf = sc.nextLine();

            try {             
                Cuenta.filtroCuenta(cuentaIBAN);
                try {
                    if (sueldo.contains(",")) {
                        sueldo = sueldo.replace(",", ".");
                    }
                    this.sueldoBase = Double.parseDouble(sueldo);
                } catch (Exception e) {
                    throw new Exception("Error en el sueldo base.");
                }
                try {
                    if (irpf.contains(",")) {
                        irpf = irpf.replace(",", ".");
                    }
                    this.tipoIRPF = Double.parseDouble(irpf);
                    correcto = true;
                } catch (Exception e) {
                    throw new Exception("Error en el tipo irpf.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine();
            }
            
            this.horasExtras = new int[12];
            this.tmAsignaturas = new TreeMap<String, String>();

        } while (!correcto);

    }
    
    /**
     * Agrega ,borra o muestra las asignaturas que imparte un profesor
     */
    
    public void asignaturasProfesor() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Indica la asignaruta que va a impartir: ");
        String key = "";
        System.out.println("Profesor: " + this.getApellidos().toLowerCase()+ ", " + this.getNombre().toLowerCase());
        int option = 0;
        do {
            System.out.println("");

            if (this.tmAsignaturas.isEmpty() == false) {
                Iterator it = this.tmAsignaturas.keySet().iterator();
                System.out.println("Asignaturas impartidas: ");
                while (it.hasNext()) {
                    key = (String) it.next();
                    String nombre = this.tmAsignaturas.get(key);
                    System.out.println(key + ": " + nombre);
                }
            }
            System.out.println("");
            System.out.println("1. Añadir Asignatura.");
            System.out.println("2. Eliminar Asignatura.");
            System.out.println("0. Salir.");
            boolean correcto = false;

            do {
                try {
                    System.out.println("Opción seleccionada: ");
                    option = sc.nextInt();
                    correcto = true;
                } catch (InputMismatchException e) {
                    System.out.println("Opción no admitida");
                    sc.nextLine(); // Limpia el buffer
                } catch (Exception e) {
                    System.out.println("Ha ocurrido una excepcion: " + e.getMessage());
                }
            } while (!correcto);
            switch (option) {
                case 1:
                    System.out.println("");
                    sc.nextLine();
                    System.out.println("Indique el código de la asignatura: ");
                    key = sc.nextLine().toUpperCase();
                    if (CentroEducativoV3.tmCCASIGNA.containsKey(key)) {
                        String nombre = CentroEducativoV3.tmCCASIGNA.get(key);
                        System.out.println(key + ": " + nombre);
                        this.tmAsignaturas.put(key, nombre);
                    }
                    System.out.println("Asignatura añadida.");
                    break;
                case 2:
                    System.out.println("");
                    sc.nextLine();
                    System.out.println("Indique el código de la asignatura que desea eliminar: ");
                    key = sc.nextLine().toUpperCase();
                    if (this.tmAsignaturas.containsKey(key)) {
                        this.tmAsignaturas.remove(key);
                    }
                    System.out.println("Asignatura eliminada.");
                    break;
            }

        } while (option != 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nCuenta IBAN: ");
        sb.append(this.cuentaIBAN);
        String EEEE = cuentaIBAN.substring(0,4);
        String banco = CentroEducativoV3.tmEEEE.get(EEEE);
        String EEEESSSS = cuentaIBAN.substring(4,12);
        String sucursal = CentroEducativoV3.tmEEEESSSS.get(EEEESSSS);
        System.out.println("\nBanco " + banco + " Sucursal " + sucursal);
        sb.append("\nSueldo Base: ");
        sb.append(this.sueldoBase);
        sb.append("\nTipo de IRPF: ");
        sb.append(this.tipoIRPF);
        return sb.toString();
        
    }
    /*
    Imprime las asignaturas de un profesor
    */
    public String imprimeAsignaturas(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nProfesor: " + this.getNombre() + ", " + this.getApellidos());
        sb.append("\nAsignaturas: ");
        Iterator it = this.tmAsignaturas.keySet().iterator();
        String key;
        while(it.hasNext()){
            key = (String) it.next();
            String asig = this.tmAsignaturas.get(key);
            sb.append("\n" +key + ": " + asig);
        }
        return sb.toString();
    }
    
    /**     
     * @return 
     */
    public String getCuentaIBAN() {
        return cuentaIBAN;
    }
    
    /**     
     * @param cuentaIBAN 
     */
    public void setCuentaIBAN(String cuentaIBAN) {
        this.cuentaIBAN = cuentaIBAN;
    }
  
    /**
     * @return the sueldoBase
     */
    public double getSueldoBase() {
        return sueldoBase;
    }

    /**
     * @param sueldoBase the sueldoBase to set
     */
    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    /**
     * @param m
     * @return the horasExtra
     */
    public int getHorasExtra(int m) {
        return horasExtras[m];
    }

    public void setHorasExtra(int i, int horasExtra) {
        this.horasExtras[i] = horasExtra;
    }

    /**
     * @return the tipoIRPF
     */
    public double getTipoIRPF() {
        return tipoIRPF;
    }

    /**
     * @param tipoIRPF the tipoIRPF to set
     */
    public void setTipoIRPF(double tipoIRPF) {
        this.tipoIRPF = tipoIRPF;
    }

    //Calculo del importe de las horas extras por mes    
    private double calcularImporteHorasExtras(int mes) {
        return this.horasExtras[mes-1] * CentroEducativoV3.getPagoPorHoraExtra();
    }

    //Método para calcular el sueldo bruto de un mes (sueldo base +
    //complemento por horas extras) calcularSueldoBruto() 
    private double calcularSueldoBruto(int mes) {
        return this.sueldoBase + this.calcularImporteHorasExtras(mes);
    }

    //Método para calcular las retenciones por IRPF de un mes. El porcentaje
    //de IRPF se aplica sobre el sueldo bruto calcularRetencionIrpf(mes). 
    private double calcularRetencionIrpf(int mes) {
        return (calcularSueldoBruto(mes) * this.tipoIRPF) / 100;
    }

    //Método para calcular el sueldo (sueldo bruto - retenciones)
    private double calcularSueldo(int mes) {
        return calcularSueldoBruto(mes) - calcularRetencionIrpf(mes);
    }

    //Método ImprimeProfesor() que se le llama desde el main mediante una
    //instancia de la clase Profesor y escribe sus datos personales:    
    public String ImprimeProfesor() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\nNombre: ");
        cadena.append(this.getNombre());
        cadena.append("\nApellido: ");
        cadena.append(this.getApellidos());
        cadena.append("\nDni: ");
        cadena.append(this.getDni());
        cadena.append("\nFecha de Nacimiento: ");
        cadena.append(this.getFechaNacimiento());
        cadena.append("\nDomicilio: ");
        cadena.append(this.getCalle());
        cadena.append("\nCiudad: ");
        cadena.append(this.getCiudad());
        cadena.append("\nCódigo Postal: ");
        cadena.append(this.getCodigoPostal());
        return cadena.toString();
    }

    //Método leer profesores
    public void leerProfesores(Scanner sc) throws Exception {
        this.nuevoProfesor();
    }

    //Método ImprimirNominas(int mes) que permite seleccionar un mes e
    //imprime una nómina de la instancia que le llama en ese mes
    public String ImprimirNominas(int mes) {
        StringBuilder sb = new StringBuilder();
         sb.append("\n\tMES: " );
        sb.append(conocerMes(mes).toUpperCase());
        sb.append("\nNombre: ");
        sb.append(this.getNombre() + ", " + this.getApellidos());
        sb.append("\nDNI: ");
        sb.append(this.getDni());
        sb.append("\nCurso: ");
        sb.append(CentroEducativoV3.getCurso());
        sb.append("\nNomina mes: ");
        sb.append(conocerMes(mes));
        sb.append("\nSueldo base: ");
        sb.append(this.getSueldoBase());
        sb.append("\nHoras Extra: ");
        sb.append(this.getHorasExtra(mes-1));
        sb.append("\nSueldo Bruto: ");
        sb.append(this.calcularSueldoBruto(mes));
        sb.append("\nRetención IRPF: ");
        sb.append(this.calcularRetencionIrpf(mes));
        sb.append("\nSueldo: ");
        sb.append(this.calcularSueldo(mes));
        return sb.toString();
    }

    private String conocerMes(int mes) {
        String[] meses = {"Enero", "Febrero", "marzo", "Abril", "mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes - 1];
    }

}
