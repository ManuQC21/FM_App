package fm.app.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

import fm.app.entity.service.Equipo;

public class EquipoUtil {

    public static boolean isValidEquipo(Equipo equipo) {
        return isValidTipoEquipo(equipo.getTipoEquipo())
                && isValidCodigoPatrimonial(equipo.getCodigoPatrimonial())
                && isValidDescripcion(equipo.getDescripcion())
                && isValidEstado(equipo.getEstado())
                && isValidFechaCompra(equipo.getFechafechaRevision())
                && isValidMarca(equipo.getMarca())
                && isValidModelo(equipo.getModelo())
                && isValidNombreEquipo(equipo.getNombreEquipo())
                && isValidNumeroOrden(equipo.getNumeroOrden())
                && isValidSerie(equipo.getSerie());
    }

    public static boolean isValidTipoEquipo(String tipoEquipo) {
        return !TextUtils.isEmpty(tipoEquipo) && tipoEquipo.length() <= 255 && Pattern.matches("^[A-Za-z ]+$", tipoEquipo);
    }

    public static boolean isValidCodigoPatrimonial(String codigoPatrimonial) {
        return !TextUtils.isEmpty(codigoPatrimonial) && codigoPatrimonial.length() <= 255 && Pattern.matches("^[A-Za-z0-9]+$", codigoPatrimonial);
    }

    public static boolean isValidDescripcion(String descripcion) {
        return !TextUtils.isEmpty(descripcion) && descripcion.length() <= 255;
    }

    public static boolean isValidEstado(String estado) {
        return !TextUtils.isEmpty(estado) && estado.length() <= 255 && Pattern.matches("^[A-Za-z ]+$", estado);
    }

    public static boolean isValidFechaCompra(String fechaCompra) {
        return !TextUtils.isEmpty(fechaCompra) && Pattern.matches("^\\d{2}/\\d{2}/\\d{4}$", fechaCompra);
    }

    public static boolean isValidMarca(String marca) {
        return !TextUtils.isEmpty(marca) && marca.length() <= 255;
    }

    public static boolean isValidModelo(String modelo) {
        return !TextUtils.isEmpty(modelo) && modelo.length() <= 255;
    }

    public static boolean isValidNombreEquipo(String nombreEquipo) {
        return !TextUtils.isEmpty(nombreEquipo) && nombreEquipo.length() <= 255;
    }

    public static boolean isValidNumeroOrden(String numeroOrden) {
        return !TextUtils.isEmpty(numeroOrden) && numeroOrden.length() <= 255 && Pattern.matches("^[A-Za-z0-9]+$", numeroOrden);
    }

    public static boolean isValidSerie(String serie) {
        return !TextUtils.isEmpty(serie) && serie.length() <= 255 && Pattern.matches("^[A-Za-z0-9]+$", serie);
    }
}
