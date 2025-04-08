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
import org.unilab.uniplan.studentgroup.StudentGroupId;
import org.unilab.uniplan.studentgroup.StudentGroupRepository;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityRepository;

@Service
public class PostConstructMethodService {
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomRepository roomRepository;
    private final CategoryRepository categoryRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final MajorRepository majorRepository;
    private final CourseRepository courseRepository;
    private final LectorRepository lectorRepository;
    private final StudentRepository studentRepository;
    private final CourseGroupRepository courseGroupRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final ProgramRepository programRepository;
    private final DisciplineRepository disciplineRepository;
    private final ProgramDisciplineRepository programDisciplineRepository;
    private final ProgramDisciplineLectorRepository programDisciplineLectorRepository;


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
        University plovdivUniversity = createUniversity();
        Faculty fmi = createFaculty(plovdivUniversity);
        Department computerSystems = createDepartment(fmi);
        Room roomOne = createRoom(fmi);
        Category roomCat = createCategory();
        RoomCategory roomOneCategory = createRoomCategory(roomOne, roomCat);
        Major informatics = createMajor(fmi);
        Course secondCourse = createCourse(informatics);
        Lector lector = createLector(fmi);
        Student student = createStudent(secondCourse);
        Discipline discipline = createDiscipline();       
        Program program = createProgram(secondCourse);
        CourseGroup courseGroup = createCourseGroup(secondCourse);
        StudentGroup studentGroup = createStudentGroup(student, courseGroup);
        ProgramDiscipline programDiscipline = createProgramDiscipline(discipline, program);
        ProgramDisciplineLector programDisciplineLector = createProgramDisciplineLector(lector, discipline ,program); 
    }
    
    private University createUniversity() {
        University plovdivUniversity = new University("Plovdiv University \"Paisii Hilendarski\"", "TsentarPlovdiv Center, King Asen St 24, 4000 Plovdiv",
                                                          (short) 1961 , "no-info" , "https://uni-plovdiv.bg/en/pages/index/10/");
        plovdivUniversity.setCreatedAt();
        return universityRepository.save(plovdivUniversity);
    }
    
    private Faculty createFaculty(University university) {
        Faculty fmi = new Faculty(university, "fmi" , "Severen, Bulgaria Blvd 236А, 4027 Plovdiv");
        fmi.setCreatedAt();
        return facultyRepository.save(fmi);
    }
    
    private Department createDepartment(Faculty faculty) {
        Department computerSystems = new Department( faculty, "Computer Systems");
        computerSystems.setCreatedAt();
        return departmentRepository.save(computerSystems);
    }
    
    private Room createRoom(Faculty faculty) {
        Room roomOne = new Room(faculty, "547");
        roomOne.setCreatedAt();
        return roomRepository.save(roomOne);
    }
    
    private Category createCategory() {
        Category roomCat = new Category("Computer hall" , (short) 30);
        roomCat.setCreatedAt();
        return categoryRepository.save(roomCat);
    }
    
    private RoomCategory createRoomCategory(Room room, Category category) {
        RoomCategory roomOneCategory = new RoomCategory(new RoomCategoryId(room.getId(), category.getId()), room, category);
        roomOneCategory.setCreatedAt();
        return roomCategoryRepository.save(roomOneCategory);
    }
    
    private Major createMajor(Faculty faculty) {
        Major informatics = new Major(faculty, "Informatics");
        informatics.setCreatedAt();
        return majorRepository.save(informatics);
    }
    
    private Course createCourse(Major major) {
        Course secondCourse = new Course(major, (byte)2, "bachelor" , "regular education");
        secondCourse.setCreatedAt();
        return courseRepository.save(secondCourse);
    }
    
    private Lector createLector(Faculty faculty) {
        Lector lector = new Lector("kiril@uni-plovdiv.bg" , faculty);
        lector.setFirstName("Kiril");
        lector.setLastName("Ivanov");
        lector.setCreatedAt();
        return lectorRepository.save(lector);
    }
    
    private Student createStudent(Course course) {
        Student student = new Student(course, "2301261015");
        student.setFirstName("Dimitar");
        student.setLastName("Petrov");
        student.setCreatedAt();
        return studentRepository.save(student);
    }
    
    private Discipline createDiscipline() {
        Discipline discipline = new Discipline("Java OOP" , "Petko" , null);
        discipline.setCreatedAt();
        return disciplineRepository.save(discipline);
    }
    
    private Program createProgram(Course course) {
        Program program = new Program(course, null);
        program.setCreatedAt();
        return programRepository.save(program);
    }
    
    private CourseGroup createCourseGroup(Course course) {
        CourseGroup courseGroup = new CourseGroup(course, "1a" , 30);
        courseGroup.setCreatedAt();
        return courseGroupRepository.save(courseGroup);
    }
    
    private StudentGroup createStudentGroup(Student student, CourseGroup courseGroup) {
        StudentGroup studentGroup = new StudentGroup(new StudentGroupId(student.getId(), courseGroup.getId()), student, courseGroup);
        studentGroup.setCreatedAt();
        return studentGroupRepository.save(studentGroup);
    }
    
    private ProgramDiscipline createProgramDiscipline(Discipline discipline, Program program) {
        ProgramDiscipline programDiscipline = new ProgramDiscipline(new ProgramDisciplineId(discipline.getId(), program.getId()),
                                                                    discipline, program , (short)40, (short)30, (byte)1);
        programDiscipline.setCreatedAt();
        return programDisciplineRepository.save(programDiscipline);
    }
    
    private ProgramDisciplineLector createProgramDisciplineLector(Lector lector, Discipline discipline, Program program) {
        ProgramDisciplineLector programDisciplineLector = new ProgramDisciplineLector(new ProgramDisciplineLectorId(lector.getId(), program.getId(), discipline.getId()) ,
                                    lector , program , discipline , LectorType.LECTURE);
        programDisciplineLector.setCreatedAt();
        return programDisciplineLectorRepository.save(programDisciplineLector);
    }
}