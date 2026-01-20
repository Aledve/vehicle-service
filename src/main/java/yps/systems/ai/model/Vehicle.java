package yps.systems.ai.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

@Node("Vehicle") // Etiqueta corregida para la base de datos
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class Vehicle {

    @Id
    @GeneratedValue
    private String elementId;

    // La placa es el identificador visual principal (Ej: PAB-1234)
    @Property("plate")
    private String plate;

    // Marca del vehículo (Ej: Chevrolet, Toyota)
    @Property("brand")
    private String brand;

    // Modelo específico (Ej: Sail, Yaris)
    @Property("model")
    private String model;

    // Color, útil para identificación visual por seguridad
    @Property("color")
    private String color;

    // Tipo de vehículo para aplicar tarifas diferenciadas (Ej: AUTOMOVIL, MOTO)
    @Property("type")
    private String type;

    // Fecha de registro en el sistema (reemplaza a startDate)
    @Property("registrationDate")
    private LocalDate registrationDate;

    // Estado para permitir o denegar acceso (Activo/Bloqueado)
    @Property("state")
    private Boolean state;

}
