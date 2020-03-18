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
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class TablasCursos {
    public static final String DIRECTORY =  "Profesores\\";
    public static final String CAFILEPATH = DIRECTORY + "CursoAsignatura.txt";
    public static final String DTGENERALESFILENAME = "datosGenerales.txt";
    
    
    static void cargaCursos(TreeMap<String, String> tmCC){
        cargaDatos(DIRECTORY + "cursos.txt", CentroEducativoV3.tmCC);
    }
    
    
    static void cargaCursosAsignaturas(TreeMap<String, String> tmCCASIGNA){
        cargaDatos(CAFILEPATH, CentroEducativoV3.tmCCASIGNA);
    }
    
    /**
     * Lee los datos que se encuentra en el fichero y los carga en el treeMap
     * @param filePath
     * @param tm
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
    
    /**
     * carga las variables globales de curso academico y precio de las horas extras, si no existe el fichero lo crea, pide y graba los datos
     */
    public static void cargaGlobales(){
        Scanner sc = new Scanner(System.in);
        RandomAccessFile fichero = null;
        String curso;
        String importeHorasExtras;
        String cadena;
        
        try {
            crearFichero(DIRECTORY, DTGENERALESFILENAME); //Se crea el fichero si no existe            
            fichero = new RandomAccessFile(DIRECTORY + DTGENERALESFILENAME, "rw");
            
            if (fichero.length() == 0) {
                
                boolean correcto = true;
                
                do {    
                        System.out.println("\n\tAún no se ha establecido los datos del curso y del importe de las horas extras");
                        System.out.println("\nIndique el curso académico actual: ");
                        curso = sc.nextLine();
                        System.out.println("Indique el precio de importe de las horas extras:");
                        importeHorasExtras = sc.nextLine();                        
                        
                    try {                        
                        if((curso.trim().isEmpty()) || importeHorasExtras.trim().isEmpty()) throw new Exception("Debe introducir los valores.");
                        if (! curso.contains("/")) throw new Exception("Debe introducir correctamente el formato del curso academico: Año/Año");
                        
                        if (importeHorasExtras.contains(",")) { // Si contiene una coma "," la reemplazamos por un punto "."
                            
                            importeHorasExtras = importeHorasExtras.replace(",", ".");
                        }
                        
                        cadena = curso + "#" + importeHorasExtras + "\n";
                        fichero.writeBytes(cadena);
                        CentroEducativoV3.setCurso(curso); //Establecemos el curso en la variable global
                        double importe = Double.parseDouble(importeHorasExtras); // Convertimos el String en double
                        CentroEducativoV3.setPagoPorHoraExtra(importe); //Establecemos el importe de las horas extras en la variable global
                        System.out.println("Los datos se han registrado correctamente. ");
                        correcto = true;
                        System.out.println();
                        
                    }catch (IOException ioe) {
                        System.out.println("Ha ocurrido un error de fichero: " + ioe.getMessage());
                        correcto = false;                    
                    } catch (Exception e) {
                        System.out.println("Ha ocurrido un error: " + e.getMessage());
                        correcto = false;
                    }
                    
                } while (! correcto);

            }else{
                
                fichero.seek(0); // Llevamos el puntero al inicio del documento
                cadena = fichero.readLine(); // Obtenemos la lectura de la cadena
                
                if (cadena.length() > 0) { //Se comprueba que existe datos en la cadena
                    int indice = cadena.indexOf("#"); //Se obtiene el indice de la almohadilla "#" que divide las dos variables Curso e importeHorasExtra
                    CentroEducativoV3.setCurso(cadena.substring(0, indice)); //Obtenemos la cadena del curso y establecemos la variable global 
                    Double iHorasExtras = Double.parseDouble(cadena.substring(indice + 1, cadena.length())); //Obtenemos la cadena del importe de horas extras y lo convertimos en double
                    CentroEducativoV3.setPagoPorHoraExtra(iHorasExtras);
                }
            }

        }catch(IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println("Ha ocurido un error: " + e.getMessage());
        }finally{
            if (fichero != null) {
                try {
                    fichero.close();
                } catch (IOException ex) {
                    Logger.getLogger(TablasCursos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
     /**
     * Crea el fichero, pasado por parametro, si no existe
     * @param ruta direccion de la ubicacion del fichero
     * @param fichero nombre del fichero
     * @throws IOException 
     */
    public static void crearFichero(String ruta, String fichero) throws IOException {
        File cursoRuta = new File(ruta);
        File cursoFichero = new File(cursoRuta, fichero);
        
        if (! cursoFichero.exists()) {

            System.out.println("El fichero " + cursoFichero.getAbsolutePath() + " no existe.");

            if (!cursoRuta.exists()) {
                System.out.println("El directorio " + cursoRuta.getAbsolutePath() + " no existe.");

                if (cursoRuta.mkdir()) {
                    System.out.println("Se ha creado el directorio " + cursoRuta.getAbsolutePath());

                    if (cursoFichero.createNewFile()) {
                        System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                    } else {
                        throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                    }

                } else {
                    throw new IOException("No se ha podido crear la ruta " + cursoRuta.getAbsolutePath());
                }
            } else {

                if (cursoFichero.createNewFile()) {
                    System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                } else {
                    throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                }

            }
        }
    }
}


