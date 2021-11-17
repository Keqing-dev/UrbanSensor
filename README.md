# UrbanSensor

## Preparación de Environment:

Para correr el proyecto primero se deben generar las claves RSA las cuales se usan para generar y validar los token JWT. Las claves se generan ejecutando los siguientes comandos
en el directorio root del proyecto:

```console
foo@bar:~$ openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:4096 -out private-key.pem
```

y luego

```console
foo@bar:~$ openssl pkey -in private-key.pem -out public-key.pem -pubout
```

Una vez generadas las claves se debe proceder a declarar las variables de entorno. Para esto en el directorio `src/main/java/resources` se debe crear un archivo llamado
`application-prod.yaml` y debe contener las siguientes variables dentro:

```yaml
spring:
  datasource:
    username: USERNAME_DE_LA_BD_A_OCUPAR
    password: PASSWORD_DE_LA_BD_A_OCUPAR
    url: jdbc:postgresql://IP_DE_LA_BD_A_OCUPAR:5432/NOMBRE_DE_LA_BD_A_OCUPAR
  sql:
    init:
      mode:
      continue-on-error: true
springdoc:
  swagger-ui:
    enabled: false

config:
  domain-name: DOMINIO_DEL_SITIO(https://example.com o http://localhost:8080)
```

Reemplazar las credenciales de base de datos y dominio del sitio y la aplicación estará lista para ejecutarse

## Ejecución de la aplicación:

Para correr la aplicación ya con todo configurado se debe ejecutar el comando

```console
foo@bar:~$ ./gradlew bootRun
```

En caso de estar en un sistema UNIX y que diga que el comando no es reconocido
es necesario ejecutar el siguiente comando

```console
foo@bar:~$ chmod +x ./gradlew
```
 y luego ejecutar nuevamente el run.
 
 ## Build de la Aplicación:
 
 Para generar la build de la aplicación es necesario ejecutar el comando
 
 ```console
foo@bar:~$ ./gradlew build
```
y se generará un directorio `build`. Dentro del mismo directorio habrá otro directorio llamando `libs` donde se encontrará el .jar para ejecutar el proyecto.

Si se desea ejecutar el .jar es necesario ejecutar el siguiente comando

```console
foo@bar:~$ java -jar build/libs/NOMBRE_DEL_JAR_GENERADO.jar
```

Con esto la aplicación ya estaría totalmente operativa y funcional.
