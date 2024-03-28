$(function(){
	$.ajax({
		type: 'post',
		url: '/mini/user/getUploadList',
		dataType: 'json',
		success: function(data){
			console.log(JSON.stringify(data));
			
			$.each(data, function(index, items){
			
				/*
				$('<tr/>').append($('<td/>', {
					align: 'center',
					text: items.seq
					
				})).append($('<td/>', {
					align: 'center',
					}).append($('<img/>', {
						src: '../storage/' + items.imageOriginalName,
						style: 'width: 70px; height: 70px;'
					}))
					
				).append($('<td/>', {
					align: 'center',
					text: items.imageName
					
				})).appendTo($('#uploadListTable'));
				*/
				
				/*
				var result = `<tr>`
						   + `<td align="center">` + items.seq + `</td>`
						   + `<td align="center"><img src="../storage/` + items.imageOriginalName + `" style="width: 70px; height: 70px;"/></td>`
						   + `<td align="center">` + items.imageName + `</td>`
						   + `</tr>`;
						   
				$('#uploadListTable').append(result);
				*/
				
				//이미지는 NCP에서 가져온다.
				var result = `<tr>`
						   + `<td align="center">`
						   		+ `<input type="checkbox" name="check" id="check" value="` + items.seq + `">` + items.seq
						   + `</td>`
						   + `<td align="center">`
						   		+ `<a href="/mini/user/uploadView?seq=` + items.seq + `">`
						   		+ `<img src="https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-81/storage/` + items.imageFileName + `" style="width: 70px; height: 70px;"/>`
						   		+ `</a>`
						   + `</td>`
						   + `<td align="center">` + items.imageName + `</td>`
						   + `</tr>`;
						   
				$('#uploadListTable').append(result);						   
				
			});//$.each
			
			//전체 선택 / 전체 해제
			$('#all').click(function(){
				if($(this).prop('checked')){
					$('input[name="check"]').prop('checked', true);
				}else{
					$('input[name="check"]').prop('checked', false);
				}
			});
			
			//전체 선택한 후 <input type="checkbox" name="check">가 1개라도 해제가 되면 all는 자동으로 해제상태로 변환한다.
			$('input[name="check"]').click(function(){
				$('#all').prop('checked', $('input[name="check"]:checked').length == $('input[name="check"]').length);
			});
		},
		error: function(e){
			console.log(e);
		}
	});//$.ajax
});









