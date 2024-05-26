package fm.app.api;

import java.util.List;

import fm.app.entity.GenericResponse;
import fm.app.entity.service.Empleado;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EmpleadoApi {
    @GET("empleados/listar")
    Call<GenericResponse<List<Empleado>>> listarEmpleados();
}

