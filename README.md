# Spring Security – Roles and Privileges
Tutorial tomado de la página [baeldung](https://www.baeldung.com/role-and-privilege-for-spring-security-registration?ref=morioh.com&utm_source=morioh.com)  
Inicialmente se encontró el tutorial en la página [morioh.com](https://morioh.com/p/96e176658ab1)
--- 
**NOTA:** en este proyecto tomaremos como sinónimos los privileges, permissions y authorities   
`privileges == permissions == authorities`  
Pero se trabajará con **permissions (permisos)** aunque en el tutorial tomado lo trabajan como **privileges (privilegios)**
---

## Usuario, rol y permiso (privilegio)
Tenemos 3 entidades principales:  
* El usuario 
* El rol representa los roles de alto nivel del usuario en el sistema. Cada rol tendrá un conjunto de permisos (privilegios) de bajo nivel.
* El permiso representa un permiso/privilegio/autoridad granular de bajo nivel en el sistema.

## Ejecutar código justo después del inicio de la aplicación
En este proyecto se usa el detector de eventos o ApplicationListener, junto al ContextRefreshedEvent.

El ContextRefreshEvent se genera cuando se inicializa o actualiza un contexto de aplicación, 
lo que significa que el método onApplicationEvent(...) se puede ejecutar más de una vez. Por lo tanto, 
es posible que deba poner un tipo de estado en su componente para asegurarse de que el código 
de inicialización se ejecute solo una vez. 

En nuestro caso usamos una bandera "alreadySetup" para controlar que solo se ejecute una vez
el código dentro del onApplicationEvent(...).

Esta carga de datos iniciales lo implementamos en package setup, clase SetupDataLoader

Existe otras formas, como la de usar el **CommandLineRunner**, pero en este proyecto usamos el ApplicationListener.

[Para más información ir click a este enlace](https://www.codejava.net/frameworks/spring-boot/run-code-on-application-startup)

## Se usó flatMap para generar un apalanado de stream
Revisar el siguiente link para mayor información sobre el [flatMap](https://www.delftstack.com/es/howto/java/flatmap-in-java/#:~:text=La%20funci%C3%B3n%20flatMap%20en%20Java,-La%20firma%20de&text=flatMap%20es%20una%20operaci%C3%B3n%20intermedia,map()%20y%20flat()%20.)

# Cómo probar el proyecto
- Tenemos los siguientes **usuarios** sus **roles** y **permisos**  
![img_1.png](img.png)

- Como estamos usando la autenticación via Formulario, mediante el navegador
  accedemos a la URL. Ingresamos algún usuario con el que queramos hacer pruebas 
  y nos logueamos (La pass para todos es **test**). Al hacerlo mostrará una 
  pantalla de error, y eso es porque no tenemos una página principal. 
  Solo usaremos el login para obtener el **JSESSIONID**, nos vamos a inspeccionar/Applicación/Cookies
    ```
    http://localhost:8080/login
    ```
- Si ya estamos logueados y queremos y queremos iniciar sesión nuevamente, debemos desloguearnos solo
  accediendo al a url /logout
    ```
    http://localhost:8080/logout
    ```
- Ya con el **JSESSIONID** correspondiente al usuario seleccionado, nos dirigimos a **POSTMAN**, realizamos
  cualquier petición, en el apartado de resultados nos mostrará un formulario para iniciar sesión (formato html),
  y en los Headers aparecerá una key **Cookie** que no puede ser descheckada, pero en la parte izquierda nos 
  vamos a **go to cookies**, se abrirá una ventana con las cookies, clickamos en **JSESSIONID**
  y cambiamos ese ID por el nuestro.
- Finalmente, así debería quedar
    ```
    Key: Cookie
    Value: JSESSIONID=D0DEA0C561D2592874E70A36DD21A5DF
    ```
- Realizamos la petición y observamos los resultados, estos dependerán del usuario sus roles y permisos
