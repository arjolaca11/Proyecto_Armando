/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

/**
 *
 * @author arjol
 */
import javax.swing.*;
import java.awt.*;

public class VerHistorial extends JFrame {
    
     public void mostrar(JFrame owner, ControlGastos sistema,
                        Color fondo, Color azul, Color hover, Color texto){
        setTitle("Historial de movimientos");
        setSize(820, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8,8));
        getContentPane().setBackground(fondo);

        JLabel tituloLabel = new JLabel("Historial de movimientos", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setForeground(texto);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
        add(tituloLabel, BorderLayout.NORTH);

        String contenido = construirHistorial(sistema);
        JTextArea area = new JTextArea(contenido, 20, 60);
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));
        area.setMargin(new Insets(8,12,8,12));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton volver = new JButton("Volver"){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int radio = 18;
                Color fondoBtn = getModel().isRollover() ? hover : azul;
                g2.setColor(fondoBtn);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                g2.setColor(new Color(120,120,120));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        volver.setFont(new Font("Arial", Font.BOLD, 16));
        volver.setForeground(Color.BLACK);
        volver.setFocusPainted(false);
        volver.setContentAreaFilled(false);
        volver.setOpaque(false);
        volver.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        pie.setBackground(fondo);
        pie.add(volver);
        add(pie, BorderLayout.SOUTH);
        volver.addActionListener(e -> dispose());

        setVisible(true);
    }

    private String construirHistorial(ControlGastos sistema){
        int total = sistema.getTotalMovimientos();
        if (total == 0) return "No hay movimientos todavía.\n";

        String texto = "";
        int i = 0;
        while (i < total){
            Movimiento mv = sistema.getMovimiento(i);
            texto = texto + (i + 1) + ") " + mv.getDescripcion() + "\n";
            texto = texto + "   Pagó: " + mv.getQuienPago().getNombre() + "\n";
            texto = texto + "   Monto: " + mv.getMonto() + " Dólares\n";
            texto = texto + "   Participantes: ";

            Amigo[] partes = mv.getParticipantes();
            int j = 0;
            while (j < partes.length){
                texto = texto + partes[j].getNombre();
                if (j < partes.length - 1) texto = texto + ", ";
                j = j + 1;
            }

            texto = texto + "\n\n";
            i = i + 1;
        }
        return texto;
    }
}


    