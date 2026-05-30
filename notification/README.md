# Notificaciones — CronoEdu

Microservicio de notificaciones con arquitectura limpia (3 capas) y envío de
correo electrónico (JavaMail) + SMS (Twilio).

---

## Endpoint disponible

```
POST /api/notificacion/enviar
Content-Type: application/json

{
  "tipo": "EMAIL",
  "email": "usuario@mail.com",
  "telefono": null,
  "mensaje": "Bienvenido a CronoEdu"
}
```

### Campos del body

| Campo     | Tipo     | Obligatorio | Descripción                     |
|-----------|----------|-------------|---------------------------------|
| `tipo`    | `String` | ✅          | `"EMAIL"` o `"SMS"`            |
| `email`   | `String` | si EMAIL    | Correo del destinatario         |
| `telefono`| `String` | si SMS      | Número del destinatario         |
| `mensaje` | `String` | ✅          | Contenido del mensaje           |

---

## Cómo consumirlo desde otro MS (ej. Auth)

### Paso 1 — Puerto en el dominio del MS consumidor

`com.CronoEdu.auth.domine.model.gateway.NotificacionGateway.java`

```java
public interface NotificacionGateway {
    void enviarNotificacion(String tipo, String email, String telefono, String mensaje);
}
```

### Paso 2 — Adaptador REST en infraestructura

`com.CronoEdu.auth.infraestructure.adapters.NotificacionRestAdapter.java`

```java
@Service
public class NotificacionRestAdapter implements NotificacionGateway {

    private final RestTemplate restTemplate;

    public NotificacionRestAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void enviarNotificacion(String tipo, String email, String telefono, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("tipo", tipo);
        body.put("email", email);
        body.put("telefono", telefono);
        body.put("mensaje", mensaje);

        restTemplate.postForEntity("http://localhost:8081/api/notificacion/enviar", body, Void.class);
    }
}
```

### Paso 3 — Inyectar en el UseCase del MS consumidor

```java
public class AuthUsecase {
    private final NotificacionGateway notificacionGateway;

    public void registrar(Usuario usuario) {
        // ... lógica de registro ...
        notificacionGateway.enviarNotificacion(
            "EMAIL", usuario.getEmail(), null, "Cuenta creada exitosamente"
        );
    }
}
```

### Paso 4 — Configurar RestTemplate

`com.CronoEdu.auth.application.config.HttpClientConfig.java`

```java
@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### Paso 5 — Dependencia en el build.gradle del MS consumidor

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
```

---

## Diagrama de flujo entre microservicios

```
Auth MS (dominio)
  └── NotificacionGateway (interface — puerto de salida)
        ↓
Auth MS (infraestructura)
  └── NotificacionRestAdapter (implementación — HTTP client)
        ↓  POST /api/notificacion/enviar
Notification MS (infraestructura)
  └── NotificacionController (entry point REST)
        ↓
Notification MS (dominio)
  └── NotificacionUsecase
        ↓
Notification MS (infraestructura)
  ├── EmailJavaMailSender  (EMAIL)
  └── SmsTwilioSender (SMS)
```

---

---

## Configuración en application.properties

```properties
server.port=8081

# Email SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=correo@gmail.com
spring.mail.password=contraseña

# Twilio
twilio.account-sid=ACxxxxxxxxxx
twilio.auth-token=xxxxxxxxxx
twilio.phone-number=+12025551234
```

---

## Estructura del proyecto

```
com.CronoEdu.notification/
├── domine/                       ← capa dominio
│   ├── model/gateway/
│   │   ├── EmailGateway.java    ← puerto para email
│   │   └── SmsGateway.java      ← puerto para SMS
│   └── usecase/
│       └── NotificacionUsecase.java
├── application/config/
│   └── UseCaseConfig.java       ← beans del dominio
└── infraestructure/              ← capa infraestructura
    ├── entry_points/
    │   └── NotificacionController.java
    ├── email/
    │   └── EmailJavaMailSender.java
    ├── sms/
    │   └── SmsTwilioSender.java
    └── evento/
        ├── NotificacionListener.java
        └── dto/
            └── NotificacionEventoDTO.java
```

---

## Dependencias (build.gradle)

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-mail'
    implementation 'com.twilio.sdk:twilio:10.6.6'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```
