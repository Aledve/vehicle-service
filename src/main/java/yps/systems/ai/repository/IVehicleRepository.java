package yps.systems.ai.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Vehicle;

import java.util.List;

@Repository
public interface IVehicleRepository extends Neo4jRepository<Vehicle, String> {

    // Buscar vehículo por el ID del Dueño (Equivalente a getByLeader)
    @Query("MATCH (v:Vehicle)-[:OWNED_BY]->(p:Person) " +
            "WHERE elementId(p) = $ownerElementId " +
            "RETURN v")
    Vehicle getByOwnerElementId(String ownerElementId);

    // Buscar vehículo por el ID de un Conductor autorizado (Equivalente a getByStudent)
    @Query("MATCH (v:Vehicle)-[:DRIVEN_BY]->(p:Person) " +
            "WHERE elementId(p) = $driverElementId " +
            "RETURN v")
    Vehicle getByDriverElementId(String driverElementId);

    // Obtener ID del Dueño dado el vehículo (Equivalente a getLeaderByTeam)
    @Query("MATCH (v:Vehicle)-[:OWNED_BY]->(p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "RETURN p.elementId")
    String getOwnerByVehicleElementId(String vehicleElementId);

    // Obtener lista de IDs de Conductores dados el vehículo (Equivalente a getStudentsByTeam)
    @Query("MATCH (v:Vehicle)-[:DRIVEN_BY]->(p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "RETURN p.elementId")
    List<String> getDriversByVehicleElementId(String vehicleElementId);

    // Asignar Conductor (Equivalente a setStudent)
    @Query("MATCH (v:Vehicle), (p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "AND elementId(p) = $personElementId " +
            "CREATE (v)-[:DRIVEN_BY]->(p)")
    void setDriverToVehicle(String vehicleElementId, String personElementId);

    // Asignar Dueño (Equivalente a setLeader)
    @Query("MATCH (v:Vehicle), (p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "AND elementId(p) = $personElementId " +
            "CREATE (v)-[:OWNED_BY]->(p)")
    void setOwnerToVehicle(String vehicleElementId, String personElementId);

    // Eliminar todas las relaciones de personas (Dueño y Conductores)
    @Query("MATCH (v:Vehicle)-[r:DRIVEN_BY|OWNED_BY]->() " +
            "WHERE elementId(v) = $vehicleElementId " +
            "DELETE r")
    void removePeopleFromVehicle(String vehicleElementId);

    // Eliminar un conductor específico (Equivalente a removeStudent)
    @Query("MATCH (v:Vehicle)-[r:DRIVEN_BY]->(p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "AND elementId(p) = $personElementId " +
            "DELETE r")
    void removeDriverFromVehicle(String vehicleElementId, String personElementId);

    // Eliminar al dueño (Equivalente a removeLeader)
    @Query("MATCH (v:Vehicle)-[r:OWNED_BY]->(p:Person) " +
            "WHERE elementId(v) = $vehicleElementId " +
            "AND elementId(p) = $personElementId " +
            "DELETE r")
    void removeOwnerFromVehicle(String vehicleElementId, String personElementId);

}
