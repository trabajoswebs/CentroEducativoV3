/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Acer
 */
public class Alumno extends Persona{
    
    public Alumno(){
        this.tmAsignaturasAlumno = new TreeMap<String, Notas>();
    }

    public Alumno(String curso, String nombre, String apellidos, String calle, String codigoPostal, String ciudad, String dni, String fechaNacimiento) {
        super(nombre, apellidos, calle, codigoPostal, ciudad, dni, fechaNacimiento);
        this.curso = curso;
        this.tmAsignaturasAlumno = new TreeMap<String, Notas>();
    }
    
    private String curso;//comprobar su existencia en Treemaps tmCursos
//Código de lasAsignaturas de las que está matriculado cada alumno  en key 
// y un array int con las notas de las cinco evaluaciones  
    private TreeMap<String, Notas> tmAsignaturasAlumno;

        
    @Override
    public void pideDatos() {
        super.pideDatos();
        Scanner sc = new Scanner(System.in);
        boolean correcto = true;
        String cur;

        do {
            try {
                System.out.println("Introducir el curso: ");
                cur = sc.nextLine().toUpperCase();
                if (CentroEducativoV3.tmCC.containsKey(cur)) {
                    this.curso = cur;
                    correcto = true;
                } else {
                    throw new Exception("El curso indicado no es correcto. ");
                }

                String repetir = "N";
                String asignatura, siglaCurso;
                do {
                    try {
                        System.out.println("Introducir la asignatura: ");
                        asignatura = sc.nextLine().toUpperCase();

                        if (CentroEducativoV3.tmCCASIGNA.containsKey(asignatura)) {

                          if (this.tmAsignaturasAlumno.containsKey(asignatura)) // Comprueba si la asignatura ya ha estado grabada en la lista del alumno
                            throw new Exception("La asignatura ya se encuentra en la lista.");

                            siglaCurso = asignatura.substring(0, 2);

                            if (! this.curso.equalsIgnoreCase(siglaCurso)) {
                                throw new Exception("La asignatura no coincide con el curso.");
                            }
                            Notas notas = new Notas();
                            this.tmAsignaturasAlumno.put(asignatura, notas);
                            
                            System.out.println("Para continuar agregando asinaturas pulse la tecla: 'S'");
                            repetir = sc.nextLine();
                        } else {
                            throw new Exception("El código de la asignatura no es correcto. ");
                        }
                    } catch (Exception e) {
                        repetir = "S";
                        System.out.println("Error: " + e.getMessage());
                    }
                } while (repetir.equalsIgnoreCase("S"));
            } catch (Exception e) {
                correcto = false;
                System.out.println("Error: " + e.getMessage());
            }

        } while (! correcto);
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * @return the tmAsignaturasAlumno
     */
    public TreeMap<String, Notas> getTmAsignaturasAlumno() {
        return tmAsignaturasAlumno;
    }

    /**
     * @param tmAsignaturasAlumno the tmAsignaturasAlumno to set
     */
    public void setTmAsignaturasAlumno(TreeMap<String, Notas> tmAsignaturasAlumno) {
        this.tmAsignaturasAlumno = tmAsignaturasAlumno;
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        cadena.append(super.toString());
        cadena.append("\nCurso: ");
        cadena.append(this.curso);
        Iterator it = this.tmAsignaturasAlumno.keySet().iterator();
        cadena.append("\nAsignaturas: ");
        String key;
        while (it.hasNext()) {
            key = (String) it.next();
            cadena.append("\n" + key);
        }
        return cadena.toString();
    }
    
    /**
     * Imprime las notas de las asignaturas que cursa el alumno
     * @param curso
     * @param evaluacion
     * @return 
     * @throws java.lang.Exception 
     */
    String boletinNotas(String curso, int evaluacion) throws Exception {
        String[] eval = {"Primera Evaluación", "Segunda Evaluación", "Tercera Evaluación", "Evaluación Ordinaria", "Evaluación Extraordinaria"};
        StringBuilder cadena = new StringBuilder();

        if (CentroEducativoV3.tmCC.containsKey(curso.toUpperCase())) { //Se comprueba que el codigo del curse este en la lista de codigos
            if (evaluacion >= 1 && evaluacion <= 5) {  //Se comprueba que la evaluacion de las notas se comprendan entre 0 y 5

                if (this.tmAsignaturasAlumno.firstKey().substring(0, 2).equals(curso.toUpperCase())) {
                    cadena.append("\nBoletín de notas del alumno/a ");
                    cadena.append(this.getApellidos() + ", " + this.getNombre() + ": ");
                }

                Iterator it = this.tmAsignaturasAlumno.keySet().iterator();
                String key, cod;
                int calificacion;
                Notas notas = new Notas();

                while (it.hasNext()) {
                    key = (String) it.next();
                    cod = key.substring(0, 2);
                    if (cod.equalsIgnoreCase(curso)) { //Comparo si las dos letras del codigo del curso son iguales
                        notas = this.tmAsignaturasAlumno.get(key);
                        calificacion = notas.getNotas()[evaluacion - 1];
                        cadena.append("\nLa asignatura ");
                        cadena.append(CentroEducativoV3.tmCCASIGNA.get(key));
                        cadena.append(" (");
                        cadena.append(key);
                        cadena.append(") tiene una calificación en la " + eval[evaluacion - 1] + " de: ");
                        cadena.append(calificacion);
                        cadena.append(" puntos.");
                    }
                }
            } else {
                throw new Exception("El número de evaluaciones es incorrecto. Indique de 1 a 5");
            }
        } else {
            throw new Exception("El nombre del curso no existe. ");
        }

        return cadena.toString();
    }

}
