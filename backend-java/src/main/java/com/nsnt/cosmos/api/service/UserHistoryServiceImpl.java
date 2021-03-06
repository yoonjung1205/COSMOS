package com.nsnt.cosmos.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsnt.cosmos.api.response.UserHistoryWeekDtoRes;
import com.nsnt.cosmos.db.entity.User;
import com.nsnt.cosmos.db.entity.UserHistory;
import com.nsnt.cosmos.db.entity.UserHistoryDay;
import com.nsnt.cosmos.db.entity.UserHistoryMonth;
import com.nsnt.cosmos.db.repository.UserHistoryDayRepository;
import com.nsnt.cosmos.db.repository.UserHistoryMonthRepository;
import com.nsnt.cosmos.db.repository.UserHistoryRepository;
import com.nsnt.cosmos.db.repository.UserHistoryWeekRepository;

/**
 *	히스토리 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("HistoryServiceImpl")
public class UserHistoryServiceImpl implements UserHistoryService {
	@Autowired
	UserHistoryRepository userHistoryRepository;
	
	@Autowired
	UserHistoryDayRepository userhistoryDayRepository;
	
	@Autowired
	UserHistoryWeekRepository userhistoryWeekRepository;
	
	@Autowired
	UserHistoryMonthRepository userhistoryMonthRepository;

	
	/** 비공개 스터디 시작 시간 입력 */
	@Override
	public UserHistory setPrivateStartTime(String user_id) {
		UserHistory userhistory = new UserHistory();
		LocalDateTime now = LocalDateTime.now();
		User user = new User();
		user.setUserId(user_id);
		userhistory.setHistory_date(now);
		userhistory.setUserStartTime(now);
		userhistory.setUserFinishTime(now);
		userhistory.setUser(user);
		
		return userHistoryRepository.save(userhistory);	
	}
	
	/** 비공개 스터디 종료 시간 입력 */
	public void setPrivateUserFinishTime(String user_id, Long userhistory_no) {
		LocalDateTime now = LocalDateTime.now();
		userHistoryRepository.setFinishTime(user_id, now, userhistory_no);
	}
	
	/** 공개 스터디 시작 시간 입력 */
	@Override
	public UserHistory setPublicUserStartTime(String start_time, String user_id) {
		UserHistory userhistory = new UserHistory();
		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime user_start_time = LocalDateTime.parse(LocalDate.now() + " " + start_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		System.out.println(">>>>>>>>>> 시작 시간 : " + user_start_time);
		
		User user = new User();
		user.setUserId(user_id);
		userhistory.setHistory_date(now);
		userhistory.setUserStartTime(user_start_time);
		userhistory.setUserFinishTime(user_start_time);
		userhistory.setUser(user);
		
		System.out.println(">>>>>>>>>>>>>> userhistory : " + userhistory.toString());

		return userHistoryRepository.save(userhistory);	
	}
	
	/** 공개 스터디 종료 시간 입력 */
	@Override
	public void setPublicUserFinishTime(String finish_time, Long userhistory_no, String user_id) {
		LocalDateTime user_finish_time = LocalDateTime.parse(LocalDate.now() + " " + finish_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		System.out.println(">>>>>>>>>> 종료 시간 : " + user_finish_time);
		
		
		userHistoryRepository.setFinishTime(user_id, user_finish_time, userhistory_no);
	}
	
	/** 일별 기록 조회 */
	@Override
	public List<UserHistoryDay> getDailyUserHistory() {
		LocalDateTime now = LocalDateTime.now();
		List<UserHistoryDay> userhistoryday = userhistoryDayRepository.findAllDayUserHistory(now);
		
		for(int i=0; i<userhistoryday.size(); i++) {
			System.out.println(userhistoryday.get(i).toString());
		}
		
		return userhistoryday;
	}
	
	/** 주별 기록 조회 */
	public List<UserHistoryWeekDtoRes> getWeeklyUserHistory() {
		LocalDateTime now = LocalDateTime.now();
		List<Object[]> userhistoryweek = userhistoryWeekRepository.findAllWeekUserHistory(now);
		List<UserHistoryWeekDtoRes> result = new ArrayList<UserHistoryWeekDtoRes>();
		
		for(int i=0; i<userhistoryweek.size(); i++) {
			
			Object userId = userhistoryweek.get(i)[0];
			Object totalTime = userhistoryweek.get(i)[1]; 
			UserHistoryWeekDtoRes week = new UserHistoryWeekDtoRes();
			
			week.setTotal_time(totalTime);
			week.setUser_id(userId);
			
			result.add(week);
		}
		
		return result;
	} 
	
	/** 월별 기록 조회 */
	public List<UserHistoryMonth> getMonthlyUserHistory() {
		LocalDateTime now = LocalDateTime.now();
		List<UserHistoryMonth> userhistorymonth = userhistoryMonthRepository.findAllMonthUserHistory(now);
		
		for(int i=0; i<userhistorymonth.size(); i++) {
			System.out.println(userhistorymonth.get(i).toString());
		}
		
		return userhistorymonth;
	}
	
	/** 유저별 총 누적 시간 조회 */
	@Override
	public int getUserTotalTime(String user_id) {
		int total_time = userhistoryDayRepository.getTotalTime(user_id);
		return total_time;
	}
	
}
