/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

import javax.swing.SwingUtilities;

/**
 *
 * @author arjol
 */

public class IntroProgra_ProyectoFinal_Armando {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ControlGastosGUI().setVisible(true);
            }
        });
    }

}