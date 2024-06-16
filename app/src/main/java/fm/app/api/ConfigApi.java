package fm.app.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {

    // Define las URLs base para diferentes entornos, manteniendo las alternativas como comentarios
    //public static final String BASE_URL = "http://10.0.2.2:8080"; // Emulador Android Studio
    //public static final String BASE_URL = "http://192.168.1.122:8080"; // URL principal
    public static final String BASE_URL = "http://192.168.0.14:8080"; // Otra IP de red local

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/MM/yyyy")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(new StethoInterceptor()) // Herramienta de debug de Facebook
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    // Ejemplos de cómo obtener las APIs
    public static UsuarioApi getUsuarioApi() {
        return getRetrofitInstance().create(UsuarioApi.class);
    }

    public static EquipoApi getEquipoApi() {
        return getRetrofitInstance().create(EquipoApi.class);
    }

    public static EmpleadoApi getEmpleadoApi() {
        return getRetrofitInstance().create(EmpleadoApi.class);
    }

    public static UbicacionApi getUbicacionApi() {
        return getRetrofitInstance().create(UbicacionApi.class);
    }
}
