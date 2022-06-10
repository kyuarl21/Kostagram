package kyu.pj.kostagram.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.domain.subscribe.SubscribeRepository;
import kyu.pj.kostagram.handler.ex.CustomApiException;
import kyu.pj.kostagram.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager entityManager; //Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> subscribeList(int principalId, int pageUserId) {
		
		//Query 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
		sb.append("if ((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append("if ((u.id = ?), 1, 0) equalUserState ");
		sb.append("FROM users u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?");
		
		//Query 완성
		Query query = entityManager.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		//Query 실행 (qlrm - Dto에 DB결과를 매핑)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}
	
	@Transactional
	public void subscribe(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mySubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독중인 대상입니다");
		}
	}
	
	@Transactional
	public void unSubscribe(int fromUserId, int toUserId) {
		subscribeRepository.myUnSubscribe(fromUserId, toUserId);
	}
}
