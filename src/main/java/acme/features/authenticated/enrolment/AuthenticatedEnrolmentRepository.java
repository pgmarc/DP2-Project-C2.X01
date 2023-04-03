
package acme.features.authenticated.enrolment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface AuthenticatedEnrolmentRepository extends AbstractRepository {

	@Query("SELECT e FROM Enrolment e WHERE e.id = :id")
	Enrolment findOneEnrolmentById(int id);

	@Query("SELECT e FROM Enrolment e JOIN e.course c where e.student.id = :id")
	List<Enrolment> findAllEnrolmentsByStudentId(int id);

	@Query("select s from Student s where s.userAccount.id = :id")
	Student findOneStudentByUserAccountId(int id);

	@Query("SELECT c FROM Course c WHERE c.id = :id")
	Course findOneCourseById(int id);

	@Query("SELECT c FROM Course c WHERE c.draftMode = 0")
	List<Course> findAllPublishCourses();
}
