package entidades;

public class Pais {
    private int id;
    private String nombre;
    private String moneda;
    private String simboloMoneda;
    private double tasaCambio;
    private String rutaMapa;
    private String rutaBandera;

    public Pais() {
    }

    public Pais(int id, String nombre, String moneda, String simboloMoneda, double tasaCambio, String rutaMapa, String rutaBandera) {
        this.id = id;
        this.nombre = nombre;
        this.moneda = moneda;
        this.simboloMoneda = simboloMoneda;
        this.tasaCambio = tasaCambio;
        this.rutaMapa = rutaMapa;
        this.rutaBandera = rutaBandera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSimboloMoneda() {
        return simboloMoneda;
    }

    public void setSimboloMoneda(String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
    }

    public double getTasaCambio() {
        return tasaCambio;
    }

    public void setTasaCambio(double tasaCambio) {
        this.tasaCambio = tasaCambio;
    }

    public String getRutaMapa() {
        return rutaMapa;
    }

    public void setRutaMapa(String rutaMapa) {
        this.rutaMapa = rutaMapa;
    }

    public String getRutaBandera() {
        return rutaBandera;
    }

    public void setRutaBandera(String rutaBandera) {
        this.rutaBandera = rutaBandera;
    }
} 