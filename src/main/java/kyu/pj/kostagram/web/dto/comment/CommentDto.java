package kyu.pj.kostagram.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CommentDto {
	@NotBlank //빈값, null, 공백 체크
	private String content;
	@NotNull //null 체크 (Integer는 null 체크만 가능 int는 불가능)
	private Integer imageId;
}
