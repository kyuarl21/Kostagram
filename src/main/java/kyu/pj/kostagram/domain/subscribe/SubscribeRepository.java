package kyu.pj.kostagram.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
	
	@Modifying //insert, update, delete를 nativeQuery로 작성하려면 해당 어노테이션 필요
	@Query(value = "INSERT INTO subscribe(from_user_id, to_user_id, create_date) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mySubscribe(int fromUserId, int toUserId);
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE from_user_id = :fromUserId AND to_user_id = :toUserId", nativeQuery = true)
	void myUnSubscribe(int fromUserId, int toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE from_user_id = :principalId AND to_user_id = :pageUserId", nativeQuery = true)
	int mySubscribeState(int principalId, int pageUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE from_user_id = :pageUserId", nativeQuery = true)
	int mySubscribeCount(int pageUserId);
}
