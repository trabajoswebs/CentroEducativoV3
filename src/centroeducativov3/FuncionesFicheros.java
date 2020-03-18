/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author Johan Manuel
 */
public class FuncionesFicheros {
    
    /**
     * 
     * @param lista
     * @param fichero
     * @throws IOException 
     */
    public static void almacenarColPersonasEnArchivo(TreeMap<String, Persona> lista, File fichero) throws IOException{        
        PrintWriter salida = null;
        String key;
        char tipo;
        char separador = '#';
        Persona per;
        StringBuilder cadena = new StringBuilder();
        
        try {
            if (! fichero.exists()) fichero.createNewFile(); //Si el fichero no existiese se crea
            
            salida = new PrintWriter(fichero);
                    
            if (! lista.isEmpty()) { // Si el TreeMap no se encuentra vacio
                Iterator it = lista.keySet().iterator();
                
                while(it.hasNext()){
                    key = (String) it.next();
                    per = (Persona) lista.get(key);
                    
                    tipo = (per instanceof Alumno) ? 'A' : 'P'; //Conoce el tipo de objeto Alumno o Profesor
                    
                    switch(tipo){ // Cambia el tipo de objeto Alumno - Profesor
                        
                        case 'A': //Objeto Alumno
                            
                            Alumno alumno = (Alumno) per;//Cast al objeto Alumno
                            cadena.append(Character.toString(tipo) + separador);
                            cadena.append(alumno.getNombre() + separador);
                            cadena.append(alumno.getApellidos() + separador);
                            cadena.append(alumno.getCalle()+ separador);
                            cadena.append(alumno.getCodigoPostal()+ separador);
                            cadena.append(alumno.getCiudad()+ separador);
                            cadena.append(alumno.getDni()+ separador);
                            cadena.append(alumno.getFechaNacimiento()+ separador);
                            cadena.append(alumno.getCurso() + separador);
                            
                            alumno.getTmAsignaturasAlumno().entrySet().forEach((al) -> {
                                //Realizamos un bucle para obtener las asignaturas y las evaluaciones del alumno
                                String clave = al.getKey();
                                Notas valor = al.getValue();
                                cadena.append(clave + separador);
                                for (int i = 0; i < valor.getNotas().length; i++) {
                                    cadena.append(Integer.toString(valor.getNotas()[i]) + separador); //Añadimos las calificaciones de cada evaluación
                                }
                            });
                            
                            cadena.append("\n"); //Salto de linea al final de la cadena                            
                            break;
                        case 'P': //Objeto Profesor
                            Profesor profesor = (Profesor) per;//Cast al objeto Profesor
                            cadena.append(Character.toString(tipo) + separador);
                            cadena.append(profesor.getNombre() + separador);
                            cadena.append(profesor.getApellidos() + separador);
                            cadena.append(profesor.getCalle()+ separador);
                            cadena.append(profesor.getCodigoPostal()+ separador);
                            cadena.append(profesor.getCiudad()+ separador);
                            cadena.append(profesor.getDni()+ separador);
                            cadena.append(profesor.getFechaNacimiento()+ separador);
                            cadena.append(Double.toString(profesor.getSueldoBase())+ separador);
                            
                            //Total Horas extras por mes
                            for (int j = 0; j < 12; j++) { // Añadimos las horas extras de cada mes
                                String horas = (Integer.toString(profesor.getHorasExtra(j)).isEmpty()) ? "0" : Integer.toString(profesor.getHorasExtra(j));
                                cadena.append( horas + separador);
                            }
                            cadena.append(Double.toString(profesor.getTipoIRPF()) + separador);
                            cadena.append(profesor.getCuentaIBAN() + separador);
                            
                            Iterator tm = profesor.getTmAsignaturas().keySet().iterator();
                            
                            while(tm.hasNext()){
                                String codCurso = (String) tm.next();
                                String nomCurso = profesor.getTmAsignaturas().get(codCurso);
                                cadena.append(codCurso + separador);
                                cadena.append(nomCurso + separador);
                            }
                            cadena.append("\n"); //Salto de linea al final de la cadena
                        break;
                    }
                    
                }
                salida.println(cadena.toString());
                salida.flush();
            }
        } catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Ha ocurrido un error con el fichero: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error : " + e.getMessage());        
        } finally {
            if (salida != null) {
                salida.close();
            }
        }
    }

    /**
     * Carga el treeMap con los datos del fichero Persona3.txt
     * @param fichero
     * @return
     * @throws IOException 
     */
    public static TreeMap<String, Persona> obtenerTreeMapDeArchivo(File fichero) throws IOException {
        TreeMap<String, Persona> lista = new TreeMap<>();
        FileReader fr = null;
        BufferedReader entrada = null;
        String cadena, key;
        String registro = "";
        char tipo;
        int indice = 0;
        int indiceFinal, indiceInicial;
        
        try {
            if (! fichero.exists()) {
                throw new Exception("El fichero " + fichero.getName() + " no existe.");
            }
            
            fr = new FileReader(fichero);
            entrada = new BufferedReader(fr);
            cadena = entrada.readLine();
            
            while(cadena != null){
                indice = cadena.indexOf("#"); // obtiene la primera coincidencia
                
                if (indice != -1) {
                    tipo = cadena.charAt(0); //  Obtiene el caracter que define  al objeto (Alumno|Profesor)
                    switch (tipo) {
                        case 'A':    //Alumno                        
                            TreeMap<String,Notas> tmAsignaturasAlumno = new TreeMap<>();
                            
                            ArrayList<String> al = new ArrayList<>();
                            
                            Alumno alumno = new Alumno();
                            
                            indiceInicial = indice; // Obtenemos el indice de la primer almohadilla "#". De ahi partiremos para obtener los datos del alumno
                            
                            int contador = 1; //Cuenta la cantidad de almohadillas que contiene cada cadena
                            
                            while (indiceInicial != -1) {
                                
                                indiceInicial++; // Aumentamos en uno +1 el punto de partida para no leer el mismo resultado
                                
                                indiceFinal = cadena.indexOf("#", indiceInicial); //Obtenemos el indice de la siguiente almohadilla "#" en caso que exista
                                
                                if (indiceFinal != -1) {
                                    registro = cadena.substring(indiceInicial, indiceFinal ); //Obtenemos los datos
                                    al.add(registro);
                                   contador++;
                                    
                                }
                                
                                indiceInicial = indiceFinal;                                
                            }
                            
                            alumno.setNombre(al.get(0));
                            alumno.setApellidos(al.get(1));
                            alumno.setCalle(al.get(2));
                            alumno.setCodigoPostal(al.get(3));
                            alumno.setCiudad(al.get(4));
                            alumno.setDni(al.get(5));
                            alumno.setFechaNacimiento(al.get(6));
                            alumno.setCurso(al.get(7)); // Indice del array se encuentra en el valor 7
                            
                            int nroAsignaturas = (contador - 9) / 6; // Con esta operación se obtiene el número de asignaturas matriculadas por el alumno
                            //Se resta el número total de almohadillas "#" menos 9 (a partir de ahí es donde comienza el registro de la primera asignatura) dividido entre 6 que es el número de almohadillas "#" que ocupa cada asignatura con sus calificaciones
                            // 1 asignatura + 5 calificaciones
                            
                            Notas notas = new Notas();
                            int[] evaluaciones = new int[5];
                            int indiceAsignaturas;
                            
                            for (int i = 0; i < nroAsignaturas; i++) { //
                                
                                indiceAsignaturas = 7 + (1 + i * 6); //El puntero del indice del array (al) parte de 7. Le sumamos uno para ubicarnos en la posición de la primera asignatura
                                // y luego multiplicamos por 6 (el número de las asignaturas (i) + las posiciones de las 5 calificaciones que contiene cada asignatura) en cada iteración del ciclo del bucle
                                //para irnos situando en la posición exacta de la siguiente asignatura.
                                
                                for (int j = 0; j < 5; j++) {
                                    evaluaciones[j] = Integer.parseInt(al.get(indiceAsignaturas + j + 1));
                                }
                                notas.setNotas(evaluaciones);
                                
                                tmAsignaturasAlumno.put(al.get(indiceAsignaturas), notas);                                    
                            }
                            
                            alumno.setTmAsignaturasAlumno(tmAsignaturasAlumno);
                            key = alumno.getApellidos() + ", " + alumno.getNombre(); // Creamos la clave de referencia del treeMap                            
                            lista.put(key, alumno);//Lo añadimos el objeto alumno al treeMap
          
                            break;
                            
                        case 'P'://Profesores
                            Profesor profesor = new Profesor();
                            TreeMap<String,String> tmAsignaturasProfesor = new TreeMap<>();
                            ArrayList<String> pro = new ArrayList<>();
                            
                            indiceInicial = indice; // Obtenemos el indice de la primer almohadilla "#". De ahi partiremos para obtener los datos del alumno
                            
                            contador = 1; //Cuenta la cantidad de almohadillas que contiene cada cadena
                            
                            while (indiceInicial != -1) {
                                
                                indiceInicial++; // Aumentamos en uno +1 el punto de partida para no leer el mismo resultado
                                
                                indiceFinal = cadena.indexOf("#", indiceInicial); //Obtenemos el indice de la siguiente almohadilla "#" en caso que exista
                                
                                if (indiceFinal != -1) {
                                    registro = cadena.substring(indiceInicial, indiceFinal ); //Obtenemos los datos
                                    pro.add(registro);
                                   contador++;
                                    
                                }
                                
                                indiceInicial = indiceFinal;                                
                            }
                            
                            profesor.setNombre(pro.get(0));
                            profesor.setApellidos(pro.get(1));
                            profesor.setCalle(pro.get(2));
                            profesor.setCodigoPostal(pro.get(3));
                            profesor.setCiudad(pro.get(4));
                            profesor.setDni(pro.get(5));
                            profesor.setFechaNacimiento(pro.get(6));
                            profesor.setSueldoBase(Double.valueOf(pro.get(7))); // Indice del array se encuentra en el valor 7
                            
                            for (int i = 0; i < 12; i++) {  
                                 profesor.setHorasExtra(i , Integer.valueOf(pro.get(7 + 1 + i))); // Establecemos las horas extras de los 12 meses del año
                            }
                            
                            profesor.setTipoIRPF(Double.valueOf(pro.get(20)));
                            profesor.setCuentaIBAN(pro.get(21));
                            
                            //Comprobamos que existen más datos en el array, lo que significa que el profesor tiene asignada X asignaturas
                            if ( (pro.size() - 21) > 0) {
                                
                                String codCurso = null;
                                String nombreCurso = null;
                                
                                for (int i = 22; i < pro.size(); i++) {
                                    if (i % 2 == 0) { // los indices pares indican el codigo del curso y los impares el nombre
                                        codCurso = pro.get(i);
                                    }else{
                                        nombreCurso = pro.get(i);
                                    }
                                    tmAsignaturasProfesor.put(codCurso, nombreCurso);
                                }
                            }
                            
                            profesor.setTmAsignaturas(tmAsignaturasProfesor);
                            key = profesor.getApellidos() + ", " + profesor.getNombre(); // Creamos la clave de referencia del treeMap
                            lista.put(key, profesor); //Lo añadimos al treeMap
                            break;
                            
                        default:
                            break;
                    }
                }
                cadena = entrada.readLine();
            }
                           
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        } catch (Exception e) {
            System.out.println("Se ha producido un error: " + e.getMessage());
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return lista;
    }
}

