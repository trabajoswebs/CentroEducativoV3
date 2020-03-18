/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;


import static java.lang.Integer.parseInt;
import java.util.Scanner;

/**
 *
 * @author Acer
 */
public class Persona {

    public String getNombre() {
        return nombre.toLowerCase();
    }

    public String getApellidos() {
        return apellidos.toLowerCase();
    }

    public String getCalle() {
        return calle;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDni() {
        return dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public Persona(){}

    public Persona(String nombre, String apellidos, String calle, String codigoPostal, String ciudad, String dni, String fechaNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.calle = calle;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    private String nombre;
    private String apellidos;
    private String calle;
    private String codigoPostal;
    private String ciudad;
    private String dni;
    private String fechaNacimiento;//DD/MM/AAAA
    
    
    public void pideDatos(){
        Scanner sc = new Scanner(System.in);
        boolean correcto = true;
        do {
            System.out.println("Introduzca su Nombre: ");
            this.nombre = sc.nextLine();
            System.out.println("Introduzca su Apellido: ");
            this.apellidos = sc.nextLine();
            System.out.println("Introduzca el nombre de la calle: ");
            this.calle = sc.nextLine();
            System.out.println("Introduzca el código postal: ");
            this.codigoPostal = sc.nextLine();
            System.out.println("Introduzca el nombre de la ciudad: ");
            this.ciudad = sc.nextLine();
            System.out.println("Introduzca el dni: ");
            this.dni = sc.nextLine();
            System.out.println("Introduzca su fecha de nacimiento: dd/mm/aaaa ");
            this.fechaNacimiento = sc.nextLine();
           
            try {
                validarDni(dni);
                verificaFecha(fechaNacimiento);
                correcto = true;
            } catch (Exception e) {
                correcto = false;
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        } while (!correcto);

    }
    
    
    public static void validarDni(String dni) throws Exception {
        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        String numeros = dni.substring(0, dni.length() - 1);
        int numerosDni = 0;
        char letra = (char) dni.toUpperCase().charAt(dni.length() - 1); // Aseguramos que todas las letras queden en mayusculas y extraemos la ultima letra del dni

        if (numeros.length() != 8) {
            throw new Exception("la longuitud del DNI debe de ser de ocho caracteres numéricos.");
        }

        try {
            numerosDni = parseInt(numeros);
        } catch (NumberFormatException e) {
            throw new Exception("Debe introducir ocho caracteres numéricos al principio.");
        }
        int resto = numerosDni % 23;

        if (letras[resto] != letra) {
            throw new Exception("La letra " + letra + " del dni no es la correcta.");
        }
    }
   
    public static void verificaFecha(String s) throws Exception {
        if (s.length() != 10) {
            throw new Exception("El fecha debe contener un total de 10 caracteres.");
        }
        if ((s.charAt(2) != '/') || (s.charAt(5) != '/')) {
            throw new Exception("El formato fecha debe ser DD/MM/AAAA.");
        }
        char letra;
        for (int i = 0; i < s.length(); i++) {
            letra = s.charAt(i);
            if (letra != '/') {
                if (letra < '0' && letra > '9') {
                    throw new Exception("Debe introducir caracteres numéricos.");
                }
            }
        }
        String sDia,sMes,sAnio, sFecha;
        sDia = s.substring(0, 2);
        sMes = s.substring(3, 5);
        sAnio = s.substring(6, 10);
        
        int dia, mes, anio; 
        
        try{                       
            dia= Integer.parseInt(sDia);
            mes= Integer.parseInt(sMes);
            anio= Integer.parseInt(sAnio);
        }catch(NumberFormatException e){
            throw new Exception("No se ha podido convertir la cadena String a número.");
        }
        String mensajeExcepcion = "";
        boolean correcto = true;
 
        switch (mes) {
            case 1: case 3:case 5: case 7: case 8:case 10:case 12:
                if (dia < 1 || dia > 31) {
                    correcto = false;
                    mensajeExcepcion = "El día de nacimiento no es correcto.";
                }
                break;
            case 2:
                int max = 28;
                if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
                    max = 29;
                }
                if (dia < 1 || dia > max) {
                    correcto = false;
                    mensajeExcepcion = "El año de nacimiento no es bisiestro, por lo tanto, febrero solo dispone de 28 días.";
                }

                break;
            case 4: case 6: case 9: case 11:
                if (dia < 1 || dia > 30) {
                    correcto = false;
                    mensajeExcepcion = "El día de nacimiento no es correcto. El mes solo corresponde a 30 días";
                }
                break;
            default:
                correcto = false;
                mensajeExcepcion = "Error en la fecha de nacimiento.";
        }
        
        if (! correcto) throw new Exception(mensajeExcepcion);
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\nNombre: ");
        cadena.append(this.nombre);
        cadena.append("\nApellido: ");
        cadena.append(this.apellidos);
        cadena.append("\nDni: ");
        cadena.append(this.dni);
        cadena.append("\nFecha de Nacimiento: ");
        cadena.append(this.fechaNacimiento);
        cadena.append("\nDomicilio: ");
        cadena.append(this.calle);
        cadena.append("\nCiudad: ");
        cadena.append(this.ciudad);
        cadena.append("\nCódigo Postal: ");
        cadena.append(this.codigoPostal);
        return cadena.toString();
    }
    
}

