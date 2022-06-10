//(1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); //폼태그 액션을 막음
	
	let data = $("#profileUpdate").serialize(); //key = value 형태
	console.log(data);
	
	$.ajax({
		type: "put",
		url: `/api/user/${userId}`,
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json"
	}).done(res => { //HttpStatus 상태코드가 200번대
		alert("회원정보가 변경되었습니다");
		location.href = `/user/${userId}`;
	}).fail(error => { //HttpStatus 상태코드가 200번대가 아닐때
		if(error.data == null){
			alert(error.responseJSON.message);
		}else{
			alert(error.responseJSON.data.name);
		}
	});
}