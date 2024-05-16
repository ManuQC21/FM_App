package fm.app.Entity;

public class DetalleMovimiento {

    private int id;
    private int cantidad;
    private String tipoMovimiento;
    private String observaciones;
    private Movimiento movimiento;

    // Constructor vacío
    public DetalleMovimiento() {
    }

    public DetalleMovimiento(Integer id) {
        this.id = id;
    }

    public DetalleMovimiento(Integer id, int cantidad, String tipoMovimiento, String observaciones, Movimiento movimiento) {
        this.id = id;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.observaciones = observaciones;
        this.movimiento = movimiento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }
}
