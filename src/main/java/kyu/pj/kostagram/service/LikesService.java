package kyu.pj.kostagram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.domain.likes.LikesRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikesService {
	
	private final LikesRepository likesRepository;
	
	@Transactional
	public void like(int imageId, int principalId) {
		likesRepository.myLikes(imageId, principalId);
	}
	
	@Transactional
	public void unLike(int imageId, int principalId) {
		likesRepository.myUnLikes(imageId, principalId);
	}
}
