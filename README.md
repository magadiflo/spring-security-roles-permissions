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

