package lab2testing.service;

import lab2testing.domain.Nota;
import lab2testing.domain.Pair;
import lab2testing.domain.Student;
import lab2testing.domain.Tema;
import lab2testing.repository.NotaXMLRepository;
import lab2testing.repository.StudentXMLRepository;
import lab2testing.repository.TemaXMLRepository;
import lab2testing.validation.ValidationException;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Service {
    private StudentXMLRepository studentXmlRepo;
    private TemaXMLRepository temaXmlRepo;
    private NotaXMLRepository notaXmlRepo;

    public Service(StudentXMLRepository studentXmlRepo, TemaXMLRepository temaXmlRepo, NotaXMLRepository notaXmlRepo) {
        this.studentXmlRepo = studentXmlRepo;
        this.temaXmlRepo = temaXmlRepo;
        this.notaXmlRepo = notaXmlRepo;
    }

    public Iterable<Student> findAllStudents() {
        return studentXmlRepo.findAll();
    }

    public Iterable<Tema> findAllTeme() {
        return temaXmlRepo.findAll();
    }

    public Iterable<Nota> findAllNote() {
        return notaXmlRepo.findAll();
    }

    //PROBLEM SOLVED: if the validation fails, the code still returns null (converted to 1 then) and makes the ui print
    //that the studdent was sucessfully added
    public int saveStudent(String id, String nume, int grupa) {
        Student student = new Student(id, nume, grupa);
        try {
            Student result = studentXmlRepo.save(student);
            if (result == null) {
                return 1;
            }
            return 0;
        } catch (ValidationException e) {
            return 2;
        }
    }

    public int saveTema(String id, String descriere, int deadline, int startline) {
        Tema tema = new Tema(id, descriere, deadline, startline);
        int ret;
        try {
            Tema result = temaXmlRepo.save(tema);
            if (result == null) {
                ret = 1;
            } else {
                ret = 0;
            }
        } catch (ValidationException e) {
            ret = 2;
        }
        return ret;
    }

    public int saveNota(String idStudent, String idTema, double valNota, int predata, String feedback) {
        try {
            if (studentXmlRepo.findOne(idStudent) == null || temaXmlRepo.findOne(idTema) == null) {
                return -1;
            } else {
                int deadline = temaXmlRepo.findOne(idTema).getDeadline();

                if (predata - deadline > 2) {
                    valNota = 1;
                }
                if (predata > deadline) {
                    valNota = valNota - 2.5 * (predata - deadline);
                }
                Nota nota = new Nota(new Pair(idStudent, idTema), valNota, predata, feedback);

                Nota result = notaXmlRepo.save(nota);
                if (result == null) {
                    return 1;
                }
                return 0;

            }
        } catch (Throwable e) {
            return 2;
        }
    }

    public int deleteStudent(String id) {
        Student result = studentXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int deleteTema(String id) {
        Tema result = temaXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int deleteNota(Pair<String, String> id) {
        Nota result = notaXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }


    public int updateStudent(String id, String numeNou, int grupaNoua) {
        Student studentNou = new Student(id, numeNou, grupaNoua);
        Student result = studentXmlRepo.update(studentNou);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateTema(String id, String descriereNoua, int deadlineNou, int startlineNou) {
        Tema temaNoua = new Tema(id, descriereNoua, deadlineNou, startlineNou);
        Tema result = temaXmlRepo.update(temaNoua);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int extendDeadline(String id, int noWeeks) {
        Tema tema = temaXmlRepo.findOne(id);

        if (tema != null) {
            LocalDate date = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int currentWeek = date.get(weekFields.weekOfWeekBasedYear());

            if (currentWeek >= 39) {
                currentWeek = currentWeek - 39;
            } else {
                currentWeek = currentWeek + 12;
            }

            if (currentWeek <= tema.getDeadline()) {
                int deadlineNou = tema.getDeadline() + noWeeks;
                return updateTema(tema.getID(), tema.getDescriere(), deadlineNou, tema.getStartline());
            }
        }
        return 0;
    }

    public void createStudentFile(String idStudent, String idTema) {
        Nota nota = notaXmlRepo.findOne(new Pair(idStudent, idTema));

        notaXmlRepo.createFile(nota);
    }
}
