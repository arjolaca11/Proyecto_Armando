/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

/**
 *
 * @author arjol
 */
public class ControlGastos {
    // Capacidades (solo arreglos)
    private int maxAmigos = 30;
    private int maxMovs   = 300;

    // Datos
    private Amigo[] amigos;
    private int totalAmigos;

    private Movimiento[] movimientos;
    private int totalMovimientos;

    public ControlGastos() {
        amigos = new Amigo[maxAmigos];
        movimientos = new Movimiento[maxMovs];
        totalAmigos = 0;
        totalMovimientos = 0;
        cargarEjemploExamen(); // se carga automáticamente en base al ejemplo
    }

    //  AMIGOS 
    public void agregarAmigo(String nombre) {
        if (totalAmigos < amigos.length) {
            amigos[totalAmigos] = new Amigo(nombre);
            totalAmigos = totalAmigos + 1;
        }
    }
    public int getTotalAmigos()        { return totalAmigos; }
    public Amigo getAmigo(int indice)  { return amigos[indice]; }

    public int buscarIndicePorNombre(String nombre) {
        int indice = 0;
        while (indice < totalAmigos) {
            if (amigos[indice].getNombre().equalsIgnoreCase(nombre)) return indice;
            indice = indice + 1;
        }
        return -1;
    }

    // MOVIMIENTOS 
    public int getTotalMovimientos()            { return totalMovimientos; }
    public Movimiento getMovimiento(int indice) { return movimientos[indice]; }

    public void agregarMovimiento(String descripcion, double monto,
                                  int indiceQuienPago, int[] indicesParticipantes) {
        if (totalMovimientos >= movimientos.length) return;
        if (indiceQuienPago < 0 || indiceQuienPago >= totalAmigos) return;
        if (indicesParticipantes == null || indicesParticipantes.length == 0) return;

        Amigo[] participantes = new Amigo[indicesParticipantes.length];
        int indiceArray = 0;
        while (indiceArray < participantes.length) {
            int indiceAmigo = indicesParticipantes[indiceArray];
            if (indiceAmigo >= 0 && indiceAmigo < totalAmigos) participantes[indiceArray] = amigos[indiceAmigo];
            indiceArray = indiceArray + 1;
        }

        Movimiento nuevo = new Movimiento(descripcion, amigos[indiceQuienPago], monto, participantes);
        movimientos[totalMovimientos] = nuevo;
        totalMovimientos = totalMovimientos + 1;
    }

    public void eliminarMovimiento(int posicion) {
        if (posicion < 0 || posicion >= totalMovimientos) return;
        int indice = posicion;
        while (indice < totalMovimientos - 1) {
            movimientos[indice] = movimientos[indice + 1];
            indice = indice + 1;
        }
        movimientos[totalMovimientos - 1] = null;
        totalMovimientos = totalMovimientos - 1;
    }

    // Registrar un pago de deudor -> acreedor (como un movimiento “PAGO” con 1 participante)
    public void registrarPagoPorNombres(String nombreDeudor, String nombreAcreedor, double monto) {
        int indiceDeudor = buscarIndicePorNombre(nombreDeudor);
        int indiceAcreedor = buscarIndicePorNombre(nombreAcreedor);
        if (indiceDeudor < 0 || indiceAcreedor < 0) return;

        Amigo[] participantes = new Amigo[1];
        participantes[0] = amigos[indiceAcreedor];
        if (totalMovimientos < movimientos.length) {
            movimientos[totalMovimientos] = new Movimiento("PAGO", amigos[indiceDeudor], monto, participantes);
            totalMovimientos = totalMovimientos + 1;
        }
    }

    //  CÁLCULOS 
    // matrizDeudas[i][j] = cuánto debe el amigo i al amigo j
    public double[][] calcularMatrizDeudas() {
        double[][] matrizDeudas = new double[totalAmigos][totalAmigos];

        int indiceMovimiento = 0;
        while (indiceMovimiento < totalMovimientos) {
            Movimiento movimientoActual = movimientos[indiceMovimiento];

            boolean esPago = false;
            if (movimientoActual.getParticipantes() != null && movimientoActual.getParticipantes().length == 1) esPago = true;
            if (movimientoActual.getDescripcion() != null && movimientoActual.getDescripcion().equals("PAGO")) esPago = true;

            if (esPago) {
                int indiceDeudor = buscarIndicePorNombre(movimientoActual.getQuienPago().getNombre());
                int indiceAcreedor = buscarIndicePorNombre(movimientoActual.getParticipantes()[0].getNombre());
                if (indiceDeudor != -1 && indiceAcreedor != -1) {
                    matrizDeudas[indiceDeudor][indiceAcreedor] = matrizDeudas[indiceDeudor][indiceAcreedor] - movimientoActual.getMonto();
                    if (matrizDeudas[indiceDeudor][indiceAcreedor] < 0) matrizDeudas[indiceDeudor][indiceAcreedor] = 0;
                }
            } else {
                Amigo[] participantes = movimientoActual.getParticipantes();
                if (participantes != null && participantes.length > 0) {
                    double montoPorPersona = movimientoActual.getMonto() / participantes.length;
                    int indicePagador = buscarIndicePorNombre(movimientoActual.getQuienPago().getNombre());
                    int indiceParticipante = 0;
                    while (indiceParticipante < participantes.length) {
                        int indicePar = buscarIndicePorNombre(participantes[indiceParticipante].getNombre());
                        if (indicePar != -1 && indicePar != indicePagador) {
                            matrizDeudas[indicePar][indicePagador] = matrizDeudas[indicePar][indicePagador] + montoPorPersona;
                        }
                        indiceParticipante = indiceParticipante + 1;
                    }
                }
            }
            indiceMovimiento = indiceMovimiento + 1;
        }

        // Neteo: dejar solo una dirección
        int indiceA = 0;
        while (indiceA < totalAmigos) {
            int indiceB = 0;
            while (indiceB < totalAmigos) {
                if (indiceA != indiceB) {
                    double deudaAB = matrizDeudas[indiceA][indiceB];
                    double deudaBA = matrizDeudas[indiceB][indiceA];
                    if (deudaAB > 0 && deudaBA > 0) {
                        if (deudaAB >= deudaBA) { matrizDeudas[indiceA][indiceB] = deudaAB - deudaBA; matrizDeudas[indiceB][indiceA] = 0; }
                        else                    { matrizDeudas[indiceB][indiceA] = deudaBA - deudaAB; matrizDeudas[indiceA][indiceB] = 0; }
                    }
                }
                indiceB = indiceB + 1;
            }
            indiceA = indiceA + 1;
        }
        return matrizDeudas;
    }

    // Resumen neto 
    public String mostrarDeudasTodos() {
        double[][] matrizDeudas = calcularMatrizDeudas();
        String texto = "";
        boolean hayDeudas = false;

        int indiceA = 0;
        while (indiceA < totalAmigos) {
            int indiceB = 0;
            while (indiceB < totalAmigos) {
                if (indiceA != indiceB && matrizDeudas[indiceA][indiceB] > 0.00001) {
                    texto = texto + amigos[indiceA].getNombre() + " le debe a " + amigos[indiceB].getNombre()
                            + " : " + matrizDeudas[indiceA][indiceB] + "\n";
                    hayDeudas = true;
                }
                indiceB = indiceB + 1;
            }
            indiceA = indiceA + 1;
        }
        if (hayDeudas == false) texto = texto + "Sin deudas.\n";
        return texto;
    }

    //  EJEMPLO DEL EXAMEN -
    private void cargarEjemploExamen() {
        // amigos
        agregarAmigo("Joshua");
        agregarAmigo("Greivin");
        agregarAmigo("Guillermo");
        agregarAmigo("Andres");
        agregarAmigo("Tavo");
        agregarAmigo("David");

        int idxJoshua      = buscarIndicePorNombre("Joshua");
        int idxGreivin    = buscarIndicePorNombre("Greivin");
        int idxGuillermo  = buscarIndicePorNombre("Guillermo");
        int idxAndres     = buscarIndicePorNombre("Andres");
        int idxTavo       = buscarIndicePorNombre("Tavo");
        int idxDavid      = buscarIndicePorNombre("David");

        // movimientos (Moneda: Dólares)
        agregarMovimiento("Cuenta 1 - Desayuno coffePrime", 120, idxGuillermo, new int[]{idxJoshua,idxGreivin,idxGuillermo,idxAndres,idxTavo,idxDavid});
        agregarMovimiento("Cuenta 2 - Almuerzo Pig Factory", 200, idxGuillermo, new int[]{idxJoshua,idxGreivin,idxGuillermo,idxAndres,idxTavo});
        agregarMovimiento("Cuenta 3 - Cena FastFood",         50, idxDavid,     new int[]{idxJoshua,idxDavid});
        agregarMovimiento("Cuenta 4 - Pizza Hut",             100, idxTavo,      new int[]{idxGuillermo,idxAndres,idxGreivin,idxTavo});
        agregarMovimiento("Cuenta 5 - Quicksilver store",     150, idxGreivin,   new int[]{idxGuillermo});
        agregarMovimiento("Cuenta 6 - Apple store",           200, idxJoshua,     new int[]{idxAndres});
        agregarMovimiento("Cuenta 7 - Desayuno Chillis",      150, idxGreivin,   new int[]{idxJoshua,idxGreivin,idxGuillermo,idxAndres,idxTavo,idxDavid});
        agregarMovimiento("Cuenta 8 - Almuerzo Hooters",      180, idxTavo,      new int[]{idxJoshua,idxGreivin,idxGuillermo,idxAndres,idxTavo,idxDavid});
    }
}
