package org.unilab.uniplan;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.category.Category;
import org.unilab.uniplan.category.CategoryRepository;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.coursegroup.CourseGroup;
import org.unilab.uniplan.coursegroup.CourseGroupRepository;
import org.unilab.uniplan.department.Department;
import org.unilab.uniplan.department.DepartmentRepository;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.discipline.DisciplineRepository;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.Lector;
import org.unilab.uniplan.lector.LectorRepository;
import org.unilab.uniplan.major.Major;
import org.unilab.uniplan.major.MajorRepository;
import org.unilab.uniplan.program.Program;
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineRepository;
import org.unilab.uniplan.programdisciplinelector.LectorType;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLector;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorId;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorRepository;
import org.unilab.uniplan.room.Room;
import org.unilab.uniplan.room.RoomRepository;
import org.unilab.uniplan.roomcategory.RoomCategory;
import org.unilab.uniplan.roomcategory.RoomCategoryId;
import org.unilab.uniplan.roomcategory.RoomCategoryRepository;
import org.unilab.uniplan.student.Student;
import org.unilab.uniplan.student.StudentRepository;
import org.unilab.uniplan.studentgroup.StudentGroup;
import org.unilab.uniplan.studentgroup.StudentGroupRepository;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityRepository;

@Service
public class PostConstructMethodService {
    private UniversityRepository universityRepository;
    private FacultyRepository facultyRepository;
    private DepartmentRepository departmentRepository;
    private RoomRepository roomRepository;
    private CategoryRepository categoryRepository;
    private RoomCategoryRepository roomCategoryRepository;
    private MajorRepository majorRepository;
    private CourseRepository courseRepository;
    private LectorRepository lectorRepository;
    private StudentRepository studentRepository;
    private CourseGroupRepository courseGroupRepository;
    private StudentGroupRepository studentGroupRepository;
    private ProgramRepository programRepository;
    private DisciplineRepository disciplineRepository;
    private ProgramDisciplineRepository programDisciplineRepository;
    private ProgramDisciplineLectorRepository programDisciplineLectorRepository;


    @Autowired
    private PostConstructMethodService(final UniversityRepository universityRepository,
                                       final FacultyRepository facultyRepository,
                                       final DepartmentRepository departmentRepository,
                                       final RoomRepository roomRepository,
                                       final CategoryRepository categoryRepository,
                                       final RoomCategoryRepository roomCategoryRepository,
                                       final MajorRepository majorRepository,
                                       final CourseRepository courseRepository,
                                       final LectorRepository lectorRepository,
                                       final StudentRepository studentRepository,
                                       final CourseGroupRepository courseGroupRepository,
                                       final StudentGroupRepository studentGroupRepository,
                                       final ProgramRepository programRepository,
                                       final DisciplineRepository disciplineRepository,
                                       final ProgramDisciplineRepository programDisciplineRepository,
                                       final ProgramDisciplineLectorRepository programDisciplineLectorRepository) {
        this.universityRepository = universityRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
        this.categoryRepository = categoryRepository;
        this.roomCategoryRepository = roomCategoryRepository;
        this.majorRepository = majorRepository;
        this.courseRepository = courseRepository;
        this.lectorRepository = lectorRepository;
        this.studentRepository = studentRepository;
        this.courseGroupRepository = courseGroupRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.programRepository = programRepository;
        this.disciplineRepository = disciplineRepository;
        this.programDisciplineRepository = programDisciplineRepository;
        this.programDisciplineLectorRepository = programDisciplineLectorRepository;
    }

    @PostConstruct
    public void populateDatabase() {
        University plovdivUniversity = new University("Plovdiv University \"Paisii Hilendarski\"", "TsentarPlovdiv Center, King Asen St 24, 4000 Plovdiv",
                                                      (short) 1961 , "no-info" , "https://uni-plovdiv.bg/en/pages/index/10/");
        plovdivUniversity.setCreatedAt();
        universityRepository.save(plovdivUniversity);
        
        Faculty fmi = new Faculty(plovdivUniversity, "fmi" , "Severen, Bulgaria Blvd 236А, 4027 Plovdiv");
        fmi.setCreatedAt();
        facultyRepository.save(fmi);
        
        Department computerSystems = new Department( fmi, "Computer Systems");
        computerSystems.setCreatedAt();
        departmentRepository.save(computerSystems);
        
        Room roomOne = new Room(fmi, "547");
        roomOne.setCreatedAt();
        roomRepository.save(roomOne);
        
        Category roomCat = new Category("Computer hall" , (short) 30);
        roomCat.setCreatedAt();
        categoryRepository.save(roomCat);
        
        RoomCategory roomOneCategory = new RoomCategory(new RoomCategoryId(roomOne.getId(), roomCat.getId()), roomOne, roomCat);
        roomOneCategory.setCreatedAt();
        roomCategoryRepository.save(roomOneCategory);

        Major informatics = new Major(fmi, "Informatics");
        informatics.setCreatedAt();
        majorRepository.save(informatics);

        Course secondCourse = new Course(informatics, (byte)2, "bachelor" , "regular education");
        secondCourse.setCreatedAt();
        courseRepository.save(secondCourse);

        Lector lector = new Lector("kiril@uni-plovdiv.bg" , fmi);
        lector.setFirstName("Kiril");
        lector.setLastName("Ivanov");
        lector.setCreatedAt();
        lectorRepository.save(lector);

        Student student = new Student(secondCourse, "2301261015");
        student.setFirstName("Dimitar");
        student.setLastName("Petrov");
        student.setCreatedAt();
        studentRepository.save(student);

        Discipline discipline = new Discipline("Java OOP" , "Petko" , null);
        discipline.setCreatedAt();
        disciplineRepository.save(discipline);
        
        Program program = new Program(secondCourse, null);
        program.setCreatedAt();
        programRepository.save(program);

        CourseGroup courseGroup = new CourseGroup(secondCourse, "1a" , 30);
        courseGroup.setCreatedAt();
        courseGroupRepository.save(courseGroup);
        
        StudentGroup studentGroup = new StudentGroup(student, courseGroup);
        studentGroup.setCreatedAt();
        studentGroupRepository.save(studentGroup);

        ProgramDiscipline programDiscipline = new ProgramDiscipline(new ProgramDisciplineId(discipline.getId(), program.getId()), 
                                                                    discipline, program , (short)40, (short)30, (byte)1);
        programDiscipline.setCreatedAt();
        programDisciplineRepository.save(programDiscipline);

        ProgramDisciplineLector programDisciplineLector = new ProgramDisciplineLector(new ProgramDisciplineLectorId(lector.getId(), program.getId(), discipline.getId()) ,
                                                                                      lector , program , discipline , LectorType.LECTURE);
        programDisciplineLector.setCreatedAt();
        programDisciplineLectorRepository.save(programDisciplineLector); 
    }
}
