package co.edu.uniandes.dse.parcialejemplo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ MedicoEspecialidadService.class, EspecialidadService.class })
public class MedicoEspecialidadServiceTest {
    @Autowired
	private MedicoEspecialidadService medicoEspecialidadService;

	@Autowired
	private EspecialidadService especialidadService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private MedicoEntity medico = new MedicoEntity();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
	}
    private void insertData() {
        medico = factory.manufacturePojo(MedicoEntity.class);
        entityManager.persist(medico);
        for (int i = 0; i < 3; i++) {
            EspecialidadEntity entity = factory.manufacturePojo(EspecialidadEntity.class);
            entity.getMedicos().add(medico);
            entityManager.persist(entity);
            medico.getEspecialidades().add(entity);
        }
    }
    @Test
	void testAddEspecialidad() throws EntityNotFoundException, IllegalOperationException {
		EspecialidadEntity newEspecialidad = factory.manufacturePojo(EspecialidadEntity.class);
		especialidadService.createEspecialidad(newEspecialidad);

		EspecialidadEntity especialidadEntity = medicoEspecialidadService.addEspecialidad(medico.getId(), newEspecialidad.getId());
		assertNotNull(especialidadEntity);

		assertEquals(especialidadEntity.getId(), newEspecialidad.getId());
		assertEquals(especialidadEntity.getNombre(), newEspecialidad.getNombre());
		assertEquals(especialidadEntity.getDescripcion(), newEspecialidad.getDescripcion());


		EspecialidadEntity lastEspecialidad = medicoEspecialidadService.geEspecialidad(medico.getId(), newEspecialidad.getId());

		assertEquals(lastEspecialidad.getId(), newEspecialidad.getId());
		assertEquals(lastEspecialidad.getNombre(), newEspecialidad.getNombre());
		assertEquals(lastEspecialidad.getDescripcion(), newEspecialidad.getDescripcion());
	}
    @Test
	void testAddEspecialidadInvaliMedico() {
		assertThrows(EntityNotFoundException.class, () -> {
			EspecialidadEntity newEspecialidad = factory.manufacturePojo(EspecialidadEntity.class);
			especialidadService.createEspecialidad(newEspecialidad);
			medicoEspecialidadService.addEspecialidad(0L, newEspecialidad.getId());
		});
	}
    @Test
	void testAddInvalidEspecialidad() {
		assertThrows(EntityNotFoundException.class, () -> {
			medicoEspecialidadService.addEspecialidad(medico.getId(), 0L);
		});
	}
}
