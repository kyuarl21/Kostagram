package kyu.pj.kostagram.domain.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	
}
