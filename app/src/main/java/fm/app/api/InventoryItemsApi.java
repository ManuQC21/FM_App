package fm.app.api;

import java.util.List;
import fm.app.Entity.InventoryItems;
import fm.app.Entity.GenericResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface InventoryItemsApi {
    // Ruta base para todos los endpoints de la API de Artículos de Inventario
    String base = "/inventoryItems";

    // Método para agregar un artículo de inventario
    @POST(base + "/agregar")
    Call<GenericResponse<InventoryItems>> addInventoryItem(@Body InventoryItems inventoryItems);

    // Método para listar todos los artículos de inventario
    @GET(base + "/listar")
    Call<GenericResponse<List<InventoryItems>>> listAllInventoryItems();

    // Método para generar código de barras basado en el código patrimonial
    @GET(base + "/generarCodigoBarra/{codigoPatrimonial}")
    Call<GenericResponse<byte[]>> generateBarcode(@Path("codigoPatrimonial") String codigoPatrimonial);
}
