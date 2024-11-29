const express = require("express")
const mysql = require("mysql2")
const bodyParser = require("body-parser")
const bcrypt = require("bcrypt")

const app = express()
const PUERTO = 3000

app.use(bodyParser.json())

const conexion = mysql.createConnection(
    {
        host:'localhost',
        database:'db_atec',
        user:'root',
        password:'caltn2023.',
       
    }
)

app.listen(PUERTO, () =>{
    console.log("Servidor corriendo en el puerto "+PUERTO)
})

conexion.connect(error =>{
    if(error) throw error
    console.log("Conexión existosa a la base de datos")
})

app.get("/", (req, res) =>{
    res.send("Bienvenido a mi Web Service")
})


app.get("/usuarios", (req, res)=>{
    const consulta = "SELECT * FROM usuarios";
    conexion.query(consulta, (error, rpta)=>{
        if(error) return console.error(error.message)
        
        const obj = {}
        if(rpta.length > 0){
            obj.listaUsuarios = rpta
            res.json(obj)
        }else{
            res.json("No hay registros")
        }
    })
})

//CALIFICAR ESTUDIANTE
app.get("/estudiantes", (req, res) => {
    const consulta = `
        SELECT 
            usu_id, 
            usu_nombres, 
            usu_apellidos,
            programa_id 
        FROM usuarios 
        WHERE rol_id = 3
    `;
    conexion.query(consulta, (error, resultados) => {
        if (error) {
            console.error("Error al obtener estudiantes:", error.message);
            return res.status(500).json({ error: "Error al obtener estudiantes" });
        }

        if (resultados.length > 0) {
            res.status(200).json({ listaEstudiantes: resultados });
        } else {
            res.status(404).json({ message: "No hay estudiantes registrados" });
        }
    });
});

// LISTA DE ESTUDIANTES
app.get("/lista/estudiantes", (req, res) => {
    const consulta = `
        SELECT 
            usuarios.usu_codigo, 
            usuarios.usu_nombres, 
            usuarios.usu_apellidos,
            programas_estudio.nombre_programa AS nombrePrograma
        FROM usuarios
        INNER JOIN programas_estudio ON usuarios.programa_id = programas_estudio.programa_id
        WHERE usuarios.rol_id = 3;
    `;

    conexion.query(consulta, (error, resultados) => {
        if (error) {
            console.error("Error al obtener estudiantes:", error.message);
            return res.status(500).json({ error: "Error al obtener estudiantes" });
        }

        // Retornar una lista vacía en lugar de un 404
        res.status(200).json({ listaEstudiantesDoc: resultados.length > 0 ? resultados : [] });
    });
});




// Ruta para el registro de usuarios
app.post("/register", (req, res) => {
    const { usu_dni, usu_nombres, usu_apellidos, usu_codigo, usu_clave, usu_correo, usu_sexo, usu_telefono, rol_id, programa_id } = req.body;

    let programa = (rol_id === 1) ? null : programa_id; // Si es Admin, no asignamos programa_id

    // Función para verificar la existencia de un valor en una columna específica
    const checkDuplicate = (column, value) => {
        return new Promise((resolve, reject) => {
            const query = `SELECT COUNT(*) AS count FROM usuarios WHERE ${column} = ?`;
            conexion.query(query, [value], (error, results) => {
                if (error) {
                    reject(error);
                } else {
                    resolve(results[0].count > 0);
                }
            });
        });
    };

    // Verificar duplicados
    Promise.all([
        checkDuplicate('usu_codigo', usu_codigo),
        checkDuplicate('usu_correo', usu_correo),
        checkDuplicate('usu_dni', usu_dni) // Verificar si el DNI ya existe
    ]).then(([codigoExists, correoExists, dniExists]) => {
        if (codigoExists || correoExists || dniExists) {
            let errorMessage = "Datos ya registrados: ";
            if (codigoExists) errorMessage += "Codigo ";
            if (correoExists) errorMessage += "Correo ";
            if (dniExists) errorMessage += "DNI ";
            return res.status(409).json({ error: errorMessage.trim() });
        }

        // Generar el hash de la contraseña usando bcrypt
        bcrypt.hash(usu_clave, 10, (err, hashedPassword) => {
            if (err) {
                console.error('Error al generar el hash de la contraseña:', err);
                res.status(500).json({ error: 'Error interno del servidor' });
            } else {
                // Insertar usuario en la base de datos con la contraseña hasheada
                const query = "INSERT INTO usuarios (usu_dni, usu_nombres, usu_apellidos, usu_codigo, usu_clave, usu_correo, usu_sexo, usu_telefono, rol_id, programa_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                conexion.query(query, [usu_dni, usu_nombres, usu_apellidos, usu_codigo, hashedPassword, usu_correo, usu_sexo, usu_telefono, rol_id, programa_id], (error) => {
                    if (error) {
                        console.error('Error al insertar usuario en la base de datos:', error);
                        res.status(500).json({ error: 'Error interno del servidor' });
                    } else {
                        res.status(200).json({ message: 'Usuario registrado correctamente' });
                    }
                });
            }
        });
    }).catch((error) => {
        console.error('Error en la verificación de duplicados:', error);
        res.status(500).json({ error: 'Error interno del servidor' });
    });
});



// LOGIN DE USUARIOS
app.post("/login", (req, res) => {
    const { usu_codigo, usu_clave } = req.body;

    if (!usu_codigo || !usu_clave) {
        return res.status(400).json({ error: "Faltan completar datos" });
    }

    const query = "SELECT * FROM usuarios WHERE usu_codigo = ?";
    conexion.query(query, [usu_codigo], (error, results) => {
        if (error) {
            console.error("Error en la consulta SQL:", error.message);
            return res.status(500).json({ error: error.message });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: "Usuario no encontrado, regístrese" });
        }

        const user = results[0];

        // Primero intentamos comparar usando bcrypt
        bcrypt.compare(usu_clave, user.usu_clave, (err, result) => {
            if (err) {
                return res.status(500).json({ error: err.message });
            }

            if (result) {
                // Contraseña encriptada coincide
                return res.json({ message: "Login exitoso", rol_id: user.rol_id });
            }

            // Si bcrypt falla, verificar si la contraseña es sin encriptar (texto plano)
            if (usu_clave === user.usu_clave) {
                return res.json({ message: "Login exitoso (sin encriptación)", rol_id: user.rol_id });
            }

            // Si ninguna de las comparaciones es correcta
            return res.status(401).json({ error: "Datos incorrectos" });
        });
    });
});

// Ruta para buscar un estudiante por su código

app.get("/buscar", (req, res) => {
    const { usu_codigo } = req.query; // Obtener el código del estudiante desde los parámetros de consulta

    if (!usu_codigo) {
        return res.status(400).json({ error: "Código de estudiante requerido" });
    }

    // Modificación en la consulta SQL para usar INNER JOIN y alias con AS
    const query = `
        SELECT u.*, p.nombre_programa AS nombrePrograma
        FROM usuarios u
        INNER JOIN programas_estudio p ON u.programa_id = p.programa_id 
        WHERE u.usu_codigo = ?`;

    conexion.query(query, [usu_codigo], (error, results) => {
        if (error) {
            console.error("Error en la consulta SQL:", error.message);
            return res.status(500).json({ error: "Error en el servidor" });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: "Estudiante no encontrado" });
        }

        // Aquí, agregamos el nombre del programa al objeto estudiante con el alias
        const estudiante = {
            ...results[0],
            programa: {
                programa_id: results[0].programa_id,
                nombrePrograma: results[0].nombrePrograma  // Usando el alias 'nombrePrograma'
            }
        };

        // Enviar la información del estudiante como respuesta JSON
        res.json(estudiante);
    });
});



// Ruta para actualizar la información de un estudiante
app.put("/estudiante/actualizar/:usu_codigo", (req, res) => {
    const { usu_codigo } = req.params; // Código del estudiante a actualizar
    const { usu_dni, usu_nombres, usu_apellidos, usu_clave, usu_correo, usu_sexo, usu_telefono, programa_id } = req.body; // Nuevos datos

    // Validar que los datos necesarios estén presentes
    if (!usu_codigo) {
        return res.status(400).json({ error: "El código del estudiante es requerido" });
    }

    // Consulta SQL para actualizar la información
    const query = `
        UPDATE usuarios 
        SET 
            usu_dni = ?,
            usu_nombres = ?, 
            usu_apellidos = ?,
            usu_clave = ?, 
            usu_correo = ?, 
            usu_telefono = ?, 
            usu_sexo = ?, 
            programa_id = ?
        WHERE usu_codigo = ?;
    `;

    // Ejecutar la consulta con los nuevos datos
    conexion.query(query, [usu_dni, usu_nombres, usu_apellidos, usu_clave, usu_correo, usu_telefono, usu_sexo, programa_id, usu_codigo], (error, results) => {
        if (error) {
            console.error("Error al actualizar el estudiante:", error.message);
            return res.status(500).json({ error: "Error al actualizar la información" });
        }

        // Si se actualizó al menos un registro
        if (results.affectedRows > 0) {
            return res.json({ message: "Estudiante actualizado correctamente" });
        } else {
            return res.status(404).json({ error: "Estudiante no encontrado o no se registró" });
        }
    });
});

app.delete("/estudiante/eliminar/:usu_codigo", (req, res) => {
    const { usu_codigo } = req.params;
    const query = "DELETE FROM usuarios WHERE usu_codigo = ?";

    conexion.query(query, [usu_codigo], (error, results) => {
        if (error) {
            console.error("Error al eliminar estudiante:", error.message);
            res.status(500).json({ error: "Error al eliminar el estudiante." });
        } else if (results.affectedRows === 0) {
            res.status(404).json({ error: "Estudiante no encontrado." });
        } else {
            res.json("Se eliminó el estudiante correctamente.");
        }
    });
});

//

app.get("/horarios", (req, res) => {
    const query = `
        SELECT 
            horarios.horario_id AS id, 
            horarios.dia, 
            horarios.hora_inicio, 
            horarios.hora_fin, 
            cursos.nombre_curso AS nombreCurso
        FROM horarios
        INNER JOIN cursos ON horarios.curso_id = cursos.curso_id;
    `;
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            res.status(500).send("Error en la consulta de horarios");
            return;
        }
        
        res.json({ listaHorarios: resultado.length > 0 ? resultado : "No hay registros" });
    });
});

app.get("/cursos", (req, res) => {
    const query = `
        SELECT
        cursos.curso_id,
        cursos.nombre_curso,
        programas_estudio.nombre_programa AS nombrePrograma
        FROM cursos
        INNER JOIN programas_estudio ON cursos.programa_id = programas_estudio.programa_id;
    `;
    
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            res.status(500).send("Error en la consulta de cursos");
            return;
        }
        
        res.json({ listaCursos: resultado.length > 0 ? resultado : "No hay registros" });
    });
});

// Ruta para obtener los certificados de un estudiante específico
app.get("/certificados", (req, res) => {
    const query = `
        SELECT
        certificados.certificado_id,
        usuarios.usu_codigo AS estudianteCodigo,
        cursos.nombre_curso AS estudianteCurso, 
        certificados.fecha_emision,
        certificados.certificado_url
        FROM certificados
        INNER JOIN usuarios ON certificados.usu_id = usuarios.usu_id
        INNER JOIN cursos ON certificados.curso_id = cursos.curso_id;
    `;
    
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            res.status(500).send("Error en la consulta de certificados");
            return;
        }
        
        res.json({ listaCertificados: resultado.length > 0 ? resultado : "No hay registros" });
    });
});

// Ruta para obtener los programas de estudio
app.get("/programas", (req, res) =>{
    const query = "SELECT * FROM programas_estudio;"
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length>0){
            objeto.listaProgramas = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros")
        }
    })
})

// Ruta para obtener los programas de estudio
app.get("/inscripciones", (req, res) =>{
    const query = "SELECT * FROM inscripciones;"
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length>0){
            objeto.listaInscipciones = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros")
        }
    })
})


// Register Programas de Estudio

app.post("/register/programas", (req, res) => {
    const { nombre_programa } = req.body;

    if (!nombre_programa) {
        return res.status(400).json({ error: "El nombre del programa es obligatorio" });
    }

    // Verificar si el programa ya existe
    const checkQuery = "SELECT COUNT(*) AS count FROM programas_estudio WHERE nombre_programa = ?";
    conexion.query(checkQuery, [nombre_programa], (error, results) => {
        if (error) {
            console.error("Error al verificar duplicados:", error.message);
            return res.status(500).json({ error: "Error interno del servidor" });
        }

        if (results[0].count > 0) {
            return res.status(409).json({ error: "El programa ya está registrado" });
        }

        // Insertar el nuevo programa
        const insertQuery = "INSERT INTO programas_estudio (nombre_programa) VALUES (?)";
        conexion.query(insertQuery, [nombre_programa], (error, results) => {
            if (error) {
                console.error("Error al insertar el programa:", error.message);
                return res.status(500).json({ error: "Error interno del servidor" });
            }

            res.status(201).json({ 
                message: "Programa de Estudio registrado correctamente.", 
                programa_id: results.insertId 
            });
        });
    });
});

// Register Cursos

app.post("/register/cursos", (req, res) => {
    const { nombre_curso, programa_id } = req.body; // Obtener datos del cuerpo de la solicitud

    // Validar que los datos requeridos estén presentes
    if (!nombre_curso || !programa_id) {
        return res.status(400).json({ error: "El nombre del curso y el ID del programa son obligatorios" });
    }

    // Verificar que el programa_id exista en la tabla programas_estudio
    const checkProgramaQuery = "SELECT COUNT(*) AS count FROM programas_estudio WHERE programa_id = ?";
    conexion.query(checkProgramaQuery, [programa_id], (error, results) => {
        if (error) {
            console.error("Error al verificar el programa:", error.message);
            return res.status(500).json({ error: "Error interno del servidor" });
        }

        if (results[0].count === 0) {
            return res.status(404).json({ error: "El programa especificado no existe" });
        }

        // Verificar si ya existe un curso con el mismo nombre en el programa
        const checkCursoQuery = "SELECT COUNT(*) AS count FROM cursos WHERE nombre_curso = ? AND programa_id = ?";
        conexion.query(checkCursoQuery, [nombre_curso, programa_id], (error, results) => {
            if (error) {
                console.error("Error al verificar el curso:", error.message);
                return res.status(500).json({ error: "Error interno del servidor" });
            }

            if (results[0].count > 0) {
                return res.status(409).json({ error: "El curso ya existe en este programa" });
            }

            // Insertar el curso en la tabla cursos
            const insertCursoQuery = "INSERT INTO cursos (nombre_curso, programa_id) VALUES (?, ?)";
            conexion.query(insertCursoQuery, [nombre_curso, programa_id], (error, results) => {
                if (error) {
                    console.error("Error al insertar el curso:", error.message);
                    return res.status(500).json({ error: "Error interno del servidor" });
                }

                res.status(201).json({
                    message: "Curso registrado correctamente.",
                    curso_id: results.insertId,
                });
            });
        });
    });
});

// Ruta para registrar inscripciones
app.post("/register/inscripciones", (req, res) => {
    const { usu_id, programa_id, estado } = req.body; // Obtener datos del cuerpo de la solicitud

    // Validar que los datos requeridos estén presentes
    if (!usu_id || !programa_id || !estado) {
        return res.status(400).json({ error: "Faltan datos." });
    }

    // Verificar si el usuario existe en la base de datos
    const checkUserQuery = "SELECT COUNT(*) AS count FROM usuarios WHERE usu_id = ?";
    conexion.query(checkUserQuery, [usu_id], (error, results) => {
        if (error) {
            console.error("Error al verificar el usuario:", error.message);
            return res.status(500).json({ error: "Error interno del servidor" });
        }

        if (results[0].count === 0) {
            return res.status(404).json({ error: "El usuario no existe" });
        }

        // Verificar si el programa existe en la base de datos
        const checkProgramaQuery = "SELECT COUNT(*) AS count FROM programas_estudio WHERE programa_id = ?";
        conexion.query(checkProgramaQuery, [programa_id], (error, results) => {
            if (error) {
                console.error("Error al verificar el programa:", error.message);
                return res.status(500).json({ error: "Error interno del servidor" });
            }

            if (results[0].count === 0) {
                return res.status(404).json({ error: "El programa no existe" });
            }

            // Verificar si ya existe una inscripción para ese usuario en ese programa
            const checkInscripcionQuery = "SELECT COUNT(*) AS count FROM inscripciones WHERE usu_id = ? AND programa_id = ?";
            conexion.query(checkInscripcionQuery, [usu_id, programa_id], (error, results) => {
                if (error) {
                    console.error("Error al verificar la inscripción existente:", error.message);
                    return res.status(500).json({ error: "Error interno del servidor" });
                }

                if (results[0].count > 0) {
                    return res.status(409).json({ error: "Este estudiante ya está calificado" });
                }

                // Insertar la nueva inscripción
                const insertQuery = "INSERT INTO inscripciones (usu_id, programa_id, estado) VALUES (?, ?, ?)";
                conexion.query(insertQuery, [usu_id, programa_id, estado], (error, results) => {
                    if (error) {
                        console.error("Error al insertar la inscripción:", error.message);
                        return res.status(500).json({ error: "Error interno del servidor" });
                    }

                    res.status(201).json({
                        message: "Inscripción de nota registrada correctamente.",
                        inscripcion_id: results.insertId, // Retorna el ID de la inscripción registrada
                    });
                });
            });
        });
    });
});

// CLASES

app.get("/clases", (req, res) => {
    const query = `
      SELECT 
        p.nombre_programa,
        c.nombre_curso,
        h.dia,
        h.hora_inicio,
        h.hora_fin
      FROM horarios h
      INNER JOIN cursos c ON h.curso_id = c.curso_id
      INNER JOIN programas_estudio p ON c.programa_id = p.programa_id;
    `;
  
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            res.status(500).send("Error en la consulta de cursos");
            return;
        }
        
        res.json({ listaClases: resultado.length > 0 ? resultado : "No hay registros" });
    });
  });
