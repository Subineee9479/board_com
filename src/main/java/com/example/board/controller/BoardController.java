package com.example.board.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.net.URLEncoder;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.board.dto.Criteria;
import com.example.board.dto.PageDto;
import com.example.board.dao.IBoardDao;
import com.example.board.dao.IFileDao;
import com.example.board.dao.IReplyDao;
import com.example.board.dto.BoardDto;
import com.example.board.dto.FileDto;
import com.example.board.dto.ReplyDto;

// Controller : HTTP 요청을 받아서 응답을 하는 컴포넌트, 스프링 부트가 자동으로 Bean으로  생성한다.
@Controller
public class BoardController {

	@Autowired
	IBoardDao boardDao;
	@Autowired
	IReplyDao replyDao;
	@Autowired
	IFileDao fileDao;

	@RequestMapping("/")
	// @ResponseBody
	public String root() {
		// return "root()함수 호출됨."; // 문자열로 반환

		System.out.println("listForm으로 리다이렉트 됨");
		return "redirect:listForm"; // listForm으로 리다이렉트 됨.
	}

	@RequestMapping("/writeForm")
	public String writeForm() {
		return "writeForm"; // writeForm.jsp 디스패치함
	}

	// 첨부파일 게시글 작성
	@RequestMapping("/writeAction")
	@ResponseBody
	public String writeAction(@RequestParam("board_name") String board_name,
							@RequestParam("board_title") String board_title, 	
							@RequestParam("board_content") String board_content,
							@RequestParam("user_id") String user_id, 
							@RequestParam("file") MultipartFile file,
							@RequestParam(value = "non_user_pw", required = false) String non_user_pw, 
							HttpServletRequest request) {
		System.out.println("제대로 넘어와 : " + non_user_pw);
		System.out.println("board_name : " + board_name);
		System.out.println("board_title : " + board_title);
		System.out.println("board_content : " + board_content);
		System.out.println("user_id : " + user_id);
		System.out.println("non_user_pw : " + non_user_pw);

		// 게시글 작성
		int res = boardDao.write(board_name, board_title, board_content, user_id, non_user_pw);
		System.out.println("res : " + res);

		if (res == 1) {
			System.out.println("글쓰기 성공!");
			// request.getSession().setAttribute("alert_message", "글쓰기 성공!");
			// return "redirect:listForm"; // listForm.jsp 으로 리다이렉트 됨

			// 파일 처리 구간(?)
			if (!file.isEmpty()) {

				String file_real_name = file.getOriginalFilename();
				String filePath = "C://fileUp/";
				String destinationFile = filePath; // 파일 저장할 경로
				String extension = file_real_name.substring(file_real_name.lastIndexOf("."));
				String file_save_name = UUID.randomUUID().toString() + "_" + extension; // 파일 이름 랜덤 생성
				long file_size = file.getSize();

				try {
					byte[] bytes = file.getBytes();
					Path path = Paths.get(filePath + file_save_name);
					Files.write(path, bytes);
				} catch (IOException e) {
					e.printStackTrace();
					return "<script>alert('파일 첨부 실패!'); location.href='writeForm';</script>";
				}

				// 파일 정보 저장
				int result = boardDao.saveFile(file_save_name, file_real_name, file_size, destinationFile);
				if (result != 1) {
					return "<script>alert('파일정보 저장 실패!'); location.href='writeForm';</script>";
				} else {
					return "<script>alert('글쓰기 성공!'); location.href='listForm';</script>";
				}
			} else {
				// 파일이 없는데 글만쓰는 경우
				return "<script>alert('글쓰기 성공!'); location.href='listForm';</script>";
			}

			// return "<script>alert('글쓰기 성공!');</script>";
		} else {
			System.out.println("글쓰기 실패:(");
			// request.getSession().setAttribute("alert_message", "글쓰기 실패:(");
			// return "redirect:writeForm"; // writeForm.jsp 으로 리다이렉트 됨
			return "<script>alert('글쓰기 실패ㅠㅠ');location.href='writeForm';</script>";
		}
	}

	@RequestMapping("/contentForm")
	public String contentForm(@RequestParam("board_idx") String board_idx, Model model, HttpServletRequest request) {
		// 조회수 증가
		boardDao.hit(board_idx);

		// 게시글 보기
		BoardDto dto = boardDao.viewDto(board_idx);
		System.out.println(dto);
		model.addAttribute("dto", dto);

		// 첨부파일
		FileDto fdto = boardDao.searchFile(board_idx);
		// List<FileDto> file_list = fileDao.file_list(board_idx);
		System.out.println("확인" + fdto);
		model.addAttribute("fdto", fdto);

		// 댓글 리스트 가져오기
		List<ReplyDto> reply_list = replyDao.reply_list(board_idx); // 어느글에 대한 댓글인지 알기위해서 board_idx 사용
		System.out.println("reply_list : " + reply_list);
		model.addAttribute("reply_list", reply_list);

		return "contentForm"; // contentForm.jsp 으로 리다이렉트 됨
	}

	// 파일 다운로드
	@RequestMapping("/downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String path = request.getSession().getServletContext().getRealPath("C:\fileUp");
		String filename = request.getParameter("file_save_name");
		String realFilename = "";
		System.out.println("filename : " + filename);

		try {
			String browser = request.getHeader("User-Agent");
			// 파일 인코딩
			if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
				System.out.println("fileName1 : " + filename);

				filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			} else {
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (UnsupportedEncodingException ex) {
			System.out.println("UnsupportedEncodingException");
		}

		realFilename = "C:\\fileUp\\" + filename;
		// realFilename = path + "/" + filename;
		System.out.println(realFilename);
		File file1 = new File(realFilename);

		if (!file1.exists()) {
			return;
		}
		// 파일 지정
		response.setContentType("application/octer-stream");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		try {
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(realFilename);

			int ncount = 0;
			byte[] bytes = new byte[512];

			while ((ncount = fis.read(bytes)) != -1) {
				os.write(bytes, 0, ncount);
			}
			fis.close();
			os.close();
		} catch (Exception e) {
			System.out.println("FileNotFoundException : " + e);
		}
	}	
	
	//글 수정
	@RequestMapping("/updateAction")
	@ResponseBody
	public String updateAction(@RequestParam("board_idx") String board_idx,
	                           @RequestParam("board_title") String board_title,
	                           @RequestParam("board_content") String board_content,
	                           @RequestParam(value = "user_id", required = false) String user_id, // 글 작성자
	                           @RequestParam(value = "whoLoginNow", required = false) String whoLoginNow, // 로그인한 사람
	                           @RequestParam(value = "non_user_pw", required = false) String inputPw, 
	                           HttpServletRequest request, HttpSession session) {
	    
	    System.out.println("글수정 board_idx: " + board_idx);
	    System.out.println("글수정 board_title: " + board_title);
	    System.out.println("글수정 board_content: " + board_content);
	    System.out.println("글수정 user_id: " + user_id);
	    System.out.println("글수정 non_user_pw: " + inputPw);
	    System.out.println("로그인ID: " + whoLoginNow);

	    // 세션에서 로그인 정보 가져오기
//	    String loggedInUserId = (String) session.getAttribute("user_id");
//	    System.out.println("loggedInUserId " + loggedInUserId);

	    // 회원 글 수정
	    if (whoLoginNow != null && !whoLoginNow.isEmpty()) {
	        if (whoLoginNow.equals(user_id)) {
	            System.out.println("회원 글수정 시작!");
	            int result2 = boardDao.updateDto(board_idx, board_title, board_content, user_id);
	            System.out.println("회원 글수정 result: " + result2);
	            try {
	                if (result2 == 1) {
	                    System.out.println("회원 글수정 성공");
	                    return "<script>alert('회원 글수정 성공!'); location.href='listForm';</script>";
	                } else {
	                    return "<script>alert('회원 글수정 실패!'); location.href='contentForm';</script>";
	                }
	            } catch (Exception e) {
	                System.out.println("회원 글수정 예외발생함");
	                e.printStackTrace();
	                return "회원 글 수정 예외 발생";
	            }
	        } else {
	            return "<script>alert('작성자만 수정할 수 있습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	        }
	    }
	    // 비회원 글 수정
	    else if (inputPw != null && !inputPw.isEmpty()) {
	        System.out.println("비회원 글수정 시작");
	        
	        // DB에서 비회원 게시글 비밀번호 조회
	        String storedNonUserPw = boardDao.getNonUserPw(board_idx);
	        System.out.println("storedNonUserPw : " + storedNonUserPw);
	        
	        // 입력한 비밀번호와 DB의 비밀번호 비교
	        if (inputPw.equals(storedNonUserPw)) {
	            System.out.println("비밀번호 일치할 경우");
	            int result = boardDao.updateDto2(board_idx, board_title, board_content, inputPw);
	            System.out.println("비회원 수정시작 result: " + result);
	            try {
	                if (result == 1) {
	                    System.out.println("비회원 글수정 성공");
	                    return "<script>alert('비회원 글수정 성공!'); location.href='listForm';</script>";
	                } else {
	                    System.out.println("비회원 글수정 실패");
	                    return "<script>alert('비회원 글수정 실패'); location.href='contentForm';</script>";
	                }
	            } catch (Exception e) {
	                System.out.println("비회원 글수정 예외발생함");
	                e.printStackTrace();
	                return "비회원 글 수정 예외 발생";
	            }
	        } else {
	            System.out.println("비회원 비밀번호 불일치");
	            return "<script>alert('비밀번호가 일치하지않습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	        }
	    } else {
	    	if(user_id != null && !user_id.isEmpty()) {
	        return "<script>alert('작성자만 수정할 수 있습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	    } else {
	    	return "<script>alert('잘못된 요청입니다!'); location.href='listForm';</script>";
	    	}
	    }
	}

	// 글 삭제
	@RequestMapping("/deleteAction")
	@ResponseBody
	public String deleteAction(@RequestParam("board_idx") String board_idx,
	                           @RequestParam(value = "user_id", required = false) String user_id,
	                           @RequestParam(value = "whoLoginNow", required = false) String whoLoginNow,
	                           @RequestParam(value = "non_user_pw", required = false) String inputPw, 
	                           HttpServletRequest request, HttpSession session) {
	    System.out.println("글삭제 board_idx: " + board_idx);
	    System.out.println("글삭제 user_id: " + user_id);
	    System.out.println("글삭제 non_user_pw: " + inputPw);

	    // 세션에서 로그인 정보 가져오기
	    //String loggedInUserId = (String) session.getAttribute("user_id");

	    // 회원 글 삭제
	    if (whoLoginNow != null && !whoLoginNow.isEmpty()) {
	        if (whoLoginNow.equals(user_id)) {
	            System.out.println("회원 글삭제 시작!");
	            int result2 = boardDao.deleteDto(board_idx, user_id);
	            System.out.println("회원 글삭제 result: " + result2);
	            try {
	                if (result2 == 1) {
	                    System.out.println("회원 글삭제 성공");
	                    return "<script>alert('회원 글삭제 성공!'); location.href='listForm';</script>";
	                } else {
	                    return "<script>alert('회원 글삭제 실패!'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	                }
	            } catch (Exception e) {
	                System.out.println("회원 글삭제 예외발생함");
	                e.printStackTrace();
	                return "회원 글 삭제 예외 발생";
	            }
	        } else {
	            return "<script>alert('작성자만 삭제할 수 있습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	        }
	    }
	    // 비회원 글 삭제
	    else if (inputPw != null && !inputPw.isEmpty()) {
	        System.out.println("비회원 글삭제 시작");
	        
	        // DB에서 비회원 게시글 비밀번호 조회
	        String storedNonUserPw = boardDao.getNonUserPw(board_idx);
	        System.out.println("storedNonUserPw : " + storedNonUserPw);
	        
	        // 입력한 비밀번호와 DB의 비밀번호 비교
	        if (inputPw.equals(storedNonUserPw)) {
	            System.out.println("비밀번호 일치할 경우");
	            int result = boardDao.deleteDto2(board_idx, inputPw);
	            System.out.println("비회원 삭제시작 result: " + result);
	            try {
	                if (result == 1) {
	                    System.out.println("비회원 글삭제 성공");
	                    return "<script>alert('비회원 글삭제 성공!'); location.href='listForm';</script>";
	                } else {
	                    System.out.println("비회원 글삭제 실패");
	                    return "<script>alert('작성자만 삭제가능합니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	                }
	            } catch (Exception e) {
	                System.out.println("비회원 글삭제 예외발생함");
	                e.printStackTrace();
	                return "비회원 글 삭제 예외 발생";
	            }
	        } else {
	            System.out.println("비회원 글삭제 실패");
	            return "<script>alert('비밀번호가 일치하지 않습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	        }
	    } else {
	        if (user_id != null && !user_id.isEmpty()) {
	            return "<script>alert('작성자만 삭제할 수 있습니다.'); location.href='contentForm?board_idx=" + board_idx + "';</script>";
	        } else {
	            return "<script>alert('잘못된 요청입니다.'); location.href='listForm';</script>";
	        }
	    }
	}


	// 엑셀 다운
	@RequestMapping("/excelDown")
	public void downloadList(@RequestParam(defaultValue = "1") int pageNum,
							@RequestParam(value = "searchType", required = false) String searchType,
							@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
							HttpServletResponse response) {
		System.out.println("pageNum : " + pageNum);
		System.out.println("searchType : " + searchType);
		System.out.println("searchKeyword : " + searchKeyword);
		
		// 데이터 조회 및 모델에 초가
		List<BoardDto> downList;
		downList = fileDao.downBoardList(searchType, searchKeyword);
		System.out.println("downList : " + downList);
		
		// 엑셀 파일 생성
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("게시판 목록");
		
		// 열 제목 설정
		String[] columnTitles = {"번호", "글 종류", "글 제목", "첨부파일", "작성자", "작성일", "조회수"};
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columnTitles.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columnTitles[i]);
		}
		
		// 데이터 삽입
		int rowIndex = 1;
		for(BoardDto board : downList) {
			Row dataRow = sheet.createRow(rowIndex++);
			dataRow.createCell(0).setCellValue(board.getBoard_idx());
			dataRow.createCell(1).setCellValue(board.getBoard_name());
			dataRow.createCell(2).setCellValue(board.getBoard_title());
			dataRow.createCell(3).setCellValue(board.getFile_count());
			dataRow.createCell(4).setCellValue(board.getUser_id());
			dataRow.createCell(5).setCellValue(board.getBoard_date().toString());
			dataRow.createCell(6).setCellValue(board.getBoard_hit());			
		}
		
		// 컨텐츠 타입과 파일 이름 설정
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=board_list.xlsx");
		
		// 생성한 엑셀 파일을 출력 스트림에 작성
		try {
			OutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
			workbook.close();		
			outputStream.flush();
			outputStream.close();		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
//	// 댓글쓰기
//	@PostMapping("/writeReplyAction")
//	@ResponseBody
//	public String writeReplyAction(@RequestParam("reply_name") String reply_name,
//								@RequestParam("reply_content") String reply_content,								
//								@RequestParam("reply_board_index") String reply_board_index, // 댓글 단 게시글 번호
//								@RequestParam(value = "reply_level", required = false) Integer reply_level,
//								@RequestParam(value = "is_deleted", required = false) Integer is_deleted,
//								@RequestParam(value= "parent_id", required = false) Integer parent_id,
//								HttpServletRequest request) {
//		
//		System.out.println("reply_name : " + reply_name);
//		System.out.println("reply_content : " + reply_content);
//		System.out.println("reply_board_index : " + reply_board_index);
//		System.out.println("reply_level : " + reply_level);
//		System.out.println("is_deleted : " + is_deleted);
//		System.out.println("parent_id : " + parent_id);
//		
//		int result = replyDao.reply_write(reply_name, reply_content, reply_board_index, reply_level, is_deleted, parent_id);
//		System.out.println("result : " + result);
//		
//		if (result == 1) {
//			System.out.println("댓글달기 성공!");
//
//			return "<script>alert('댓글달기 성공!'); location.href='contentForm?board_idx=" + reply_board_index
//					+ "';</script>";
//		} else {
//			System.out.println("댓글달기 실패!");
//
//			return "<script>alert('댓글달기 실패!'); location.href='contentForm?board_idx=" + reply_board_index
//					+ "';</script>";
//		}
//	}
	
	@PostMapping("/writeReplyAction")
	@ResponseBody
	public String writeReplyAction(@RequestParam("reply_name") String reply_name,
	                               @RequestParam("reply_content") String reply_content,
	                               @RequestParam("reply_board_idx") String reply_board_idx, // 댓글 단 게시글 번호
	                               @RequestParam(value = "reply_level", required = false) Integer reply_level,
	                               @RequestParam(value = "is_deleted", required = false) Integer is_deleted,
	                               @RequestParam(value = "parent_id", required = false) Integer parent_id,
	                               HttpServletRequest request) {

	    System.out.println("reply_name: " + reply_name);
	    System.out.println("reply_content: " + reply_content);
	    System.out.println("reply_board_idx: " + reply_board_idx);
	    System.out.println("reply_level: " + reply_level);
	    System.out.println("is_deleted: " + is_deleted);
	    System.out.println("parent_id: " + parent_id);

	    int result;
	    if (parent_id != null) {
	        // 대댓글인 경우	
	    	reply_level = 1; 
	        result = replyDao.reply_write(reply_name, reply_content, reply_board_idx, reply_level, is_deleted, parent_id);
	        
	    } else {
	        // 댓글인 경우
	        result = replyDao.reply_write(reply_name, reply_content, reply_board_idx, reply_level, is_deleted, parent_id);
	    }

	    System.out.println("result: " + result);

	    if (result == 1) {
	        System.out.println("댓글 작성 성공!");
	        return "<script>alert('댓글 작성 성공!'); location.href='contentForm?board_idx=" + reply_board_idx
	                + "';</script>";
	    } else {
	        System.out.println("댓글 작성 실패!");
	        return "<script>alert('댓글 작성 실패!'); location.href='contentForm?board_idx=" + reply_board_idx
	                + "';</script>";
	    }
	}

	// 댓글삭제
	@RequestMapping("/deleteReplyAction")
	@ResponseBody
	public String deleteReplyAction(@RequestParam("reply_idx") String reply_idx,
									@RequestParam("reply_board_idx") String reply_board_idx, 
									@RequestParam("reply_name") String reply_name,
									@RequestParam(value = "whoLoginNow", required = false) String whoLoginNow,
									HttpServletRequest request) {
		System.out.println("댓글삭제 reply_idx : " + reply_idx);
		System.out.println("댓글삭제 reply_board_idx : " + reply_board_idx);
		System.out.println("댓글삭제 user_id : " + reply_name);
		System.out.println("댓글삭제 whoLoginNow : " + whoLoginNow);		
				
		
		if (whoLoginNow != null && whoLoginNow.equals(reply_name)) {			
			int result = replyDao.reply_deleteDto(reply_idx, reply_board_idx, reply_name);
				if (result == 1) {
					System.out.println("댓글삭제 성공!");
					return "<script>alert('댓글삭제 성공!'); location.href='contentForm?board_idx=" + reply_board_idx + "';</script>";
				} else System.out.println("댓글삭제 실패ㅠㅠ");
				return "<script>alert('댓글삭제 실패ㅠㅠ'); location.href='contentForm?board_idx=" + reply_board_idx + "';</script>";
			
		} else {
			//System.out.println("댓글삭제 실패ㅠㅠ");
			return "<script>alert('작성자만 삭제할 수 있습니다.'); location.href='contentForm?board_idx=" + reply_board_idx + "';</script>";
		}
	}
	
	// 게시판 조회 + 페이징 + 검색
	@RequestMapping("/listForm")
	public String totalListForm(@RequestParam(value="searchType", required = false) String searchType,
	                            @RequestParam(value="searchKeyword", required = false) String searchKeyword,
	                            Model model, Criteria cri) {
	    System.out.println("searchType : " + searchType);
	    System.out.println("searchKeyword : " + searchKeyword);

	    if (searchType == null || searchKeyword == null || searchType.isEmpty() || searchKeyword.isEmpty()) {
	        // 검색 조건이 없는 경우 전체 목록 조회
	        cri.setSearchType(null);
	        cri.setSearchKeyword(null);
	    } else {
	        // 검색 조건이 있는 경우 검색 결과 조회
	        cri.setSearchType(searchType);
	        cri.setSearchKeyword(searchKeyword);
	    }

	    List<BoardDto> getList = boardDao.getList(cri);
	    System.out.println("getList : " + getList);
	    PageDto pageDto = new PageDto(cri, boardDao.getTotal(cri));
	    System.out.println("pageDto : " + pageDto);

	    model.addAttribute("pageDto", pageDto);
	    model.addAttribute("list", getList);

	    return "listForm";
	}
}

	
