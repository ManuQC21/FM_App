package fm.app.entity.service;

import java.time.LocalDateTime;

public class Auditoria {

    private int id;
    private LocalDateTime fechaAuditoria;
    private String observaciones;
    private String estadoAuditoria;
    private Equipo equipo;
    public Auditoria() {
    }

    public Auditoria(Integer id) {
        this.id = id;
    }

    public Auditoria(Integer id, LocalDateTime fechaAuditoria, String observaciones, String estadoAuditoria, Equipo equipo) {
        this.id = id;
        this.fechaAuditoria = fechaAuditoria;
        this.observaciones = observaciones;
        this.estadoAuditoria = estadoAuditoria;
        this.equipo = equipo;
    }

    public LocalDateTime getFechaAuditoria() {
        return fechaAuditoria;
    }

    public void setFechaAuditoria(LocalDateTime fechaAuditoria) {
        this.fechaAuditoria = fechaAuditoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstadoAuditoria() {
        return estadoAuditoria;
    }

    public void setEstadoAuditoria(String estadoAuditoria) {
        this.estadoAuditoria = estadoAuditoria;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
