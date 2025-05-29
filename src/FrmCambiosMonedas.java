import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import datechooser.beans.DateChooserCombo;
import entidades.CambioMoneda;
import entidades.Pais;
import servicios.CambioMonedaServicio;
import servicios.PaisServicio;

public class FrmCambiosMonedas extends JFrame {

    private JComboBox cmbMoneda;
    private DateChooserCombo dccDesde, dccHasta;
    private JTabbedPane tpCambiosMoneda;
    private JPanel pnlGrafica;
    private JPanel pnlEstadisticas;
    private JPanel pnlPais;
    private JLabel lblBandera;
    private JLabel lblMapa;
    private JLabel lblInfoPais;

    private List<String> monedas;

    public FrmCambiosMonedas() {
        setTitle("Cambios de Monedas");
        setSize(900, 600); // Aumentado el tamaño para acomodar las imágenes
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Configurar fechas por defecto
        dccDesde = new DateChooserCombo();
        dccHasta = new DateChooserCombo();
        
        // Establecer fechas por defecto (último año)
        Calendar calHasta = Calendar.getInstance();
        Calendar calDesde = Calendar.getInstance();
        calDesde.add(Calendar.YEAR, -1);
        
        dccHasta.setSelectedDate(calHasta);
        dccDesde.setSelectedDate(calDesde);

        JToolBar tb = new JToolBar();

        JButton btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Grafica Cambios vs Fecha");
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGraficarClick();
            }
        });
        tb.add(btnGraficar);

        JButton btnCalcularEstadisticas = new JButton();
        btnCalcularEstadisticas.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnCalcularEstadisticas.setToolTipText("Estadísticas de la moneda seleccionada");
        btnCalcularEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCalcularEstadisticasClick();
            }
        });
        tb.add(btnCalcularEstadisticas);

        // Contenedor con BoxLayout (vertical)
        JPanel pnlCambios = new JPanel();
        pnlCambios.setLayout(new BoxLayout(pnlCambios, BoxLayout.Y_AXIS));

        // Panel superior con datos de proceso y país
        JPanel pnlSuperior = new JPanel(new BorderLayout());
        
        // Panel de datos de proceso
        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        JLabel lblMoneda = new JLabel("Moneda");
        lblMoneda.setBounds(10, 10, 100, 25);
        pnlDatosProceso.add(lblMoneda);

        cmbMoneda = new JComboBox();
        cmbMoneda.setBounds(110, 10, 100, 25);
        cmbMoneda.addActionListener(e -> actualizarInfoPais());
        pnlDatosProceso.add(cmbMoneda);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(220, 10, 100, 25);
        pnlDatosProceso.add(dccDesde);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(330, 10, 100, 25);
        pnlDatosProceso.add(dccHasta);

        pnlSuperior.add(pnlDatosProceso, BorderLayout.NORTH);

        // Panel de información del país
        pnlPais = new JPanel(new BorderLayout());
        pnlPais.setPreferredSize(new Dimension(pnlPais.getWidth(), 150));
        pnlPais.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Panel para imágenes
        JPanel pnlImagenes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        lblBandera = new JLabel();
        lblMapa = new JLabel();
        pnlImagenes.add(lblBandera);
        pnlImagenes.add(lblMapa);

        // Panel para información
        lblInfoPais = new JLabel("Seleccione una moneda para ver la información del país");
        lblInfoPais.setHorizontalAlignment(SwingConstants.CENTER);

        pnlPais.add(pnlImagenes, BorderLayout.CENTER);
        pnlPais.add(lblInfoPais, BorderLayout.SOUTH);

        pnlSuperior.add(pnlPais, BorderLayout.CENTER);

        // Paneles de gráfica y estadísticas
        pnlGrafica = new JPanel();
        pnlGrafica.setPreferredSize(new Dimension(800, 400));
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);
        spGrafica.setPreferredSize(new Dimension(800, 400));

        pnlEstadisticas = new JPanel();

        tpCambiosMoneda = new JTabbedPane();
        tpCambiosMoneda.addTab("Gráfica", spGrafica);
        tpCambiosMoneda.addTab("Estadísticas", pnlEstadisticas);

        // Agregar componentes
        pnlCambios.add(pnlSuperior);
        pnlCambios.add(tpCambiosMoneda);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlCambios, BorderLayout.CENTER);

        cargarDatos();
    }

    private List<CambioMoneda> datos;

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") +
                "/src/datos/Cambios Monedas.csv";
        System.out.println("Cargando datos desde: " + nombreArchivo);
        datos = CambioMonedaServicio.getDatos(nombreArchivo);
        System.out.println("Número de registros cargados: " + datos.size());
        var monedas = CambioMonedaServicio.getMonedas(datos);
        System.out.println("Monedas disponibles: " + monedas);

        DefaultComboBoxModel dcbm = new DefaultComboBoxModel(monedas.toArray());
        cmbMoneda.setModel(dcbm);
    }

    private void btnGraficarClick() {
        if (cmbMoneda.getSelectedIndex() >= 0) {
            try {
                String moneda = (String) cmbMoneda.getSelectedItem();
                Calendar fechaDesde = dccDesde.getSelectedDate();
                Calendar fechaHasta = dccHasta.getSelectedDate();

                if (fechaDesde == null || fechaHasta == null) {
                    System.out.println("Error: Las fechas no pueden ser nulas");
                    return;
                }

                LocalDate desde = fechaDesde.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate hasta = fechaHasta.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                System.out.println("Generando gráfica para moneda: " + moneda);
                System.out.println("Período: " + desde + " hasta " + hasta);

                // Cambiar a la pestaña de Grafica
                tpCambiosMoneda.setSelectedIndex(0);

                var datosFiltrados = CambioMonedaServicio.filtrarCambioMonedas(moneda, desde, hasta, datos);
                System.out.println("Registros filtrados: " + datosFiltrados.size());

                if (datosFiltrados.isEmpty()) {
                    System.out.println("No hay datos para mostrar en el período seleccionado");
                    return;
                }

                var fechas = CambioMonedaServicio.getFechas(datosFiltrados);
                var cambios = CambioMonedaServicio.getCambios(datosFiltrados);

                var datosGrafica = CambioMonedaServicio.getDatosGrafica(fechas, cambios,
                        "Cambio de " + moneda + " por USD");
                var grafica = CambioMonedaServicio.getGrafica(datosGrafica,
                        "Cambios de la moneda " + moneda + " entre " + desde + " y " + hasta);
                CambioMonedaServicio.mostrarGrafica(pnlGrafica, grafica);
            } catch (Exception e) {
                System.out.println("Error al generar la gráfica: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor seleccione una moneda");
        }
    }

    private void btnCalcularEstadisticasClick() {
        if (cmbMoneda.getSelectedIndex() >= 0) {
            try {
                String moneda = (String) cmbMoneda.getSelectedItem();
                Calendar fechaDesde = dccDesde.getSelectedDate();
                Calendar fechaHasta = dccHasta.getSelectedDate();

                if (fechaDesde == null || fechaHasta == null) {
                    System.out.println("Error: Las fechas no pueden ser nulas");
                    return;
                }

                LocalDate desde = fechaDesde.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate hasta = fechaHasta.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                System.out.println("Calculando estadísticas para moneda: " + moneda);
                System.out.println("Período: " + desde + " hasta " + hasta);

                // Cambiar a la pestaña de estadísticas
                tpCambiosMoneda.setSelectedIndex(1);

                var datosFiltrados = CambioMonedaServicio.filtrarCambioMonedas(moneda, desde, hasta, datos);
                if (datosFiltrados.isEmpty()) {
                    System.out.println("No hay datos para mostrar en el período seleccionado");
                    return;
                }

                var estadistiscas = CambioMonedaServicio.getEstadististicas(moneda, desde, hasta, datos);
                System.out.println("Estadísticas calculadas: " + estadistiscas.size());

                pnlEstadisticas.removeAll();
                pnlEstadisticas.setLayout(new GridBagLayout());
                int fila = 0;
                for (var estadistisca : estadistiscas.entrySet()) {
                    var gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = fila;
                    pnlEstadisticas.add(new JLabel(estadistisca.getKey()), gbc);
                    gbc.gridx = 1;
                    pnlEstadisticas.add(new JLabel(String.format("%.2f",estadistisca.getValue())), gbc);
                    fila++;
                }
                pnlEstadisticas.revalidate();
            } catch (Exception e) {
                System.out.println("Error al calcular estadísticas: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor seleccione una moneda");
        }
    }

    private void actualizarInfoPais() {
        if (cmbMoneda.getSelectedIndex() >= 0) {
            String moneda = (String) cmbMoneda.getSelectedItem();
            Pais pais = PaisServicio.getPaisPorMoneda(moneda);
            
            if (pais != null) {
                // Actualizar imágenes
                try {
                    ImageIcon iconoBandera = new ImageIcon(getClass().getResource(pais.getRutaBandera()));
                    ImageIcon iconoMapa = new ImageIcon(getClass().getResource(pais.getRutaMapa()));
                    
                    // Redimensionar imágenes si es necesario
                    Image imgBandera = iconoBandera.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
                    Image imgMapa = iconoMapa.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    
                    lblBandera.setIcon(new ImageIcon(imgBandera));
                    lblMapa.setIcon(new ImageIcon(imgMapa));
                } catch (Exception e) {
                    System.out.println("Error al cargar imágenes: " + e.getMessage());
                    lblBandera.setIcon(null);
                    lblMapa.setIcon(null);
                }

                // Actualizar información
                String info = String.format("<html><center><h2>%s</h2>" +
                        "Moneda: %s (%s)<br>" +
                        "Tasa de cambio actual: %.2f USD</center></html>",
                        pais.getNombre(),
                        pais.getMoneda(),
                        pais.getSimboloMoneda(),
                        pais.getTasaCambio());
                lblInfoPais.setText(info);
            } else {
                lblBandera.setIcon(null);
                lblMapa.setIcon(null);
                lblInfoPais.setText("No se encontró información para la moneda seleccionada");
            }
        }
    }

}
