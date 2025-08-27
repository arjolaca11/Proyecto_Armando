/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasexamen;

/**
 *
 * @author arjol
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import pruebasexamen.Amigo;
import pruebasexamen.ControlGastos;
import pruebasexamen.Movimiento;

public class ControlGastosGUI extends JFrame {

  
        private ControlGastos sistema;

    // Estilo
    private Color COLOR_FONDO = new Color(235, 235, 235);
    private Color COLOR_AZUL  = new Color(170, 210, 255);
    private Color COLOR_HOVER = new Color(140, 190, 250);
    private Color COLOR_TEXTO = Color.BLACK;

    //  Fuente con soporte de emoji (Windows) 
    private String FUENTE_EMOJI_PRINCIPAL = "Segoe UI Emoji";
    private String FUENTE_EMOJI_FALLBACK  = "Segoe UI Symbol"; // respaldo

    private Font fuenteEmoji(int estilo, int tamanio){
        String[] disponibles = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        boolean encontrada = false;
        int i = 0;
        while (i < disponibles.length){
            if (disponibles[i].equalsIgnoreCase(FUENTE_EMOJI_PRINCIPAL)) { encontrada = true; }
            i = i + 1;
        }
        if (encontrada) return new Font(FUENTE_EMOJI_PRINCIPAL, estilo, tamanio);
        return new Font(FUENTE_EMOJI_FALLBACK, estilo, tamanio);
    }

    // Botón redondeado e 
    private JButton botonRedondo(String texto){
        JButton boton = new JButton(texto){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int radio = 18;
                Color fondo = getModel().isRollover() ? COLOR_HOVER : COLOR_AZUL;
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                g2.setColor(new Color(120,120,120));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setFont(fuenteEmoji(Font.BOLD, 16));
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        return boton;
    }

    //  Diálogo base + pie + mensajes bonitos 
    private JDialog dialogoBase(String titulo){
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setSize(820, 520);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(8,8));
        dialogo.getContentPane().setBackground(COLOR_FONDO);
        JLabel tituloLabel = new JLabel(titulo, SwingConstants.CENTER);
        tituloLabel.setFont(fuenteEmoji(Font.BOLD, 18));
        tituloLabel.setForeground(COLOR_TEXTO);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
        dialogo.add(tituloLabel, BorderLayout.NORTH);
        return dialogo;
    }
    private JPanel pie(JButton volver, JButton aceptar){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        p.setBackground(COLOR_FONDO);
        if (aceptar != null) p.add(aceptar);
        if (volver != null)  p.add(volver);
        return p;
    }
    private void mensaje(String titulo, String htmlTexto, String tipo){
        JDialog dialogo = dialogoBase(titulo);

        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));
        Icon icono = tipo.equals("error") ? UIManager.getIcon("OptionPane.errorIcon")
                                          : UIManager.getIcon("OptionPane.informationIcon");
        panel.add(new JLabel(icono), BorderLayout.WEST);
        panel.add(new JLabel("<html><div style='font-family:Segoe UI Emoji, Segoe UI Symbol; font-size:14px; color:#000;'>"
                + htmlTexto + "</div></html>"), BorderLayout.CENTER);

        dialogo.add(panel, BorderLayout.CENTER);
        JButton volver = botonRedondo("OK");
        dialogo.add(pie(volver, null), BorderLayout.SOUTH);
        dialogo.setSize(520, 220);
        volver.addActionListener(ev -> dialogo.dispose());
        dialogo.setVisible(true);
    }
    private void info(String titulo, String texto){ mensaje(titulo, texto, "info"); }
    private void error(String titulo, String texto){ mensaje(titulo, texto, "error"); }

    //  Validaciones 
    private boolean esNombreValido(String texto){
        if (texto == null) return false;
        texto = texto.trim();
        if (texto.length()==0) return false;
        int i = 0;
        while (i < texto.length()){
            char c = texto.charAt(i);
            if (Character.isLetter(c) || c==' ') { }
            else return false;
            i = i + 1;
        }
        return true;
    }
    private boolean existeNombre(String nombre){
        int i = 0;
        while (i < sistema.getTotalAmigos()){
            if (sistema.getAmigo(i).getNombre().equalsIgnoreCase(nombre)) return true;
            i = i + 1;
        }
        return false;
    }
    private boolean esDecimalPositivo(String texto){
        if (texto == null) return false;
        texto = texto.trim();
        if (texto.length()==0) return false;
        int puntos = 0, pos = 0;
        if (texto.charAt(0)=='+') pos = 1;
        while (pos < texto.length()){
            char c = texto.charAt(pos);
            if (c=='.'){ puntos = puntos + 1; if (puntos>1) return false; }
            else if (c<'0' || c>'9'){ return false; }
            pos = pos + 1;
        }
        if (texto.equals(".") || texto.equals("+.") || texto.equals("0") || texto.equals("0.") || texto.equals("0.0")) return false;
        return true;
    }

    //  Pedir datos en bucle
    private String pedirNombre(String titulo, String etiqueta){
        JPanel panel = new JPanel(new GridLayout(0,1,6,6));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160,160,160), 1),
                BorderFactory.createEmptyBorder(12,12,12,12)));
        JLabel label = new JLabel(etiqueta);
        label.setForeground(COLOR_TEXTO);
        label.setFont(fuenteEmoji(Font.BOLD, 16));
        JTextField campo = new JTextField();
        campo.setFont(fuenteEmoji(Font.PLAIN, 16));
        panel.add(label); panel.add(campo);

        int opcion = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) { } else { return null; }

        String valor = campo.getText();
        boolean ok = false;
        while (ok == false) {
            if (valor != null) {
                valor = valor.trim();
                if (esNombreValido(valor) && existeNombre(valor)==false) ok = true;
                else {
                    error("Dato inválido", "Nombre inválido o repetido (solo letras).");
                    opcion = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (opcion == JOptionPane.OK_OPTION) { } else { return null; }
                    valor = campo.getText();
                }
            } else return null;
        }
        return valor;
    }
    private Double pedirMonto(String titulo, String etiqueta){
        JPanel panel = new JPanel(new GridLayout(0,1,6,6));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160,160,160), 1),
                BorderFactory.createEmptyBorder(12,12,12,12)));
        JLabel label = new JLabel(etiqueta);
        label.setForeground(COLOR_TEXTO);
        label.setFont(fuenteEmoji(Font.BOLD, 16));
        JTextField campo = new JTextField();
        campo.setFont(fuenteEmoji(Font.PLAIN, 16));
        panel.add(label); panel.add(campo);

        int opcion = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) { } else { return null; }

        String texto = campo.getText();
        while (esDecimalPositivo(texto) == false){
            error("Monto inválido", "Escriba un número positivo (use punto decimal).");
            opcion = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion == JOptionPane.OK_OPTION) { } else { return null; }
            texto = campo.getText();
        }
        return Double.parseDouble(texto.trim());
    }

    //  Utilitarios de diálogo 
    private void dialogoTexto(String titulo, String contenido){
        JDialog dialogo = dialogoBase(titulo);
        JTextArea area = new JTextArea(contenido, 20, 60);
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));
        area.setMargin(new Insets(8,12,8,12));
        dialogo.add(new JScrollPane(area), BorderLayout.CENTER);
        JButton volver = botonRedondo("Volver");
        dialogo.add(pie(volver, null), BorderLayout.SOUTH);
        volver.addActionListener(ev -> dialogo.dispose());
        dialogo.setVisible(true);
    }

    //Selecciones 
    private String[] nombresAmigos(){
        int n = sistema.getTotalAmigos();
        String[] nombres = new String[n];
        int i = 0;
        while (i < n){ nombres[i] = sistema.getAmigo(i).getNombre(); i = i + 1; }
        return nombres;
    }
    private String seleccionarPagadorConCombo(String titulo){
        String[] nombres = nombresAmigos();
        if (nombres.length == 0) return null;

        JComboBox<String> combo = new JComboBox<String>(nombres);
        combo.setFont(fuenteEmoji(Font.PLAIN, 16));
        combo.setPreferredSize(new Dimension(360, 32));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(COLOR_FONDO);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0; cons.gridy = 0; cons.insets = new Insets(8,8,8,8);
        JLabel etiqueta = new JLabel("Seleccione una persona:");
        etiqueta.setFont(fuenteEmoji(Font.BOLD, 16));
        etiqueta.setForeground(COLOR_TEXTO);
        formulario.add(etiqueta, cons);
        cons.gridy = 1;
        formulario.add(combo, cons);

        JDialog dialogo = dialogoBase(titulo);
        dialogo.add(formulario, BorderLayout.CENTER);
        JButton volver  = botonRedondo("Volver");
        JButton aceptar = botonRedondo("Aceptar");
        dialogo.add(pie(volver, aceptar), BorderLayout.SOUTH);

        String[] elegido = new String[1];
        aceptar.addActionListener(ev -> { elegido[0] = (String) combo.getSelectedItem(); dialogo.dispose(); });
        volver.addActionListener(ev -> { elegido[0] = null; dialogo.dispose(); });

        dialogo.setVisible(true);
        return elegido[0];
    }
    private int[] seleccionarParticipantesConChecks(){
        String[] nombres = nombresAmigos();
        if (nombres.length == 0) return null;

        JPanel lista = new JPanel(new GridLayout(0,1,6,6));
        lista.setBackground(COLOR_FONDO);
        JCheckBox[] checks = new JCheckBox[nombres.length];

        int i = 0;
        while (i < nombres.length){
            JCheckBox casilla = new JCheckBox(nombres[i]);
            casilla.setBackground(COLOR_FONDO);
            casilla.setForeground(COLOR_TEXTO);
            casilla.setFont(fuenteEmoji(Font.PLAIN, 15));
            checks[i] = casilla;
            lista.add(casilla);
            i = i + 1;
        }

        JDialog dialogo = dialogoBase("Seleccione participantes");
        dialogo.add(new JScrollPane(lista), BorderLayout.CENTER);
        JButton volver  = botonRedondo("Volver");
        JButton aceptar = botonRedondo("Aceptar");
        dialogo.add(pie(volver, aceptar), BorderLayout.SOUTH);

        int[][] seleccion = new int[1][];
        aceptar.addActionListener(ev -> {
            int conteo = 0, k = 0;
            while (k < checks.length){
                if (checks[k].isSelected()) conteo = conteo + 1;
                k = k + 1;
            }
            if (conteo == 0) {
                error("Dato requerido", "Debe seleccionar al menos un participante.");
                return;
            }

            int[] indices = new int[conteo];
            int pos = 0; k = 0;
            while (k < checks.length){
                if (checks[k].isSelected()){ indices[pos] = k; pos = pos + 1; }
                k = k + 1;
            }
            seleccion[0] = indices;
            dialogo.dispose();
        });
        volver.addActionListener(ev -> { seleccion[0] = null; dialogo.dispose(); });

        dialogo.setVisible(true);
        return seleccion[0];
    }

   
    private String limpiarDescripcion(String texto){
        if (texto == null) return "";
        texto = texto.trim();
        if (texto.startsWith("Cuenta")){
            int pos = texto.indexOf(" - ");
            if (pos >= 0 && pos + 3 < texto.length()) return texto.substring(pos + 3);
        }
        return texto;
    }
    private String participantesComoLinea(Amigo[] participantes){
        String linea = "";
        int i = 0;
        while (i < participantes.length){
            linea = linea + participantes[i].getNombre();
            if (i < participantes.length - 1) linea = linea + ", ";
            i = i + 1;
        }
        return linea;
    }

    //  RESUMEN TOTAL
    private String reporteDeudasDetalladasPorMovimiento(){
        int totalMovs = sistema.getTotalMovimientos();
        if (totalMovs == 0) return "No hay movimientos todavía.\n";

        String texto = "Detalle de deudas por movimiento (Moneda: Dólares)\n\n";

        int i = 0;
        while (i < totalMovs){
            Movimiento mov = sistema.getMovimiento(i);

            boolean esPago = false;
            if (mov.getParticipantes() != null && mov.getParticipantes().length == 1) esPago = true;
            if (mov.getDescripcion() != null && mov.getDescripcion().equals("PAGO")) esPago = true;

            if (esPago){
                texto = texto
                    + "Pago registrado:\n"
                    + "   " + mov.getQuienPago().getNombre()
                    + " pagó " + mov.getMonto() + " Dólares a "
                    + mov.getParticipantes()[0].getNombre() + ".\n\n";
            } else {
                String desc = limpiarDescripcion(mov.getDescripcion());
                Amigo pagador = mov.getQuienPago();
                Amigo[] partes = mov.getParticipantes();
                int cantidad = partes.length;
                double porPersona = mov.getMonto() / cantidad;

                texto = texto
                    + "Cuenta " + (i+1) + " - " + desc + ":\n"
                    + "   Pagó " + pagador.getNombre() + " " + mov.getMonto() + " Dólares.\n"
                    + "   Participaron " + cantidad + " personas: " + participantesComoLinea(partes) + ".\n";

                int k = 0;
                while (k < cantidad){
                    Amigo participante = partes[k];
                    if (participante.getNombre().equals(pagador.getNombre()) == false){
                        texto = texto
                            + "   - " + participante.getNombre()
                            + " le debe " + porPersona + " Dólares a "
                            + pagador.getNombre() + " por " + desc + ".\n";
                    }
                    k = k + 1;
                }
                texto = texto + "\n";
            }
            i = i + 1;
        }

        //  RESUMEN TOTAL POR PERSONA
        texto = texto + "-----------------------------------------\n";
        texto = texto + "Resumen TOTAL por persona (Dólares):\n";

        double[][] matriz = sistema.calcularMatrizDeudas();  // usa  método del modelo
        int totalAmigos = sistema.getTotalAmigos();
        boolean hayAlguna = false;

        int a = 0;
        while (a < totalAmigos){
            int b = a + 1;
            while (b < totalAmigos){
                double neto = matriz[a][b] - matriz[b][a];   // neto del par (A,B)
                if (neto > 0.0001){
                    texto = texto
                        + " - " + sistema.getAmigo(a).getNombre()
                        + " le debe " + neto + " Dólares a "
                        + sistema.getAmigo(b).getNombre() + ".\n";
                    hayAlguna = true;
                } else if (neto < -0.0001){
                    texto = texto
                        + " - " + sistema.getAmigo(b).getNombre()
                        + " le debe " + (-neto) + " Dólares a "
                        + sistema.getAmigo(a).getNombre() + ".\n";
                    hayAlguna = true;
                }
                b = b + 1;
            }
            a = a + 1;
        }
        if (hayAlguna == false){
            texto = texto + " - Todos están saldados.\n";
        }

        return texto;
    }

    // Acciones 
    private void agregarAmigo(){
        String nombre = pedirNombre("Agregar amigo", "Nombre:");
        if (nombre == null) return;
        sistema.agregarAmigo(nombre);
        info("Listo", "Nuevo Amigo: " + nombre);
    }
    private void agregarMovimiento(){
       
        String descripcion = pedirNombre("Nuevo movimiento", "Descripción:");
        if (descripcion == null) return;

        Double monto = pedirMonto("Nuevo movimiento", "Monto total en Dólares:");
        if (monto == null) return;

        String nombrePagador = seleccionarPagadorConCombo("Quién pagó?");
        if (nombrePagador == null) return;
        int indicePagador = sistema.buscarIndicePorNombre(nombrePagador);

        int[] indicesParticipantes = seleccionarParticipantesConChecks();
        if (indicesParticipantes == null ||indicesParticipantes.length < 2) {
        error("Atención", "No se puede agregar un único participante.");
        return;
        }
        sistema.agregarMovimiento(descripcion, monto.doubleValue(), indicePagador, indicesParticipantes);
        info("Movimiento agregado", descripcion);
    }
    private void borrarMovimiento(){
        if (sistema.getTotalMovimientos() == 0) {
            error("Sin datos", "No hay movimientos para borrar.");
            return;
        }

        String[] columnas = {"#", "Movimiento", "Descripción", "Participantes", "Quien pagó", "Monto", "Moneda"};
        int n = sistema.getTotalMovimientos();
        String[][] filas = new String[n][columnas.length];

        int i = 0;
        while (i < n){
            Movimiento mv = sistema.getMovimiento(i);
            filas[i][0] = String.valueOf(i);
            filas[i][1] = "Cuenta " + (i+1);
            filas[i][2] = limpiarDescripcion(mv.getDescripcion());
            filas[i][3] = participantesComoLinea(mv.getParticipantes());
            filas[i][4] = mv.getQuienPago().getNombre();
            filas[i][5] = String.valueOf(mv.getMonto());
            filas[i][6] = "Dólares";
            i = i + 1;
        }

        JTable tabla = new JTable(filas, columnas);
        tabla.setFont(fuenteEmoji(Font.PLAIN, 14));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(fuenteEmoji(Font.BOLD, 14));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ver nombres completos
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(230);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(420);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(100);

        JDialog dialogo = dialogoBase("Borrar movimiento");
        dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton volver = botonRedondo("Volver");
        JButton borrar = botonRedondo("Borrar seleccionado");
        borrar.setEnabled(false);
        dialogo.add(pie(volver, borrar), BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent evt){
                if (evt.getValueIsAdjusting()) return;
                if (tabla.getSelectedRow() >= 0) borrar.setEnabled(true);
                else borrar.setEnabled(false);
            }
        });

        borrar.addActionListener(ev -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0){
                int indiceReal = Integer.parseInt((String) tabla.getValueAt(fila, 0));
                sistema.eliminarMovimiento(indiceReal);
                dialogo.dispose();
                info("Listo", "Movimiento eliminado.");
            }
        });
        volver.addActionListener(ev -> dialogo.dispose());
        dialogo.setVisible(true);
    }
    private void registrarPago(){
        if (sistema.getTotalAmigos() < 2) {
            error("Atención", "Registre al menos 2 amigos.");
            return;
        }

        String nombreDeudor   = seleccionarPagadorConCombo("Quién paga (deudor)?");
        if (nombreDeudor == null) return;
        String nombreAcreedor = seleccionarPagadorConCombo("A quién paga (acreedor)?");
        if (nombreAcreedor == null) return;

        int idxDeudor   = sistema.buscarIndicePorNombre(nombreDeudor);
        int idxAcreedor = sistema.buscarIndicePorNombre(nombreAcreedor);

        double[][] m = sistema.calcularMatrizDeudas();
        double deudaActual = m[idxDeudor][idxAcreedor];
        if (deudaActual <= 0) {
            error("Sin deuda", "No hay deuda de " + nombreDeudor + " hacia " + nombreAcreedor + ".");
            return;
        }

        Double monto = pedirMonto("Registrar pago", "Monto en Dólares (máximo " + deudaActual + "):");
        if (monto == null) return;
        double valor = monto.doubleValue();
        if (valor > deudaActual) valor = deudaActual;

        sistema.registrarPagoPorNombres(nombreDeudor, nombreAcreedor, valor);
        info("Pago registrado", nombreDeudor + " pagó a " + nombreAcreedor + ".");
    }
  private void verCuentas(){
    // Columnas 
    String[] columnas = {"Movimiento", "Descripción", "Participantes", "Quien pagó", "Monto total"};

    int total = sistema.getTotalMovimientos();
    String[][] filas = new String[total][columnas.length];

    int i = 0;
    while (i < total){
        Movimiento mov = sistema.getMovimiento(i);
        filas[i][0] = "Cuenta " + (i + 1);
        filas[i][1] = limpiarDescripcion(mov.getDescripcion());
        filas[i][2] = participantesComoLinea(mov.getParticipantes());
        filas[i][3] = mov.getQuienPago().getNombre();
        filas[i][4] = f1(mov.getMonto());          // <<< 1 decimal
        i = i + 1;
    }

    JTable tabla = new JTable(filas, columnas);
    tabla.setEnabled(false);
    tabla.setRowHeight(26);
    tabla.setFont(fuenteEmoji(Font.PLAIN, 14));
    tabla.getTableHeader().setFont(fuenteEmoji(Font.BOLD, 14));

    // Anchos ajustados 
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.getColumnModel().getColumn(0).setPreferredWidth(110); // Movimiento
    tabla.getColumnModel().getColumn(1).setPreferredWidth(260); // Descripción
    tabla.getColumnModel().getColumn(2).setPreferredWidth(420); // Participantes
    tabla.getColumnModel().getColumn(3).setPreferredWidth(160); // Quien pagó
    tabla.getColumnModel().getColumn(4).setPreferredWidth(130); // Monto total (1 decimal)

    // l título aclarando la moneda
    JDialog dialogo = dialogoBase("Cuentas registradas (Moneda: Dólares)");
    dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);
    JButton volver = botonRedondo("Volver");
    dialogo.add(pie(volver, null), BorderLayout.SOUTH);
    volver.addActionListener(ev -> dialogo.dispose());
    dialogo.setVisible(true);
}
  
    private void verDeudasDetalladas(){
        dialogoTexto("Deudas ", reporteDeudasDetalladasPorMovimiento());
    }

    //  Constructor GUI
    public ControlGastosGUI() {
        sistema = new ControlGastos();

        UIManager.put("OptionPane.messageFont", fuenteEmoji(Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont",  fuenteEmoji(Font.BOLD, 14));
        UIManager.put("OptionPane.background",  COLOR_FONDO);
        UIManager.put("Panel.background",       COLOR_FONDO);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO);
        UIManager.put("Button.background",      COLOR_AZUL);
        UIManager.put("Button.foreground",      COLOR_TEXTO);

        setTitle("Control de Gastos ");
        setSize(740, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(COLOR_FONDO);

        JLabel titulo = new JLabel("Control de gastos ", SwingConstants.CENTER);
        titulo.setFont(fuenteEmoji(Font.BOLD, 24));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(8, 1, 12, 12));
        centro.setBackground(COLOR_FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(6, 26, 10, 26));

        JButton b1 = botonRedondo("➕ Agregar amigo");
        JButton b2 = botonRedondo("📝 Agregar movimiento");
        JButton b3 = botonRedondo("🗑️ Borrar movimiento");
        JButton b4 = botonRedondo("📑 Ver cuentas (tabla)");
        JButton b5 = botonRedondo("📌 Ver deudas (detalle)");
        JButton b6 = botonRedondo("💱 Registrar pago");
        JButton b7 = botonRedondo("📜 Ver historial");
        JButton b8 = botonRedondo("❌ Salir");

        centro.add(b1); centro.add(b2); centro.add(b3); centro.add(b4);
        centro.add(b5); centro.add(b6); centro.add(b7); centro.add(b8);
        add(centro, BorderLayout.CENTER);

        b1.addActionListener(e -> agregarAmigo());
        b2.addActionListener(e -> agregarMovimiento());
        b3.addActionListener(e -> borrarMovimiento());
        b4.addActionListener(e -> verCuentas());
        b5.addActionListener(e -> verDeudasDetalladas());
        b6.addActionListener(e -> registrarPago());
        b7.addActionListener(e -> new VerHistorial().mostrar(this, sistema, COLOR_FONDO, COLOR_AZUL, COLOR_HOVER, COLOR_TEXTO));
        b8.addActionListener(e -> System.exit(0));

        setLocationRelativeTo(null);
    }

   
    // helper para mostrar 1 decimal
private String f1(double valor){
    double redondeado = Math.round(valor * 10.0) / 10.0; // 1 decimal
    return String.valueOf(redondeado);
}
}

