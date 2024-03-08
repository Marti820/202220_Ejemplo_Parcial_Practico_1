package co.edu.uniandes.dse.parcialejemplo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;

public interface EspecialidadRepository extends JpaRepository<EspecialidadEntity, Long> {
    
}
