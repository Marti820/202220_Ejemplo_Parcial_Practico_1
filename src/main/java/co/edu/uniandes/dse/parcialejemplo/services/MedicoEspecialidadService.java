package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.Optional;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoEspecialidadService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public EspecialidadEntity addEspecialidad(Long medicoID, Long especialidadID) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle una especialidad al medico con id = {0}", medicoID);  
        Optional<MedicoEntity> medicoEntity = medicoRepository.findById(medicoID);
        Optional<EspecialidadEntity> especialidadEntity = especialidadRepository.findById(especialidadID);

        if (medicoEntity.isEmpty()){
            throw new EntityNotFoundException("no hay medico");
        }
        if (especialidadEntity.isEmpty()){
            throw new EntityNotFoundException("no hay especialidad");
        }
        
        especialidadEntity.get().getMedicos().add(medicoEntity.get());
        log.info("Termina proceso de asociarle una especialidad al medico con id = {0}", medicoID);  
        return especialidadEntity.get();
    }   
    @Transactional
	public EspecialidadEntity geEspecialidad(Long medicoId, Long especialidadId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el libro con id = {0} del autor con id = " + medicoId, especialidadId);
		Optional<MedicoEntity> medicoEntity = medicoRepository.findById(medicoId);
		Optional<EspecialidadEntity> especialidadEntity = especialidadRepository.findById(especialidadId);

        if (medicoEntity.isEmpty()){
            throw new EntityNotFoundException("no hay medico");
        }
        if (especialidadEntity.isEmpty()){
            throw new EntityNotFoundException("no hay especialidad");
        }

		log.info("Termina proceso de consultar el libro con id = {0} del autor con id = "  + medicoId, especialidadId);
		if (!especialidadEntity.get().getMedicos().contains(medicoEntity.get()))
			throw new IllegalOperationException("no esta asociados");
		
		return especialidadEntity.get();
	}

}