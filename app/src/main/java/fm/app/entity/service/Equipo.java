package fm.app.entity.service;

import java.time.LocalDate;
public class Equipo {

    private int id;
    private String tipoEquipo;
    private String codigoBarra;
    private String codigoPatrimonial;
    private String descripcion;
    private String estado;
    private LocalDate fechaCompra;
    private String marca;
    private String modelo;
    private String nombreEquipo;
    private String numeroOrden;
    private String serie;
    private Empleado responsable;
    private Ubicacion ubicacion;
    private Foto foto;

    public Equipo() {
    }

    public Equipo(int id) {
        this.id = id;
    }

    public Equipo(int id, String tipoEquipo, String codigoBarra, String codigoPatrimonial, String descripcion, String estado, LocalDate fechaCompra, String marca, String modelo, String nombreEquipo, String numeroOrden, String serie, Empleado responsable, Ubicacion ubicacion, Foto foto) {
        this.id = id;
        this.tipoEquipo = tipoEquipo;
        this.codigoBarra = codigoBarra;
        this.codigoPatrimonial = codigoPatrimonial;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCompra = fechaCompra;
        this.marca = marca;
        this.modelo = modelo;
        this.nombreEquipo = nombreEquipo;
        this.numeroOrden = numeroOrden;
        this.serie = serie;
        this.responsable = responsable;
        this.ubicacion = ubicacion;
        this.foto = foto;
    }

    public Equipo(InventoryItems inventoryItems) {
        this.id = id;
        this.tipoEquipo = inventoryItems.getTipoEquipo();
        this.codigoBarra = inventoryItems.getCodigoBarra();
        this.codigoPatrimonial = inventoryItems.getCodigoPatrimonial();
        this.descripcion = inventoryItems.getDescripcion();
        this.estado = inventoryItems.getEstado();
        this.fechaCompra = inventoryItems.getFechaCompra();
        this.marca = inventoryItems.getMarca();
        this.modelo = inventoryItems.getModelo();
        this.nombreEquipo = inventoryItems.getNombreEquipo();
        this.numeroOrden = inventoryItems.getNumeroOrden();
        this.serie = inventoryItems.getSerie();
        this.responsable = inventoryItems.getResponsable();
        this.ubicacion = inventoryItems.getUbicacion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoPatrimonial() {
        return codigoPatrimonial;
    }

    public void setCodigoPatrimonial(String codigoPatrimonial) {
        this.codigoPatrimonial = codigoPatrimonial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
