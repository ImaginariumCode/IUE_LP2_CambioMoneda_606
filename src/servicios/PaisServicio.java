package servicios;

import java.util.ArrayList;
import java.util.List;
import entidades.Pais;

public class PaisServicio {
    private static List<Pais> paises = new ArrayList<>();

    static {
        // Inicializar los países con sus monedas
        paises.add(new Pais(1, "Argentina", "ARS", "$", 900.0, 
            "/imagenes/mapas/argentina.png", "/imagenes/banderas/argentina.png"));
        paises.add(new Pais(2, "Colombia", "COP", "$", 4000.0, 
            "/imagenes/mapas/colombia.png", "/imagenes/banderas/colombia.png"));
        paises.add(new Pais(3, "Brasil", "BRL", "R$", 5.0, 
            "/imagenes/mapas/brasil.png", "/imagenes/banderas/brasil.png"));
        paises.add(new Pais(4, "China", "CNY", "¥", 7.2, 
            "/imagenes/mapas/china.png", "/imagenes/banderas/china.png"));
        paises.add(new Pais(5, "Unión Europea", "EUR", "€", 0.92, 
            "/imagenes/mapas/europa.png", "/imagenes/banderas/europa.png"));
    }

    public static List<Pais> getPaises() {
        return paises;
    }

    public static Pais getPaisPorMoneda(String moneda) {
        return paises.stream()
                .filter(p -> p.getMoneda().equals(moneda))
                .findFirst()
                .orElse(null);
    }

    public static void actualizarTasaCambio(String moneda, double nuevaTasa) {
        paises.stream()
                .filter(p -> p.getMoneda().equals(moneda))
                .findFirst()
                .ifPresent(p -> p.setTasaCambio(nuevaTasa));
    }
} 