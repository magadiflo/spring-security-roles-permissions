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