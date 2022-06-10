package kyu.pj.kostagram.web.dto.users;

import kyu.pj.kostagram.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersProfileDto {
	private boolean pageOwnerState;
	private int imageCount;
	private boolean subscribeState;
	private int subscribeCount;
	private Users users;
}
