/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

/**
 *
 * @author arjol
 */
/**
  Representa un movimiento registrado:
 - descripcion: texto libre (ej. "Cena", "PAGO")
  - quienPagoIndex: índice del amigo que pagó
  - monto: monto total del movimiento
  - participantesIndices: índices de los amigos que participaron
 
.
 */
public class Movimiento {
  
     private String descripcion;
    private Amigo quienPago;
    private double monto;
    private Amigo[] participantes;

    public Movimiento(String descripcion, Amigo quienPago, double monto, Amigo[] participantes) {
        this.descripcion = descripcion;
        this.quienPago = quienPago;
        this.monto = monto;
        this.participantes = participantes;
    }

    public String getDescripcion() { return descripcion; }
    public Amigo getQuienPago()    { return quienPago; }
    public double getMonto()       { return monto; }
    public Amigo[] getParticipantes(){ return participantes; }
}
