package com.example.etacproyect1.servicio


import com.example.etacproyect1.entidades.Certificado
import com.example.etacproyect1.entidades.Curso
import com.example.etacproyect1.entidades.Estudiante
import com.example.etacproyect1.entidades.Horario
import com.example.etacproyect1.entidades.ListaEstudianteDoc
import com.example.etacproyect1.entidades.LoginUsuario
import com.example.etacproyect1.entidades.RegistroCurso
import com.example.etacproyect1.entidades.RegistroInscripcion
import com.example.etacproyect1.entidades.RegistroPrograma
import com.example.etacproyect1.entidades.RegistroUsuario
import com.example.etacproyect1.entidades.Usuario
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Define las constantes de la aplicación
object AppConstantes {
    const val BASE_URL = "http://192.168.18.50:3000"
}


// Define la interfaz del Web Service
interface WebService {

    @POST("/login")
    suspend fun loginUsuario(@Body loginUsuario: LoginUsuario): Response<LoginResponse>

    @POST("/register")
    suspend fun registroUsuario(@Body registroUsuario: RegistroUsuario): Response<RegistroResponse>

    // Muscar un estudiante por su código
    @GET("/buscar")
    suspend fun buscarEstudiante(@Query("usu_codigo") codigo: String): Response<Estudiante>

    @PUT("/estudiante/actualizar/{usu_codigo}")
    suspend fun actualizarEstudiante(@Path("usu_codigo") codigo: String, @Body usuario: Usuario): Response<ActualizarEstudianteResponse>

    @DELETE("/estudiante/eliminar/{usu_codigo}")
    suspend fun eliminarEstudiante(@Path("usu_codigo") codigo: String): Response<String>

    @GET("/horarios")
    suspend fun obtenerHorarios(): Response<HorarioResponse>

    @GET("/cursos")
    suspend fun obtenerCursos(): Response<CursoResponse>

    @GET("/certificados")
    suspend fun obtenerCertificados(): Response<CertificadoResponse>

    @GET("/programas")
    suspend fun obtenerProgramas(): Response<ProgramaResponse>

    @POST("/register/programas")
    suspend fun registroPrograma(@Body registroPrograma: RegistroPrograma): Response<RegistroProgramaResponse>

    @POST("/register/cursos")
    suspend fun registroCurso(@Body registroCurso: RegistroCurso): Response<RegistroCursoResponse>

    @POST("/register/inscripciones")
    suspend fun registroInscripcion(@Body registroInscripcion: RegistroInscripcion): Response<RegistroInscripcionResponse>

    @GET("/estudiantes")
    suspend fun obtenerEstudiantes(): Response<EstudianteResponse>

    @GET("/lista/estudiantes")
    suspend fun obtenerListaEstudiantes(): Response<ListaEstudianteResponse>

    @GET("/clases")
    suspend fun obtenerClases(): Response<ClaseResponse>

}

// Configura el cliente de Retrofit
object RetrofitCliente {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}