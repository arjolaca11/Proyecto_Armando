/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

/**
 *
 * @author arjol
 */
public class Amigo {
    private String nombre;
    private double saldo;

    // Constructor vacío )
    public Amigo() {
        this.nombre = "";
        this.saldo = 0;
    }

    // Constructor principal
    public Amigo(String nombre) {
        this.nombre = nombre;
        this.saldo = 0;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    // Setters 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Ajusta el saldo (puede ser positivo o negativo)
    public void ajustarSaldo(double monto) {
        this.saldo = this.saldo + monto;
    }


}
