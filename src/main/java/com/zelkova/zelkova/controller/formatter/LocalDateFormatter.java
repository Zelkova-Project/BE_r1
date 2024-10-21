package com.zelkova.zelkova.controller.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * 브라우저 -> 서버 : 문자열
 * 서버: LocalDate 타입으로 처리
 * 일괄적으로 JSON으로 넘어오는 데이터 'LocalDate' 타입으로 처리
 */
public class LocalDateFormatter implements Formatter<LocalDate>{


  @Override
  public LocalDate parse(String text, Locale locale) throws ParseException {
    return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  @Override
  public String print(LocalDate object, Locale locale) {
    return DateTimeFormatter.ofPattern("yyyy-MM-DD").format(object);
  } 
    
}
