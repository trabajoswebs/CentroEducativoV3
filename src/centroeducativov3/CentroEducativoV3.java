/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centroeducativov3;


import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Acer
 */
public class CentroEducativoV3 {

    public static String curso;
    public static double pagoPorHoraExtra;
    static TreeMap<String, Persona> lista = new TreeMap<String, Persona>();
    static TreeMap<String, String> tmEEEE = new TreeMap<String, String>();//Map con entidades bancarias
    static TreeMap<String, String> tmEEEESSSS = new TreeMap<String, String>();//Map con sucursales bancarias
    static TreeMap<String, String> tmCC = new TreeMap<String, String>();//Map con nombres de cursos
    static TreeMap<String, String> tmCCASIGNA = new TreeMap<String, String>();//Map con curso+asignatura

    public static void setCurso(String cur) {
        curso = cur;
    }

    public static void setPagoPorHoraExtra(double pPorHoraExtra) {
        pagoPorHoraExtra = pPorHoraExtra;
    }

    public static void setLista(TreeMap<String, Persona> list) {
        lista = list;
    }

    public static void setTmEEEE(TreeMap<String, String> tmpEEEE) {
        tmEEEE = tmpEEEE;
    }

    public static void setTmEEEESSSS(TreeMap<String, String> tmpEEEESSSS) {
        tmEEEESSSS = tmpEEEESSSS;
    }

    public static void setTmCC(TreeMap<String, String> tmpCC) {
        tmCC = tmpCC;
    }

    public static void setTmCCASIGNA(TreeMap<String, String> tmpCCASIGNA) {
        tmCCASIGNA = tmpCCASIGNA;
    }

    public static String getCurso() {
        return curso;
    }

    public static double getPagoPorHoraExtra() {
        return pagoPorHoraExtra;
    }

    public static TreeMap<String, Persona> getLista() {
        return lista;
    }

    public static TreeMap<String, String> getTmEEEE() {
        return tmEEEE;
    }

    public static TreeMap<String, String> getTmEEEESSSS() {
        return tmEEEESSSS;
    }

    public static TreeMap<String, String> getTmCC() {
        return tmCC;
    }

    public static TreeMap<String, String> getTmCCASIGNA() {
        return tmCCASIGNA;
    }

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        //Se carga TreeMap y las variables globales
        TablasCursos.cargaGlobales();
        Cuenta.cargaEntidadesBancarias(tmEEEE);
        Cuenta.cargaSucursalesBancarias(tmEEEESSSS);
        TablasCursos.cargaCursos(tmCC);
        TablasCursos.cargaCursosAsignaturas(tmCCASIGNA);

        // CREAMOS UN FICHERO PARA GUARDAR LOS PROFESORES
        File ruta = new File("Profesores");//crea un objeto con una ruta
        File fPersonas = new File(ruta, "Personas3.txt");//crea un objeto fichero en la ruta

        try {
            if (fPersonas.exists()) {//Si existe el fichero podemos leer los registros que tiene
                lista = FuncionesFicheros.obtenerTreeMapDeArchivo(fPersonas);
            } else {
                fPersonas.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error con el fichero: " + e.getMessage());
        }

        Boolean salir = false;

        int option = 0;
        do {
            System.out.println("\n*************** MENU PRINCIPAL ***************\n");
            System.out.println("\t1. MANTENIMIENTO ALUMNOS");
            System.out.println("\t2. MANTENIMIENTO PROFESORES");        
            System.out.println("\t3. LISTADO DE NOMBRES DE PROFESORES Y ALUMNOS");
            System.out.println("\t4. LISTADO DE NOMBRES DE PROFESORES");
            System.out.println("\t5. LISTADO DE NOMBRES DE ALUMNOS");
            System.out.println("\t0. FIN DE LA APLICACION");
            System.out.print("\n\t   Opcion seleccionada: ");

            option = sc.nextInt();

            switch (option) {
                case 1:
                    subMenuAlumno();
                    break;
                case 2:
                    subMenuProfesores();       
                    break;
                case 3:
                    imprimirListados("Curso Académico: %s\nLISTADO DE PROFESORES Y ALUMNOS\nAPELLIDOS/NOMBRE\n", true, true);
                    break;
                case 4:
                    imprimirListados("Curso Académico: %s\nLISTADO DE PROFESORES\nAPELLIDOS/NOMBRE\n", true, false);
                    break;
                case 5:
                    imprimirListados("Curso Académico: %s\nLISTADO DE ALUMNOS\nAPELLIDOS/NOMBRE\n", false, true);
                    break;
                case 0:
                default:
                    try {
                        FuncionesFicheros.almacenarColPersonasEnArchivo(lista, fPersonas);
                    } catch (IOException e) {
                        System.out.println("Error: -> " + e.getMessage());
                    }

                    salir = true;
                    break;

            }

        } while (!salir);

    }

    /**
     *
     */
    public static void subMenuProfesores() {
        boolean correcto;
        int opt = 0;
        String key;
        Profesor profe;
        Persona per = null;
        correcto = false;

        do {

            try {
                System.out.println("\n\n\n *************** MANTENIMIENTO DE PROFESORES ***************\n");
                System.out.println("\t1. ALTA DE UN PROFESOR");
                System.out.println("\t2. BAJA DE UN PROFESOR");
                System.out.println("\t3. CONSULTA DE DATOS PERSONALES DE UN PROFESOR");
                System.out.println("\t4. INTRODUCIR HORAS EXTRAORDINARIAS DE UN MES");
                System.out.println("\t5. LISTADO DE PROFESORES. DATOS PERSONALES");
                System.out.println("\t6. LISTADO DE PROFESORES. CLASES QUE IMPARTEN");
                System.out.println("\t7. LISTADO DE NOMINAS DE UN MES");
                System.out.println("\t8. MANTENIMIENTO DE ASIGNATURAS IMPARTIDAS POR CADA PROFESOR");
                System.out.println("\t0. VUELTA AL MENU PRINCIPAL");
                System.out.print("\n\t   Opcion seleccionada: ");
                opt = sc.nextInt();

                switch (opt) {
                    case 1:
                        System.out.println("Opcion seleccionada: Alta profesor");
                        try {
                            profe = new Profesor();
                            profe.nuevoProfesor();
                            key = profe.getApellidos() + ", " + profe.getNombre();
                            System.out.println(key);
                            if (lista.containsKey(key)) {
                                throw new Exception("Este nombre ya existe. No puedo grabarlo");
                            }
                            lista.put(key, profe);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        correcto = false;
                        do {
                            try {
                                System.out.println("Opción seleccionada: Baja Profesor");
                                System.out.println("indique el nombre del profesor:");
                                String n = sc.nextLine().toLowerCase();
                                System.out.println("indique el apellido del profesor:");
                                String a = sc.nextLine().toLowerCase();
                                key = a + ", " + n;
                                if (lista.containsKey(key) == false) {
                                    throw new Exception("El nombre que desea eliminar no existe");
                                }
                                lista.remove(key);
                                correcto = true;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } while (!correcto);
                        break;
                    case 3:
                        correcto = false;
                        System.out.println("Opcion seleccionada: Consulta datos perosnales profesor");
                        do {
                            try {
                                sc.nextLine();
                                System.out.println("Indique el nombre del profesor: ");
                                String n = sc.nextLine().toLowerCase();
                                System.out.println("Indique los apellidos del profesor: ");
                                String a = sc.nextLine().toLowerCase();
                                key = a + ", " + n;

                                if (lista.containsKey(key)) {
                                    per = lista.get(key);
                                } else {
                                    throw new Exception("El profesor no existe.");
                                }
                                if (per instanceof Profesor) {
                                    profe = (Profesor) per;
                                    System.out.println(lista.get(key).toString());
                                } else {
                                    throw new Exception("No se trata de un profesor. ");
                                }
                                correcto = true;
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } while (!correcto);
                        break;
                    case 4:
                        correcto = false;
                        int mes,
                         horas;
                        do {
                            try {
                                System.out.println("\tOpcion seleccionada: Introducir horas extras por mes: ");
                                System.out.println("\nHoras extraordinarias por los profesores en el mes de: ");
                                mes = sc.nextInt();
                                if (mes < 1 && mes > 12) {
                                    throw new Exception("Mes incorrecto.");
                                }

                                Iterator it = lista.keySet().iterator();

                                while (it.hasNext()) {
                                    key = (String) it.next();
                                    if (lista.get(key) instanceof Profesor) {
                                        profe = (Profesor) lista.get(key);
                                        System.out.println("Nombre del profesor: ");
                                        System.out.println(key);
                                        System.out.println("Horas realizadas: ");
                                        horas = sc.nextInt();
                                        if (horas > 20) {
                                            throw new Exception("No se puede exceder de mas de 20 horas al mes");
                                        }
                                        profe.setHorasExtra(mes - 1, horas);
                                    }
                                }
                                correcto = true;
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                                sc.nextLine();
                            }

                        } while (!correcto);
                        break;
                    case 5:
                        System.out.println("Opción seleccionada: Imprime datos personales de los profesores.");
                        Iterator it = lista.keySet().iterator();

                        while (it.hasNext()) {
                            key = (String) it.next();
                            per = lista.get(key);
                            if (per instanceof Profesor) {
                                profe = (Profesor) per;
                                System.out.println(profe.toString());
                                System.out.println();
                            }
                        }
                        break;

                    case 6:
                        System.out.println("Opción seleccionada: Datos de las clases que imparten los profesores.");

                        it = lista.keySet().iterator();
                        while (it.hasNext()) {
                            key = (String) it.next();
                            if (lista.get(key) instanceof Profesor) {
                                profe = (Profesor) lista.get(key);
                                System.out.println(profe.imprimeAsignaturas());
                                System.out.println();
                            }
                        }
                        break;

                    case 7:
                        correcto = false;
                        do {
                            System.out.println("Opción seleccionada: Imprimir nomina de cada profesor según el mes: ");
                            try {
                                System.out.println("Nómnias del mes: ");
                                mes = sc.nextInt();
                                if (mes < 1 || mes > 12) {
                                    throw new InputMismatchException("Error al introducir el mes del año: ");
                                }
                                it = lista.keySet().iterator();
                                while (it.hasNext()) {
                                    key = (String) it.next();
                                    per = lista.get(key);
                                    if (per instanceof Profesor) {
                                        profe = (Profesor) per;
                                        System.out.println(profe.ImprimirNominas(mes));
                                    }
                                    System.out.println();
                                }
                                correcto = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Error: " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } while (!correcto);
                        break;
                    case 8:
                        correcto = false;

                        do {
                            try {
                                System.out.println("Opción seleccionada: Mantenimiento de asignaturas impartidas por cada profesor: ");
                                sc.nextLine();
                                System.out.println("Nombre del profesor: ");
                                String n = sc.nextLine();
                                System.out.println("Apellidos del Profesor: ");
                                String a = sc.nextLine();
                                key = a.toLowerCase() + ", " + n.toLowerCase();

                                if (!lista.containsKey(key)) {
                                    throw new Exception("No existe ningún profesor con ese nombre.");
                                }

                                per = lista.get(key);
                                if (per instanceof Persona) {
                                    profe = (Profesor) per;
                                    System.out.println(profe.ImprimeProfesor());
                                    profe.asignaturasProfesor();
                                    correcto = true;
                                }
                            } catch (Exception e) {
                                correcto = false;
                                System.out.println("Error: " + e.getMessage());
                            }

                        } while (!correcto);
                        System.out.println("");
                        break;
                    case 0:
                        correcto = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!correcto);
    }

    /**
     *
     */
    public static void subMenuAlumno() {
        int opt = 0;
        String key;
        do {
            try {
                System.out.println("\n\n\n *************** MANTENIMIENTO DE ALUMNOS ***************\n");
                System.out.println("\t1. ALTA DE UN ALUMNO");
                System.out.println("\t2. BAJA DE UN ALUMNO");
                System.out.println("\t3. CONSULTA DE DATOS PERSONALES DE UN ALUMNO");
                System.out.println("\t4. INTRODUCIR NOTAS DE UNA ASIGNATURA Y EVALUACION A TODOS LOS MATRICULADOS");
                System.out.println("\t5. LISTADO DE ALUMNOS DE UN GRUPO. DATOS PERSONALES");
                System.out.println("\t6. LISTADO DE ALUMNOS MATRICULADOS EN UNA ASIGNATURA");
                System.out.println("\t7. LISTADO DE BOLETINES DE NOTAS DE UNA EVALUACION Y CURSO");
                System.out.println("\t0. VUELTA AL MENU PRINCIPAL");
                System.out.print("\n\t   Opcion seleccionada: ");
                opt = sc.nextInt();

                switch (opt) {
                    case 0:
                        break;
                    case 1:
                        System.out.println("\t1. ALTA DE UN ALUMNO");
                        Alumno alumn = new Alumno();
                        alumn.pideDatos();

                        String nombreCompleto = alumn.getApellidos() + ", " + alumn.getNombre();
                        try {
                            lista.put(nombreCompleto, alumn);
                            System.out.println("El alumno ha sido dado de alta correctamente.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 2:
                        try {
                            System.out.println("\t2. BAJA DE UN ALUMNO");
                            nombreCompleto = getKey();

                            if (lista.containsKey(nombreCompleto)) {
                                if (lista.get(nombreCompleto) instanceof Alumno) {
                                    lista.remove(nombreCompleto);
                                } else {
                                    throw new Exception("No se puede eliminar este registro al no tratarse de un alumno. ");
                                }
                            } else {
                                throw new Exception("No existe ningun alumno con este nombre. ");
                            }

                            System.out.println("El alumno ha sido dado de baja.");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 3:
                        try {
                            System.out.println("\t3. CONSULTA DE DATOS PERSONALES DE UN ALUMNO");
                            nombreCompleto = getKey();

                            if (lista.containsKey(nombreCompleto)) {
                                if (lista.get(nombreCompleto) instanceof Alumno) {
                                    System.out.println(lista.get(nombreCompleto).toString());
                                } else {
                                    throw new Exception("No se puede eliminar este registro al no tratarse de un alumno. ");
                                }
                            } else {
                                throw new Exception("No existe ningun alumno con este nombre. ");
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 4:
                        String[] evaluacion = {"Primera Evaluación", "Segunda Evaluación", "Tercera Evaluación", "Evaluación Ordinaria", "Evaluación Extraordinaria"};
                        System.out.println("\t4. INTRODUCIR NOTAS DE UNA ASIGNATURA Y EVALUACION A TODOS LOS MATRICULADOS");
                        Iterator it = lista.keySet().iterator();

                        while (it.hasNext()) {
                            key = (String) it.next();
                            if (lista.get(key) instanceof Alumno) {
                                System.out.println("Alumno: " + key + ":");
                                Alumno al = (Alumno) lista.get(key);
                                Iterator itAsignatura = al.getTmAsignaturasAlumno().keySet().iterator();

                                try {
                                    while (itAsignatura.hasNext()) {
                                        String keyAsig = (String) itAsignatura.next();
                                        System.out.println("Nota de la asignatura: " + tmCCASIGNA.get(keyAsig) + "(" + keyAsig + "): ");
                                        Notas notas = new Notas();
                                        int[] nota = new int[5];

                                        for (int i = 0; i < 5; i++) {
                                            System.out.println("Introduce las notas de la " + evaluacion[i]);
                                            nota[i] = sc.nextInt();
                                            if (nota[i] < 0 || nota[i] > 10) {
                                                throw new Exception("Las notas solo puntuan de 0 a 10.");
                                            }
                                        }

                                        al.getTmAsignaturasAlumno().get(keyAsig).setNotas(nota);

                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }

                            }
                        }
                        break;
                    case 5:
                        sc.nextLine();
                        System.out.println("\t5. LISTADO DE ALUMNOS DE UN CURSO. DATOS PERSONALES");
                        boolean listado = true;
                        do {
                            try {
                                System.out.println("Indique el código de un curso: ");
                                String curso = sc.nextLine().toUpperCase();
                                if (tmCC.containsKey(curso)) {
                                    it = lista.keySet().iterator();
                                    while (it.hasNext()) {
                                        key = (String) it.next();
                                        if (lista.get(key) instanceof Alumno) {
                                            alumn = (Alumno) lista.get(key);
                                            if (alumn.getCurso().equalsIgnoreCase(curso)) {
                                                System.out.println(alumn.toString());
                                            }
                                        }
                                    }
                                    listado = true;
                                } else {
                                    throw new Exception("Ese curso no existe. ");
                                }
                            } catch (Exception e) {
                                listado = false;
                                System.out.println("Error al introducir el código del curso: " + e.getMessage());
                            }
                        } while (!listado);

                        break;
                    case 6:
                        sc.nextLine();
                        System.out.println("\t6. LISTADO DE ALUMNOS MATRICULADOS EN UNA ASIGNATURA");
                        listado = true;
                        do {
                            try {
                                System.out.println("Indique una Asignatura: ");
                                String asig = sc.nextLine().toUpperCase();
                                if (tmCCASIGNA.containsKey(asig)) {
                                    it = lista.keySet().iterator();
                                    while (it.hasNext()) {
                                        key = (String) it.next();
                                        if (lista.get(key) instanceof Alumno) {
                                            alumn = (Alumno) lista.get(key);
                                            if (alumn.getTmAsignaturasAlumno().containsKey(asig)) {
                                                System.out.println(alumn.toString());
                                            }
                                        }
                                    }
                                    listado = true;
                                } else {
                                    throw new Exception("Esa asignatura no existe. ");
                                }
                            } catch (Exception e) {
                                listado = false;
                                System.out.println("Error al introducir la asignatura: " + e.getMessage());
                            }
                        } while (!listado);

                        break;
                    case 7:
                        System.out.println("\t7. LISTADO DE BOLETINES DE NOTAS DE UNA EVALUACION Y CURSO");
                        boolean correcto = false;
                        do {
                            sc.nextLine();
                            try {
                                System.out.println("Indique el código del curso: ");
                                String codCurso = sc.nextLine();
                                System.out.println("Indique el número de la evaluación: ");
                                int eval = sc.nextInt();
                                it = lista.keySet().iterator();
                                while (it.hasNext()) {
                                    key = (String) it.next();
                                    if (lista.get(key) instanceof Alumno) {
                                        alumn = (Alumno) lista.get(key);
                                        System.out.println(alumn.boletinNotas(codCurso, eval));
                                    }
                                }
                                correcto = true;
                            } catch (InputMismatchException e) {
                                correcto = false;
                                System.out.println("Ha occurrido una excepción: " + e.getMessage());
                            }
                        } while (!correcto);

                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Ha ocurrido una excepción en el submenu Alumno: " + e.getMessage());
            }
        } while (opt != 0);
    }

    /**
     *
     * @return
     */
    public static String getKey() {
        StringBuilder nombreCompleto = new StringBuilder();
        String nombre, apellido;
        sc.nextLine();
        System.out.println("Indica el nombre:");
        nombre = sc.nextLine().toLowerCase();
        System.out.println("Indica el apellido:");
        apellido = sc.nextLine().toLowerCase();

        nombreCompleto.append(apellido);
        nombreCompleto.append(", ");
        nombreCompleto.append(nombre);
        return nombreCompleto.toString();
    }

    /**
     *
     * @param text
     * @param p
     * @param s
     */
    public static void imprimirListados(String text, boolean p, boolean s) {
        System.out.printf(text, getCurso());

        String t = (p && !s) ? "(P) " : "";

        for (Map.Entry<String, Persona> entry : lista.entrySet()) {
            if (p && entry.getValue() instanceof Profesor) {
                System.out.printf("%s%s, %s\n", t, entry.getValue().getApellidos(), entry.getValue().getNombre());
            } else if (s && entry.getValue() instanceof Alumno) {
                Alumno a = Alumno.class.cast(entry.getValue());

                System.out.printf("(%s) %s, %s\n", a.getCurso(), a.getApellidos(), a.getNombre());
            }
        }
    }
}
