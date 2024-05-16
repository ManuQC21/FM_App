package fm.app.entity.service;

import java.time.LocalDateTime;

public class    Movimiento {

    private int id;
    private LocalDateTime fechaMovimiento;
    private String nombreResponsable;
    private String motivoMovimiento;
    private Equipo equipo;
    private Ubicacion ubicacionOrigen;
    private Ubicacion ubicacionDestino;

    public Movimiento() {
    }

    public Movimiento(Integer id) {
        this.id = id;
    }

    public Movimiento(Integer id, LocalDateTime fechaMovimiento, String nombreResponsable, String motivoMovimiento, Equipo equipo, Ubicacion ubicacionOrigen, Ubicacion ubicacionDestino) {
        this.id = id;
        this.fechaMovimiento = fechaMovimiento;
        this.nombreResponsable = nombreResponsable;
        this.motivoMovimiento = motivoMovimiento;
        this.equipo = equipo;
        this.ubicacionOrigen = ubicacionOrigen;
        this.ubicacionDestino = ubicacionDestino;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(String motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Ubicacion getUbicacionOrigen() {
        return ubicacionOrigen;
    }

    public void setUbicacionOrigen(Ubicacion ubicacionOrigen) {
        this.ubicacionOrigen = ubicacionOrigen;
    }

    public Ubicacion getUbicacionDestino() {
        return ubicacionDestino;
    }

    public void setUbicacionDestino(Ubicacion ubicacionDestino) {
        this.ubicacionDestino = ubicacionDestino;
    }
}
