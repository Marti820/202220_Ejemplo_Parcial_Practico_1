package co.edu.uniandes.dse.parcialejemplo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoService {
    
    @Autowired
    MedicoRepository medicoRepository;

    @Transactional
    public MedicoEntity createMedicos(MedicoEntity medico) throws IllegalOperationException{
        log.info("Inicia proceso de creación del medico");
        if ((medico.getRegistro().charAt(0) != 'R') || (medico.getRegistro().charAt(1) != 'M')){
            throw new IllegalOperationException("El registro Debe iniciar con RM");
        }
        return medicoRepository.save(medico);
    }
    
}
