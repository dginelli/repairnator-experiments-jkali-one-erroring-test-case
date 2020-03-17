# DesarrolloAplicacionesWeb
Desarrollo de Aplicaciones Web | GIS | Universidad Rey Juan Carlos 2017/2018

[![Build Status](https://travis-ci.org/Hgmoa/DesarrolloAplicacionesWeb.svg?branch=master)](https://travis-ci.org/Hgmoa/DesarrolloAplicacionesWeb)
## FASE 1

### 1. Nombre de la aplicacion Web: OnControlHome [OCH] 

### 2. Descripcion: 
Aplicacion Web, para la compra de un servicio con el cual puedes controlar las persiana y lamparas de una vivienda.   
Pudiendo según el servicio, subir y bajar las persianas y apagar o encender la luz, desde cualquier dispositivo movil y computadoras.     
Pudiendo visualizar el consumo energetico.

### 3. Integrantes del Equipo de Desarrollo:  
| Nombre | Mail | GitHub     |
| --- | --- | --- |
| Galindo Peña, Jorge        | j.galindop@alumnos.urjc.es      | [JorgeGalingP](https://github.com/JorgeGalingP) |
| Macias Medina, Daniel      | d.maciasm@alumnos.urjc.es       | [DaniMaci](https://github.com/DaniMaci) |
| Serrano Rodriguez, Ramon   | r.serranoro@alumnos.urjc.es     | [RamonSpain](https://github.com/RamonSpain) |
| Velazquez Sanchez, Cristian  | c.velazquezs@alumnos.urjc.es    | [CVelazquezURJC](https://github.com/VelazquezURJC) |
| Vizcaino Santana, Hugo Samuel | hs.vizcaino@alumnos.urjc.es     | [Hgmoa](https://github.com/Hgmoa) |

### 4. Tablero de organizacion: 
https://github.com/Hgmoa/DesarrolloAplicacionesWeb/projects/1

### 5. Secciones:

####   - Tablas/Entidades:
         - Usuario: Guarda los datos de cada usuario.     
         - Casa: Direccion exacta y codigo postal.    
         - Dispositivos: Cantidad y Tipos de dispositivos.    
         - Mediciones: Guarda los datos de valor y fecha para realizar graficos.    

####   - Funcionalidad Avanzada:
         - Mediciones y comparativas por zona (cofigo postal) en gasto.(Presentacion en grafica)    
         - Aviso al usuario en caso de uso excesivo.    

####   - Requisitos Tecnicos:
         - Mostrar consumo de usuario por grafico de barras o puntos.    
         - Gestion de imagenes de usuarios (avatar).    
         - Social Login. (Facebook, Google)    
         - API externa del tiempo.    
         - Descarga de datos en PDF.    
    
## FASE 2
### 1.Capturas de Pantalla 
SIN REGISTRAR
![index](/pantalalzosFase2/cliente/index.jpg "Index")     
![Nuestro Trabajo](/pantalalzosFase2/cliente/nuestroTrabajo.jpg "Nuestro Trabajo")  
![Productos](/pantalalzosFase2/cliente/Productos.jpg "Productos")  
Pagina principal, visualizacion para cualquier usuario, pudiendo ver apartados como, caracteristicas, quienes somo, tansolo con bajar con el raton, como pulsando el los propios nombres.(ejemplo nuestro trabajo y productos).
    
![Login](/pantalalzosFase2/cliente/login.jpg "Login")
Para poder entrarcomo usuario, pudiendo recordar contraseña, o registrate.

![Recordar contraseña](/pantalalzosFase2/cliente/RecuerdoContrasena.jpg "Recordar Contraseña")     
Para introducir el correo y que te recuerden la contraseña.

![Registro](/pantalalzosFase2/cliente/Registro.jpg "Registro")     
Página donde deves rellenar los datos más caracteristicos para registrarte en la página, puedes hacerlo via facebook, google...

![Error 404](/pantalalzosFase2/cliente/Error404.jpg "Error 404")     
Página cuando hay error 404

![Error 500](/pantalalzosFase2/cliente/Error500.jpg "Error 500")     
Página cuando hay error 500

CON REGISTRO

![index](/pantalalzosFase2/dasboard/IndexDasboard.png "Index") 
Desde esta pantalla controlaremos lo general de la casa. Luces persianas,...

![profile](/pantalalzosFase2/dasboard/UserDasboard.png "profile") 
Perfil del cliente que está dado de alta en nuestro servicio con información general sobre el.

![forms](/pantalalzosFase2/dasboard/FormDasboard.png "forms")
Formularios para las diferentes aspectos de la aplicación.

![charts](/pantalalzosFase2/dasboard/GraficDasboard.png "charts")
Graficas que muestran el consumo de la casa.

![homes](/pantalalzosFase2/dasboard/HousesDasboard.png "homes")
Consumo de las diferentes casas que pueda tener un mismo usuario

![terms-conditions](/pantalalzosFase2/dasboard/Term.png "terms-conditions")
Terminios y condiciones legales



### 2. Diagrama de navegación
Cliente sin registrar:
![Navegacion sin registro](/pantalalzosFase2/cliente/sinRegistrar.jpg "Esquema sin registrar") 

Cliente registrado:
![Navegacion con registro](/pantalalzosFase2/dasboard/NavegacionDasboard.jpg "Esquema con registrar") 
