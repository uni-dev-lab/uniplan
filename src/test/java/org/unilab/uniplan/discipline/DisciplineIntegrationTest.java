package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.unilab.uniplan.program.Program;
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineRepository;

@SpringBootTest
@Transactional
class DisciplineIntegrationTest {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private ProgramDisciplineRepository programDisciplineRepository;
    @Autowired
    private ProgramRepository programRepository;

    @Test
    void testDisciplineUpdateAffectsProgramDiscipline() {
        // Step 1: Setup initial data

        // 1. Save a Discipline
        Discipline discipline = new Discipline();
        discipline.setName("Original Name");
        discipline.setMainLector("Lector A");
        discipline = disciplineRepository.save(discipline);

        // Save a Program
        Program program = new Program();
        program = programRepository.save(program);


        // 2. Save associated ProgramDiscipline, linking to discipline
        ProgramDiscipline programDiscipline = new ProgramDiscipline();
        ProgramDisciplineId id = new ProgramDisciplineId();
        id.setDisciplineId(discipline.getId());
        id.setProgramId(program.getId());

        // set other id fields if any

        programDiscipline.setId(id);
        programDiscipline.setDiscipline(discipline);
        programDiscipline.setProgram(program); // assuming you create it
        programDiscipline.setHoursLecture((short) 40);
        programDiscipline.setHoursExercises((short) 20);
        programDiscipline.setSemesterCount((byte) 2);
        programDisciplineRepository.save(programDiscipline);

        //Step 3: Fetch ProgramDiscipline and verify
        ProgramDiscipline fetchedPD = programDisciplineRepository.findById(id).orElseThrow();

        // Step 2: Change Discipline
        discipline.setName("Updated Name");
        disciplineRepository.save(discipline);


        // Assert the relationship is maintained and possibly affected
        assertThat(fetchedPD.getDiscipline().getName()).isEqualTo("Updated Name");
        // additional assertions depending on your logic
    }
}