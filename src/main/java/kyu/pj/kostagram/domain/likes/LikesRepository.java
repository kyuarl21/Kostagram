package kyu.pj.kostagram.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
	
	@Modifying
	@Query(value = "INSERT INTO likes(image_id, user_id, create_date) VALUES(:imageId, :principalId, now())", nativeQuery = true)
	int myLikes(int imageId, int principalId);
	
	@Modifying
	@Query(value = "DELETE FROM likes WHERE image_id = :imageId AND user_id = :principalId", nativeQuery = true)
	int myUnLikes(int imageId, int principalId);
}
