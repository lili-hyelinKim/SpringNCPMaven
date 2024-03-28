package user.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import user.bean.UserImageDTO;
import user.service.ObjectStorageService;
import user.service.UserUploadService;

@Controller
@RequestMapping(value="user")
public class UserUploadConteroller {
	@Autowired
	private UserUploadService userUploadService;
	
	@Autowired
	private ObjectStorageService objectStorageService;
	
	private String bucketName = "자신의 bucket이름";
	
	
	@GetMapping(value="uploadForm")
	public String uploadForm() {
		return "user/uploadForm";
	}
	
	//----- 한번에 1개 선택 또는 여러 개 선택 -------
	@PostMapping(value="upload")
	@ResponseBody
	public String upload(@ModelAttribute UserImageDTO userImageDTO,
						 @RequestParam("img[]") List<MultipartFile> list,
						 HttpSession session) {
		
		//실제폴더
		String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
		System.out.println("실제폴더 = " + filePath);
		
		String imageFileName = "";
		String imageOriginalFileName = "";
		File file;
		String result = "";
		
		//파일들을 모아서 DB로 보내기
		List<UserImageDTO> userImageList = new ArrayList<>();
		
		for(MultipartFile img : list) {
			imageOriginalFileName = img.getOriginalFilename();
			System.out.println("imageOriginalFileName = " + imageOriginalFileName);
			
			//네이버 클라우드 Object Storage
			imageFileName = objectStorageService.uploadFile(bucketName, "storage/", img);
			
			file = new File(filePath, imageOriginalFileName);
			
			try {
				img.transferTo(file);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				result += "<span><img src='/mini/storage/" 
					   + URLEncoder.encode(imageOriginalFileName, "UTF-8") 
					   + "' width='200' height='200' /></span>";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			UserImageDTO dto = new UserImageDTO();
			dto.setImageName(userImageDTO.getImageName());
			dto.setImageContent(userImageDTO.getImageContent());
			dto.setImageFileName(imageFileName);
			dto.setImageOriginalName(imageOriginalFileName);
			
			userImageList.add(dto);
		}//for
		
		//DB
		userUploadService.upload(userImageList);
		
		return result;
	}
	
	@GetMapping(value="uploadList")
	public String uploadList() {
		return "user/uploadList";
	}
	
	@PostMapping(value="getUploadList")
	@ResponseBody
	public List<UserImageDTO> getUploadList(){
		return userUploadService.getUploadList();
	}
	
	@GetMapping(value="uploadView")
	public String uploadView(@RequestParam String seq, Model model) {
		model.addAttribute("seq", seq);
		return "user/uploadView";
	}
	
	@PostMapping(value="getUploadImage") //1개의 이미지 데이터
	@ResponseBody
	public UserImageDTO getUploadImage(@RequestParam String seq) {
		return userUploadService.getUploadImage(seq);
	}
	
	@GetMapping(value="uploadUpdateForm")
	public String uploadUpdateForm(@RequestParam String seq, Model model) {
		model.addAttribute("seq", seq);
		return "user/uploadUpdateForm";
	}
	
	@PostMapping(value="uploadUpdate", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String uploadUpdate(@ModelAttribute UserImageDTO userImageDTO,
			                   @RequestParam("img") MultipartFile img) {
		System.out.println("seq = " + userImageDTO.getSeq());
		userUploadService.uploadUpdate(userImageDTO, img);
		return "이미지 수정 완료";
	}
	
	@PostMapping(value="uploadDelete")
	@ResponseBody
	public void uploadDelete(@RequestParam String[] check) {
		for(String c : check) 
			System.out.println(c);
		
		userUploadService.uploadDelete(check);
	}
}















