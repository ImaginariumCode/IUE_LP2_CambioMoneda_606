package servicios;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import entidades.CambioMoneda;

public class CambioMonedaServicio {

    public static List<CambioMoneda> getDatos(String nombreArchivo) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            System.out.println("Intentando leer archivo: " + nombreArchivo);
            Stream<String> lineas = Files.lines(Paths.get(nombreArchivo));
            List<CambioMoneda> resultado = lineas.skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> {
                        try {
                            return new CambioMoneda(textos[0],
                                    LocalDate.parse(textos[1], formatoFecha),
                                    Double.parseDouble(textos[2]));
                        } catch (Exception e) {
                            System.out.println("Error al procesar línea: " + String.join(",", textos));
                            System.out.println("Error: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(cambio -> cambio != null)
                    .collect(Collectors.toList());
            System.out.println("Datos cargados exitosamente: " + resultado.size() + " registros");
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error al leer el archivo: " + ex.getMessage());
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<String> getMonedas(List<CambioMoneda> datos) {
        List<String> monedas = datos.stream()
                .map(CambioMoneda::getMoneda)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Monedas encontradas: " + monedas);
        return monedas;
    }

    public static List<CambioMoneda> filtrarCambioMonedas(String moneda,
            LocalDate desde, LocalDate hasta,
            List<CambioMoneda> datos) {
        if (datos == null || datos.isEmpty()) {
            System.out.println("Error: No hay datos para filtrar");
            return Collections.emptyList();
        }

        if (desde == null || hasta == null) {
            System.out.println("Error: Las fechas no pueden ser nulas");
            return Collections.emptyList();
        }

        if (desde.isAfter(hasta)) {
            System.out.println("Error: La fecha inicial no puede ser posterior a la fecha final");
            return Collections.emptyList();
        }

        List<CambioMoneda> filtrados = datos.stream()
                .filter(cambio -> {
                    if (cambio == null) {
                        System.out.println("Error: Se encontró un registro nulo");
                        return false;
                    }
                    return cambio.getMoneda().equals(moneda)
                            && !(cambio.getFecha().isBefore(desde) || cambio.getFecha().isAfter(hasta));
                })
                .collect(Collectors.toList());

        System.out.println("Registros filtrados para " + moneda + ": " + filtrados.size());
        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron registros para la moneda " + moneda + 
                             " en el período " + desde + " a " + hasta);
        }
        return filtrados;
    }

    public static List<LocalDate> getFechas(List<CambioMoneda> datos) {
        if (datos == null || datos.isEmpty()) {
            System.out.println("Error: No hay datos para obtener fechas");
            return Collections.emptyList();
        }

        List<LocalDate> fechas = datos.stream()
                .sorted(Comparator.comparing(CambioMoneda::getFecha))
                .map(CambioMoneda::getFecha)
                .collect(Collectors.toList());

        System.out.println("Fechas obtenidas: " + fechas.size());
        return fechas;
    }

    public static List<Double> getCambios(List<CambioMoneda> datos) {
        if (datos == null || datos.isEmpty()) {
            System.out.println("Error: No hay datos para obtener cambios");
            return Collections.emptyList();
        }

        List<Double> cambios = datos.stream()
                .sorted(Comparator.comparing(CambioMoneda::getFecha))
                .map(CambioMoneda::getCambio)
                .collect(Collectors.toList());

        System.out.println("Cambios obtenidos: " + cambios.size());
        return cambios;
    }

    public static TimeSeriesCollection getDatosGrafica(List<LocalDate> fechas,
            List<Double> cambios, String titulo) {
        if (fechas == null || cambios == null || fechas.isEmpty() || cambios.isEmpty()) {
            System.out.println("Error: No hay datos para generar la gráfica");
            return new TimeSeriesCollection();
        }

        if (fechas.size() != cambios.size()) {
            System.out.println("Error: El número de fechas no coincide con el número de cambios");
            return new TimeSeriesCollection();
        }

        var serie = new TimeSeries(titulo);
        IntStream.range(0, fechas.size())
                .forEach((i) -> {
                    var fecha = fechas.get(i);
                    serie.add(new Day(fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear()),
                            cambios.get(i));
                });
        var datosGrafica = new TimeSeriesCollection();
        datosGrafica.addSeries(serie);
        System.out.println("Datos de gráfica generados: " + serie.getItemCount() + " puntos");
        return datosGrafica;
    }

    public static JFreeChart getGrafica(TimeSeriesCollection datosGrafica,
            String titulo) {
        return ChartFactory.createTimeSeriesChart(titulo, "Fechas",
                "Cambio por USD", datosGrafica);
    }

    public static void mostrarGrafica(JPanel pnl, JFreeChart grafica) {
        pnl.removeAll();
        var pnlGrafica = new ChartPanel(grafica);
        pnlGrafica.setPreferredSize(new Dimension(pnl.getWidth(), pnl.getHeight()));
        pnlGrafica.setMouseWheelEnabled(true);
        pnl.setLayout(new BorderLayout());
        pnl.add(pnlGrafica, BorderLayout.CENTER);
        pnl.validate();
    }

    public static double getPromedio(List<Double> datos) {
        return datos.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public static double getDesviacionEstandar(List<Double> datos) {
        var promedio = getPromedio(datos);
        return Math.sqrt(datos.stream()
                .mapToDouble(dato -> Math.pow(dato - promedio, 2))
                .average()
                .orElse(0));
    }

    public static double getMaximo(List<Double> datos) {
        return datos.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);
    }

    public static double getMinimo(List<Double> datos) {
        return datos.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);
    }

    public static double getMediana(List<Double> datos) {
        var datosOrdenados = datos.stream()
                .sorted()
                .collect(Collectors.toList());
        var n = datosOrdenados.size();
        return n % 2 == 0 ? (datosOrdenados.get(n / 2 - 1) + datosOrdenados.get(n / 2)) / 2 : datosOrdenados.get(n / 2);
    }

    public static double getModa(List<Double> datos) {
        return datos.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static Map<String, Double> getEstadististicas(String moneda,
            LocalDate desde, LocalDate hasta,
            List<CambioMoneda> datos) {
        var datosFiltrados = CambioMonedaServicio.filtrarCambioMonedas(moneda, desde, hasta, datos);
        var cambios = CambioMonedaServicio.getCambios(datosFiltrados);

        Map<String, Double> estadistiscas = new HashMap<>();
        estadistiscas.put("Promedio", getPromedio(cambios));
        estadistiscas.put("Desviación", getDesviacionEstandar(cambios));
        estadistiscas.put("Máximo", getMaximo(cambios));
        estadistiscas.put("Mínimo", getMinimo(cambios));
        estadistiscas.put("Moda", getModa(cambios));
        estadistiscas.put("Mediana", getMediana(cambios));

        return estadistiscas;
    }
}
