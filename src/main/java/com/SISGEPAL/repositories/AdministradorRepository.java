package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.Administrador;
import com.SISGEPAL.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    public Administrador findByEmpleado(Empleado empleado);
}